package org.apache.hadoop.hive.cassandra;

import junit.framework.TestCase;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.cassandra.output.CassandraPut;
import org.apache.hadoop.hive.cassandra.serde.CassandraColumnSerDe;
import org.apache.hadoop.hive.serde2.lazy.LazyPrimitive;
import org.apache.hadoop.hive.serde2.lazy.objectinspector.LazySimpleStructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.StructField;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.MapWritable;

import java.util.List;
import java.util.Properties;

/*
    Tests the functionality of Timestamp data type support for Cassandra handler
 */
public class TimestampTest extends TestCase {

    public void testCreateLazyObjectForTimestamp() throws Exception {

        CassandraColumnSerDe serDe = new CassandraColumnSerDe();

        Properties props = new Properties();
        props.setProperty("columns", "key,name,time");
        props.setProperty("columns.types", "string,string:timestamp");
        props.put("cassandra.cf.name", "users");
        props.put("cassandra.columns.mapping", ":key,name,time");

        serDe.initialize(new Configuration(),props);

        // Get the row structure
        LazySimpleStructObjectInspector oi = (LazySimpleStructObjectInspector) serDe.getObjectInspector();
        List<? extends StructField> fieldRefs = oi.getAllStructFieldRefs();
        assertEquals(3, fieldRefs.size());

        //data
        MapWritable map = new MapWritable();
        map.put(new BytesWritable("name".getBytes()),new BytesWritable("Event1".getBytes()));
        map.put(new BytesWritable("time".getBytes()),new BytesWritable("2010-03-08 14:59:30.252".getBytes()));

        Object row = serDe.deserialize(map);

        for (int i = 0; i < fieldRefs.size(); i++) {
            Object fieldData = oi.getStructFieldData(row, fieldRefs.get(i));
            if (fieldData != null) {
                fieldData = ((LazyPrimitive<?, ?>)fieldData).getWritableObject();
                System.out.println(fieldData.toString());
            }
        }

        //serialize it
        CassandraPut put = (CassandraPut) serDe.serialize(row, oi);
        System.out.println(put.toString());
    }
}
