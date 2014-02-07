/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package org.apache.cassandra.db.migration.avro;

@SuppressWarnings("all")
public class ColumnDef extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = org.apache.avro.Schema.parse("{\"type\":\"record\",\"name\":\"ColumnDef\",\"namespace\":\"org.apache.cassandra.db.migration.avro\",\"fields\":[{\"name\":\"name\",\"type\":\"bytes\"},{\"name\":\"validation_class\",\"type\":\"string\"},{\"name\":\"index_type\",\"type\":[{\"type\":\"enum\",\"name\":\"IndexType\",\"symbols\":[\"KEYS\",\"CUSTOM\"],\"aliases\":[\"org.apache.cassandra.config.avro.IndexType\"]},\"null\"]},{\"name\":\"index_name\",\"type\":[\"string\",\"null\"]},{\"name\":\"index_options\",\"type\":[\"null\",{\"type\":\"map\",\"values\":\"string\"}],\"default\":null}],\"aliases\":[\"org.apache.cassandra.config.avro.ColumnDef\"]}");
  public java.nio.ByteBuffer name;
  public java.lang.CharSequence validation_class;
  public org.apache.cassandra.db.migration.avro.IndexType index_type;
  public java.lang.CharSequence index_name;
  public java.util.Map<java.lang.CharSequence,java.lang.CharSequence> index_options;
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return name;
    case 1: return validation_class;
    case 2: return index_type;
    case 3: return index_name;
    case 4: return index_options;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: name = (java.nio.ByteBuffer)value$; break;
    case 1: validation_class = (java.lang.CharSequence)value$; break;
    case 2: index_type = (org.apache.cassandra.db.migration.avro.IndexType)value$; break;
    case 3: index_name = (java.lang.CharSequence)value$; break;
    case 4: index_options = (java.util.Map<java.lang.CharSequence,java.lang.CharSequence>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
}
