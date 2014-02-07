package org.apache.hadoop.hive.cassandra.output;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.Mutation;
import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.hadoop.hive.cassandra.CassandraProxyClient;
import org.apache.hadoop.hive.cassandra.serde.AbstractColumnSerDe;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveContext;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.JobConf;

/**
 * This represents a standard column family. It implements hadoop Writable interface.
 *
 */
public class CassandraPut extends CassandraAbstractPut implements Writable {

  private ByteBuffer key;
  private List<CassandraColumn> columns;
  private static volatile AtomicInteger INCREMENTAL_ROW_COUNT = new AtomicInteger(-1);
  private static volatile AtomicLong lastAccessedTime = new AtomicLong(-1);

  public CassandraPut(){
    columns = new ArrayList<CassandraColumn>();
  }

  public CassandraPut(ByteBuffer key){
    this();
    this.key = key;
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    int keyLen = in.readInt();
    byte[] keyBytes = new byte[keyLen];
    in.readFully(keyBytes);
    key = ByteBuffer.wrap(keyBytes);
    int ilevel = in.readInt();
    int cols = in.readInt();
    for (int i =0;i<cols;i++){
      CassandraColumn cc = new CassandraColumn();
      cc.readFields(in);
      columns.add(cc);
    }
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeInt(key.remaining());
    out.write(ByteBufferUtil.getArray(key));
    out.writeInt(1);
    out.writeInt(columns.size());
    for (CassandraColumn c: columns){
      c.write(out);
    }
  }

  public ByteBuffer getKey() {
    return key;
  }

  public void setKey(ByteBuffer key) {
    this.key = key;
  }

  public List<CassandraColumn> getColumns() {
    return columns;
  }

  public void setColumns(List<CassandraColumn> columns) {
    this.columns = columns;
  }

  @Override
  public String toString(){
    StringBuilder sb = new StringBuilder();
    sb.append("key: ");
    sb.append(this.key);
    for (CassandraColumn col:this.columns){
      sb.append( "column : [" );
      sb.append( col.toString() );
      sb.append( "]" );
    }
    return sb.toString();
  }

  @Override
  public void write(String keySpace, CassandraProxyClient client, JobConf jc) throws IOException {
      ConsistencyLevel flevel = getConsistencyLevel(jc);
      int batchMutation = getBatchMutationSize(jc);
      boolean isIncrementalIndex = Boolean.parseBoolean(jc.
              get(AbstractColumnSerDe.CASSANDRA_ENABLE_INCREMENTAL_PROCESS));

      Map<ByteBuffer, Map<String, List<Mutation>>> mutation_map = new HashMap<ByteBuffer, Map<String, List<Mutation>>>();
      Map<ByteBuffer, Map<String, List<Mutation>>> index_mutation_map = new HashMap<ByteBuffer, Map<String, List<Mutation>>>();

      Map<String, List<Mutation>> maps = new HashMap<String, List<Mutation>>();
      Map<String, List<Mutation>> specialIndexMaps = new HashMap<String, List<Mutation>>();

      Map<String, Long> lastIndexRowTimeCache = new HashMap<String, Long>();
      Map<String, Long> lastUpdateColTimeStamp = new HashMap<String, Long>();

      int count = 0;
      // TODO check for counter
      for (CassandraColumn col : columns) {
          long timeStamp = System.currentTimeMillis();
          Column cassCol = new Column();
          cassCol.setValue(col.getValue());
          cassCol.setTimestamp(col.getTimeStamp());
          cassCol.setName(col.getColumn());

          ColumnOrSuperColumn thisCol = new ColumnOrSuperColumn();
          thisCol.setColumn(cassCol);

          Mutation mutation = new Mutation();
          mutation.setColumn_or_supercolumn(thisCol);

          List<Mutation> mutList = maps.get(col.getColumnFamily());
          if (mutList == null) {
              mutList = new ArrayList<Mutation>();
              maps.put(col.getColumnFamily(), mutList);
          }

          mutList.add(mutation);

          if (isIncrementalIndex) {
              Long indexCFRowKey = getIndexCFRowKey(timeStamp);
              Long lastIndexTimeStamp = lastIndexRowTimeCache.get(col.getColumnFamily());

              if (lastIndexTimeStamp == null || !lastIndexTimeStamp.equals(indexCFRowKey)) {
                  Column indexColumn = new Column();
                  indexColumn.setValue(ByteBufferUtil.bytes(String.valueOf(indexCFRowKey)));
                  indexColumn.setTimestamp(timeStamp);
                  indexColumn.setName(ByteBufferUtil.bytes(indexCFRowKey));

                  ColumnOrSuperColumn specialIndexCol = new ColumnOrSuperColumn();
                  specialIndexCol.setColumn(indexColumn);

                  Mutation indexMutation = new Mutation();
                  indexMutation.setColumn_or_supercolumn(specialIndexCol);

                  String columnFamilyName = resolveIndexCFName(col.getColumnFamily());

                  List<Mutation> mutationList = specialIndexMaps.get(columnFamilyName);

                  if (null == mutationList) {
                      mutationList = new ArrayList<Mutation>();
                      specialIndexMaps.put(columnFamilyName, mutationList);
                  }
                  mutationList.add(indexMutation);
                  lastIndexRowTimeCache.put(col.getColumnFamily(), indexCFRowKey);
              }

          //since the insert occurs for each rowKey, can only cache the column family name and lastAccesed timestamp
          // value for that particular rowKey
              synchronized (CassandraPut.class) {
                  int incCount = 0;
                  int id = getTaskId(jc);
                  if (lastAccessedTime.get() != timeStamp) {
                      lastAccessedTime.set(timeStamp);
                      INCREMENTAL_ROW_COUNT.set(0);
                  } else {
                      incCount = INCREMENTAL_ROW_COUNT.getAndIncrement();
                  }
                  String timestampStr = String.valueOf(timeStamp) +
                          String.format("%02d", id) +
                          String.format("%02d", incCount);

                  lastUpdateColTimeStamp.put(resolveIndexCFName(col.getColumnFamily()), Long.parseLong(timestampStr));
          }
          }





          count++;

          if (count == batchMutation) {
              mutation_map.put(key, maps);
              commitChanges(keySpace, client, flevel, mutation_map);

              if (isIncrementalIndex) {
                  //incremental changes
                  String indexKeyspace = HiveContext.getCurrentContext().getConf().get(HiveConf.ConfVars.
                          HIVE_INCREMENTAL_PROCESS_KEYSPACE.toString());
                  index_mutation_map = getIndexColumnFamilyMutationMap(lastIndexRowTimeCache, key, specialIndexMaps, index_mutation_map);
                  commitChanges(indexKeyspace, client, flevel, index_mutation_map);
              }

              //reset mutation map, maps and count;
              mutation_map = new HashMap<ByteBuffer, Map<String, List<Mutation>>>();
              maps = new HashMap<String, List<Mutation>>();
              index_mutation_map = new HashMap<ByteBuffer, Map<String, List<Mutation>>>();
              specialIndexMaps = new HashMap<String, List<Mutation>>();

              lastIndexRowTimeCache = new HashMap<String, Long>();
              lastUpdateColTimeStamp = new HashMap<String, Long>();
              count = 0;
          }
      }


      if (count > 0) {
          mutation_map.put(key, maps);
          commitChanges(keySpace, client, flevel, mutation_map);

          if (isIncrementalIndex) {
              //incremental changes
              String indexKeyspace = HiveContext.getCurrentContext().getConf().get(HiveConf.ConfVars.
                      HIVE_INCREMENTAL_PROCESS_KEYSPACE.toString());
              index_mutation_map = getIndexColumnFamilyMutationMap(lastUpdateColTimeStamp, key, specialIndexMaps, index_mutation_map);
              commitChanges(indexKeyspace, client, flevel, index_mutation_map);
          }
      }
  }


}