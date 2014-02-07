package org.apache.hadoop.hive.cassandra.output;

import org.apache.cassandra.thrift.*;
import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.hadoop.hive.cassandra.CassandraProxyClient;
import org.apache.hadoop.hive.cassandra.serde.AbstractColumnSerDe;
import org.apache.hadoop.mapred.JobConf;
import org.apache.thrift.TException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.*;

public abstract class CassandraAbstractPut implements Put {
 private static final String EVENT_INDEX_TABLE_PREFIX = "event_index_";
 public static final String EVENT_INDEX_ROWS_KEY = "INDEX_ROW";
    /**
     * Parse batch mutation size from job configuration. If none is defined, return the default value 500.
     *
     * @param jc job configuration
     * @return batch mutation size
     */
    protected int getBatchMutationSize(JobConf jc) {
        return jc.getInt(
                AbstractColumnSerDe.CASSANDRA_BATCH_MUTATION_SIZE,
                AbstractColumnSerDe.DEFAULT_BATCH_MUTATION_SIZE);
    }

    /**
     * Parse consistency level from job configuration. If none is defined,  or if the specified value is not a valid
     * <code>ConsistencyLevel</code>, return default consistency level ONE.
     *
     * @param jc job configuration
     * @return cassandra consistency level
     */
    protected static ConsistencyLevel getConsistencyLevel(JobConf jc) {
        String consistencyLevel = jc.get(AbstractColumnSerDe.CASSANDRA_CONSISTENCY_LEVEL,
                AbstractColumnSerDe.DEFAULT_CONSISTENCY_LEVEL);
        ConsistencyLevel level = null;
        try {
            level = ConsistencyLevel.valueOf(consistencyLevel);
        } catch (IllegalArgumentException e) {
            level = ConsistencyLevel.ONE;
        }

        return level;
    }

    /**
     * Commit the changes in mutation map to cassandra client for given keyspace with given consistency level.
     *
     * @param keySpace     cassandra key space
     * @param client       cassandra client
     * @param flevel       cassandra consistency level
     * @param mutation_map cassandra mutation map
     * @throws IOException when error happens in batch mutate
     */
    protected void commitChanges(String keySpace,
                                 CassandraProxyClient client,
                                 ConsistencyLevel flevel,
                                 Map<ByteBuffer, Map<String, List<Mutation>>> mutation_map) throws IOException {
        try {
            client.getProxyConnection().set_keyspace(keySpace);
            client.getProxyConnection().batch_mutate(mutation_map, flevel);
        } catch (InvalidRequestException e) {
            throw new IOException(e);
        } catch (UnavailableException e) {
            throw new IOException(e);
        } catch (TimedOutException e) {
            throw new IOException(e);
        } catch (TException e) {
            throw new IOException(e);
        }
    }

    //    private Map<ByteBuffer, Map<String, List<Mutation>>> getIndexCFMutationMap(Map<ByteBuffer,
//            Map<String, List<Mutation>>> mutationMap) {
//        if (Boolean.parseBoolean(HiveContext.getCurrentContext().getConf().get(AbstractColumnSerDe.CASSANDRA_ENABLE_INCREMENTAL_PROCESS))) {
//
//        }
//        return null;
//    }


    public String resolveIndexCFName(String cfName) {
        return EVENT_INDEX_TABLE_PREFIX + cfName;
    }

    public long getIndexCFRowKey(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();

    }

    public Map<ByteBuffer, Map<String, List<Mutation>>> getIndexColumnFamilyMutationMap(Map<String, Long> timeStampMap,
                                                                                        ByteBuffer rowKey,
                                                                                        Map<String, List<Mutation>> specialIndexMaps,
                                                                                        Map<ByteBuffer, Map<String, List<Mutation>>> index_mutation_map) throws CharacterCodingException {
        Set<String> columnFamilyNames = timeStampMap.keySet();
        Map<String, List<Mutation>> indexMaps = new HashMap<String, List<Mutation>>();

        for (String aColumnFamilyName : columnFamilyNames) {
            long timeStampKey = timeStampMap.get(aColumnFamilyName);
            long colTimeStamp = getTimeStampFromKey(timeStampKey);
            Column column = new Column();
            String rowKeyStr =  ByteBufferUtil.string(rowKey);
            column.setValue(ByteBufferUtil.bytes(rowKeyStr));
            column.setTimestamp(colTimeStamp);

            column.setName(ByteBufferUtil.bytes(timeStampKey));

            ColumnOrSuperColumn indexCol = new ColumnOrSuperColumn();
            indexCol.setColumn(column);

            Mutation indexMutation = new Mutation();
            indexMutation.setColumn_or_supercolumn(indexCol);


            List<Mutation> mutationList = indexMaps.get(aColumnFamilyName);

            if (null == mutationList) {
                mutationList = new ArrayList<Mutation>();
                indexMaps.put(aColumnFamilyName, mutationList);
            }
            mutationList.add(indexMutation);
            String timeStampStr = String.valueOf(getIndexCFRowKey(colTimeStamp));
            index_mutation_map.put(ByteBufferUtil.bytes(timeStampStr), indexMaps);
        }
        index_mutation_map.put(ByteBufferUtil.bytes(EVENT_INDEX_ROWS_KEY), specialIndexMaps);
        return index_mutation_map;

    }


    public int getTaskId(JobConf jobConf){
      String jobId = jobConf.get("mapred.tip.id");
      String[] jobIdSplit = jobId.split("_");
      return Integer.parseInt(jobIdSplit[2].trim());
    }


    private long getTimeStampFromKey(long timeKey){
       String timeKeyStr = String.valueOf(timeKey);
       return Long.parseLong(timeKeyStr.substring(0, timeKeyStr.length()-4));
    }


}
