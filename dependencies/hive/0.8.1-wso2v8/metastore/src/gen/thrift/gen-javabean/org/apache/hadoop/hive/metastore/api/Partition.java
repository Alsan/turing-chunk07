/**
 * Autogenerated by Thrift Compiler (0.7.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package org.apache.hadoop.hive.metastore.api;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Partition implements org.apache.thrift.TBase<Partition, Partition._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Partition");

  private static final org.apache.thrift.protocol.TField VALUES_FIELD_DESC = new org.apache.thrift.protocol.TField("values", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField DB_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("dbName", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField TABLE_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("tableName", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField CREATE_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("createTime", org.apache.thrift.protocol.TType.I32, (short)4);
  private static final org.apache.thrift.protocol.TField LAST_ACCESS_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("lastAccessTime", org.apache.thrift.protocol.TType.I32, (short)5);
  private static final org.apache.thrift.protocol.TField SD_FIELD_DESC = new org.apache.thrift.protocol.TField("sd", org.apache.thrift.protocol.TType.STRUCT, (short)6);
  private static final org.apache.thrift.protocol.TField PARAMETERS_FIELD_DESC = new org.apache.thrift.protocol.TField("parameters", org.apache.thrift.protocol.TType.MAP, (short)7);
  private static final org.apache.thrift.protocol.TField PRIVILEGES_FIELD_DESC = new org.apache.thrift.protocol.TField("privileges", org.apache.thrift.protocol.TType.STRUCT, (short)8);

  private List<String> values; // required
  private String dbName; // required
  private String tableName; // required
  private int createTime; // required
  private int lastAccessTime; // required
  private StorageDescriptor sd; // required
  private Map<String,String> parameters; // required
  private PrincipalPrivilegeSet privileges; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    VALUES((short)1, "values"),
    DB_NAME((short)2, "dbName"),
    TABLE_NAME((short)3, "tableName"),
    CREATE_TIME((short)4, "createTime"),
    LAST_ACCESS_TIME((short)5, "lastAccessTime"),
    SD((short)6, "sd"),
    PARAMETERS((short)7, "parameters"),
    PRIVILEGES((short)8, "privileges");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // VALUES
          return VALUES;
        case 2: // DB_NAME
          return DB_NAME;
        case 3: // TABLE_NAME
          return TABLE_NAME;
        case 4: // CREATE_TIME
          return CREATE_TIME;
        case 5: // LAST_ACCESS_TIME
          return LAST_ACCESS_TIME;
        case 6: // SD
          return SD;
        case 7: // PARAMETERS
          return PARAMETERS;
        case 8: // PRIVILEGES
          return PRIVILEGES;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __CREATETIME_ISSET_ID = 0;
  private static final int __LASTACCESSTIME_ISSET_ID = 1;
  private BitSet __isset_bit_vector = new BitSet(2);

  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.VALUES, new org.apache.thrift.meta_data.FieldMetaData("values", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    tmpMap.put(_Fields.DB_NAME, new org.apache.thrift.meta_data.FieldMetaData("dbName", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.TABLE_NAME, new org.apache.thrift.meta_data.FieldMetaData("tableName", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.CREATE_TIME, new org.apache.thrift.meta_data.FieldMetaData("createTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.LAST_ACCESS_TIME, new org.apache.thrift.meta_data.FieldMetaData("lastAccessTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.SD, new org.apache.thrift.meta_data.FieldMetaData("sd", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, StorageDescriptor.class)));
    tmpMap.put(_Fields.PARAMETERS, new org.apache.thrift.meta_data.FieldMetaData("parameters", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING), 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    tmpMap.put(_Fields.PRIVILEGES, new org.apache.thrift.meta_data.FieldMetaData("privileges", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, PrincipalPrivilegeSet.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Partition.class, metaDataMap);
  }

  public Partition() {
  }

  public Partition(
    List<String> values,
    String dbName,
    String tableName,
    int createTime,
    int lastAccessTime,
    StorageDescriptor sd,
    Map<String,String> parameters)
  {
    this();
    this.values = values;
    this.dbName = dbName;
    this.tableName = tableName;
    this.createTime = createTime;
    setCreateTimeIsSet(true);
    this.lastAccessTime = lastAccessTime;
    setLastAccessTimeIsSet(true);
    this.sd = sd;
    this.parameters = parameters;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Partition(Partition other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    if (other.isSetValues()) {
      List<String> __this__values = new ArrayList<String>();
      for (String other_element : other.values) {
        __this__values.add(other_element);
      }
      this.values = __this__values;
    }
    if (other.isSetDbName()) {
      this.dbName = other.dbName;
    }
    if (other.isSetTableName()) {
      this.tableName = other.tableName;
    }
    this.createTime = other.createTime;
    this.lastAccessTime = other.lastAccessTime;
    if (other.isSetSd()) {
      this.sd = new StorageDescriptor(other.sd);
    }
    if (other.isSetParameters()) {
      Map<String,String> __this__parameters = new HashMap<String,String>();
      for (Map.Entry<String, String> other_element : other.parameters.entrySet()) {

        String other_element_key = other_element.getKey();
        String other_element_value = other_element.getValue();

        String __this__parameters_copy_key = other_element_key;

        String __this__parameters_copy_value = other_element_value;

        __this__parameters.put(__this__parameters_copy_key, __this__parameters_copy_value);
      }
      this.parameters = __this__parameters;
    }
    if (other.isSetPrivileges()) {
      this.privileges = new PrincipalPrivilegeSet(other.privileges);
    }
  }

  public Partition deepCopy() {
    return new Partition(this);
  }

  @Override
  public void clear() {
    this.values = null;
    this.dbName = null;
    this.tableName = null;
    setCreateTimeIsSet(false);
    this.createTime = 0;
    setLastAccessTimeIsSet(false);
    this.lastAccessTime = 0;
    this.sd = null;
    this.parameters = null;
    this.privileges = null;
  }

  public int getValuesSize() {
    return (this.values == null) ? 0 : this.values.size();
  }

  public java.util.Iterator<String> getValuesIterator() {
    return (this.values == null) ? null : this.values.iterator();
  }

  public void addToValues(String elem) {
    if (this.values == null) {
      this.values = new ArrayList<String>();
    }
    this.values.add(elem);
  }

  public List<String> getValues() {
    return this.values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }

  public void unsetValues() {
    this.values = null;
  }

  /** Returns true if field values is set (has been assigned a value) and false otherwise */
  public boolean isSetValues() {
    return this.values != null;
  }

  public void setValuesIsSet(boolean value) {
    if (!value) {
      this.values = null;
    }
  }

  public String getDbName() {
    return this.dbName;
  }

  public void setDbName(String dbName) {
    this.dbName = dbName;
  }

  public void unsetDbName() {
    this.dbName = null;
  }

  /** Returns true if field dbName is set (has been assigned a value) and false otherwise */
  public boolean isSetDbName() {
    return this.dbName != null;
  }

  public void setDbNameIsSet(boolean value) {
    if (!value) {
      this.dbName = null;
    }
  }

  public String getTableName() {
    return this.tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public void unsetTableName() {
    this.tableName = null;
  }

  /** Returns true if field tableName is set (has been assigned a value) and false otherwise */
  public boolean isSetTableName() {
    return this.tableName != null;
  }

  public void setTableNameIsSet(boolean value) {
    if (!value) {
      this.tableName = null;
    }
  }

  public int getCreateTime() {
    return this.createTime;
  }

  public void setCreateTime(int createTime) {
    this.createTime = createTime;
    setCreateTimeIsSet(true);
  }

  public void unsetCreateTime() {
    __isset_bit_vector.clear(__CREATETIME_ISSET_ID);
  }

  /** Returns true if field createTime is set (has been assigned a value) and false otherwise */
  public boolean isSetCreateTime() {
    return __isset_bit_vector.get(__CREATETIME_ISSET_ID);
  }

  public void setCreateTimeIsSet(boolean value) {
    __isset_bit_vector.set(__CREATETIME_ISSET_ID, value);
  }

  public int getLastAccessTime() {
    return this.lastAccessTime;
  }

  public void setLastAccessTime(int lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
    setLastAccessTimeIsSet(true);
  }

  public void unsetLastAccessTime() {
    __isset_bit_vector.clear(__LASTACCESSTIME_ISSET_ID);
  }

  /** Returns true if field lastAccessTime is set (has been assigned a value) and false otherwise */
  public boolean isSetLastAccessTime() {
    return __isset_bit_vector.get(__LASTACCESSTIME_ISSET_ID);
  }

  public void setLastAccessTimeIsSet(boolean value) {
    __isset_bit_vector.set(__LASTACCESSTIME_ISSET_ID, value);
  }

  public StorageDescriptor getSd() {
    return this.sd;
  }

  public void setSd(StorageDescriptor sd) {
    this.sd = sd;
  }

  public void unsetSd() {
    this.sd = null;
  }

  /** Returns true if field sd is set (has been assigned a value) and false otherwise */
  public boolean isSetSd() {
    return this.sd != null;
  }

  public void setSdIsSet(boolean value) {
    if (!value) {
      this.sd = null;
    }
  }

  public int getParametersSize() {
    return (this.parameters == null) ? 0 : this.parameters.size();
  }

  public void putToParameters(String key, String val) {
    if (this.parameters == null) {
      this.parameters = new HashMap<String,String>();
    }
    this.parameters.put(key, val);
  }

  public Map<String,String> getParameters() {
    return this.parameters;
  }

  public void setParameters(Map<String,String> parameters) {
    this.parameters = parameters;
  }

  public void unsetParameters() {
    this.parameters = null;
  }

  /** Returns true if field parameters is set (has been assigned a value) and false otherwise */
  public boolean isSetParameters() {
    return this.parameters != null;
  }

  public void setParametersIsSet(boolean value) {
    if (!value) {
      this.parameters = null;
    }
  }

  public PrincipalPrivilegeSet getPrivileges() {
    return this.privileges;
  }

  public void setPrivileges(PrincipalPrivilegeSet privileges) {
    this.privileges = privileges;
  }

  public void unsetPrivileges() {
    this.privileges = null;
  }

  /** Returns true if field privileges is set (has been assigned a value) and false otherwise */
  public boolean isSetPrivileges() {
    return this.privileges != null;
  }

  public void setPrivilegesIsSet(boolean value) {
    if (!value) {
      this.privileges = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case VALUES:
      if (value == null) {
        unsetValues();
      } else {
        setValues((List<String>)value);
      }
      break;

    case DB_NAME:
      if (value == null) {
        unsetDbName();
      } else {
        setDbName((String)value);
      }
      break;

    case TABLE_NAME:
      if (value == null) {
        unsetTableName();
      } else {
        setTableName((String)value);
      }
      break;

    case CREATE_TIME:
      if (value == null) {
        unsetCreateTime();
      } else {
        setCreateTime((Integer)value);
      }
      break;

    case LAST_ACCESS_TIME:
      if (value == null) {
        unsetLastAccessTime();
      } else {
        setLastAccessTime((Integer)value);
      }
      break;

    case SD:
      if (value == null) {
        unsetSd();
      } else {
        setSd((StorageDescriptor)value);
      }
      break;

    case PARAMETERS:
      if (value == null) {
        unsetParameters();
      } else {
        setParameters((Map<String,String>)value);
      }
      break;

    case PRIVILEGES:
      if (value == null) {
        unsetPrivileges();
      } else {
        setPrivileges((PrincipalPrivilegeSet)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case VALUES:
      return getValues();

    case DB_NAME:
      return getDbName();

    case TABLE_NAME:
      return getTableName();

    case CREATE_TIME:
      return Integer.valueOf(getCreateTime());

    case LAST_ACCESS_TIME:
      return Integer.valueOf(getLastAccessTime());

    case SD:
      return getSd();

    case PARAMETERS:
      return getParameters();

    case PRIVILEGES:
      return getPrivileges();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case VALUES:
      return isSetValues();
    case DB_NAME:
      return isSetDbName();
    case TABLE_NAME:
      return isSetTableName();
    case CREATE_TIME:
      return isSetCreateTime();
    case LAST_ACCESS_TIME:
      return isSetLastAccessTime();
    case SD:
      return isSetSd();
    case PARAMETERS:
      return isSetParameters();
    case PRIVILEGES:
      return isSetPrivileges();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Partition)
      return this.equals((Partition)that);
    return false;
  }

  public boolean equals(Partition that) {
    if (that == null)
      return false;

    boolean this_present_values = true && this.isSetValues();
    boolean that_present_values = true && that.isSetValues();
    if (this_present_values || that_present_values) {
      if (!(this_present_values && that_present_values))
        return false;
      if (!this.values.equals(that.values))
        return false;
    }

    boolean this_present_dbName = true && this.isSetDbName();
    boolean that_present_dbName = true && that.isSetDbName();
    if (this_present_dbName || that_present_dbName) {
      if (!(this_present_dbName && that_present_dbName))
        return false;
      if (!this.dbName.equals(that.dbName))
        return false;
    }

    boolean this_present_tableName = true && this.isSetTableName();
    boolean that_present_tableName = true && that.isSetTableName();
    if (this_present_tableName || that_present_tableName) {
      if (!(this_present_tableName && that_present_tableName))
        return false;
      if (!this.tableName.equals(that.tableName))
        return false;
    }

    boolean this_present_createTime = true;
    boolean that_present_createTime = true;
    if (this_present_createTime || that_present_createTime) {
      if (!(this_present_createTime && that_present_createTime))
        return false;
      if (this.createTime != that.createTime)
        return false;
    }

    boolean this_present_lastAccessTime = true;
    boolean that_present_lastAccessTime = true;
    if (this_present_lastAccessTime || that_present_lastAccessTime) {
      if (!(this_present_lastAccessTime && that_present_lastAccessTime))
        return false;
      if (this.lastAccessTime != that.lastAccessTime)
        return false;
    }

    boolean this_present_sd = true && this.isSetSd();
    boolean that_present_sd = true && that.isSetSd();
    if (this_present_sd || that_present_sd) {
      if (!(this_present_sd && that_present_sd))
        return false;
      if (!this.sd.equals(that.sd))
        return false;
    }

    boolean this_present_parameters = true && this.isSetParameters();
    boolean that_present_parameters = true && that.isSetParameters();
    if (this_present_parameters || that_present_parameters) {
      if (!(this_present_parameters && that_present_parameters))
        return false;
      if (!this.parameters.equals(that.parameters))
        return false;
    }

    boolean this_present_privileges = true && this.isSetPrivileges();
    boolean that_present_privileges = true && that.isSetPrivileges();
    if (this_present_privileges || that_present_privileges) {
      if (!(this_present_privileges && that_present_privileges))
        return false;
      if (!this.privileges.equals(that.privileges))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(Partition other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    Partition typedOther = (Partition)other;

    lastComparison = Boolean.valueOf(isSetValues()).compareTo(typedOther.isSetValues());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetValues()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.values, typedOther.values);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetDbName()).compareTo(typedOther.isSetDbName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDbName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.dbName, typedOther.dbName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTableName()).compareTo(typedOther.isSetTableName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTableName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tableName, typedOther.tableName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCreateTime()).compareTo(typedOther.isSetCreateTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCreateTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.createTime, typedOther.createTime);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetLastAccessTime()).compareTo(typedOther.isSetLastAccessTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLastAccessTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.lastAccessTime, typedOther.lastAccessTime);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetSd()).compareTo(typedOther.isSetSd());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSd()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.sd, typedOther.sd);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetParameters()).compareTo(typedOther.isSetParameters());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetParameters()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.parameters, typedOther.parameters);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPrivileges()).compareTo(typedOther.isSetPrivileges());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPrivileges()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.privileges, typedOther.privileges);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    org.apache.thrift.protocol.TField field;
    iprot.readStructBegin();
    while (true)
    {
      field = iprot.readFieldBegin();
      if (field.type == org.apache.thrift.protocol.TType.STOP) { 
        break;
      }
      switch (field.id) {
        case 1: // VALUES
          if (field.type == org.apache.thrift.protocol.TType.LIST) {
            {
              org.apache.thrift.protocol.TList _list75 = iprot.readListBegin();
              this.values = new ArrayList<String>(_list75.size);
              for (int _i76 = 0; _i76 < _list75.size; ++_i76)
              {
                String _elem77; // required
                _elem77 = iprot.readString();
                this.values.add(_elem77);
              }
              iprot.readListEnd();
            }
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 2: // DB_NAME
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.dbName = iprot.readString();
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 3: // TABLE_NAME
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.tableName = iprot.readString();
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 4: // CREATE_TIME
          if (field.type == org.apache.thrift.protocol.TType.I32) {
            this.createTime = iprot.readI32();
            setCreateTimeIsSet(true);
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 5: // LAST_ACCESS_TIME
          if (field.type == org.apache.thrift.protocol.TType.I32) {
            this.lastAccessTime = iprot.readI32();
            setLastAccessTimeIsSet(true);
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 6: // SD
          if (field.type == org.apache.thrift.protocol.TType.STRUCT) {
            this.sd = new StorageDescriptor();
            this.sd.read(iprot);
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 7: // PARAMETERS
          if (field.type == org.apache.thrift.protocol.TType.MAP) {
            {
              org.apache.thrift.protocol.TMap _map78 = iprot.readMapBegin();
              this.parameters = new HashMap<String,String>(2*_map78.size);
              for (int _i79 = 0; _i79 < _map78.size; ++_i79)
              {
                String _key80; // required
                String _val81; // required
                _key80 = iprot.readString();
                _val81 = iprot.readString();
                this.parameters.put(_key80, _val81);
              }
              iprot.readMapEnd();
            }
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 8: // PRIVILEGES
          if (field.type == org.apache.thrift.protocol.TType.STRUCT) {
            this.privileges = new PrincipalPrivilegeSet();
            this.privileges.read(iprot);
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        default:
          org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
      }
      iprot.readFieldEnd();
    }
    iprot.readStructEnd();
    validate();
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    validate();

    oprot.writeStructBegin(STRUCT_DESC);
    if (this.values != null) {
      oprot.writeFieldBegin(VALUES_FIELD_DESC);
      {
        oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, this.values.size()));
        for (String _iter82 : this.values)
        {
          oprot.writeString(_iter82);
        }
        oprot.writeListEnd();
      }
      oprot.writeFieldEnd();
    }
    if (this.dbName != null) {
      oprot.writeFieldBegin(DB_NAME_FIELD_DESC);
      oprot.writeString(this.dbName);
      oprot.writeFieldEnd();
    }
    if (this.tableName != null) {
      oprot.writeFieldBegin(TABLE_NAME_FIELD_DESC);
      oprot.writeString(this.tableName);
      oprot.writeFieldEnd();
    }
    oprot.writeFieldBegin(CREATE_TIME_FIELD_DESC);
    oprot.writeI32(this.createTime);
    oprot.writeFieldEnd();
    oprot.writeFieldBegin(LAST_ACCESS_TIME_FIELD_DESC);
    oprot.writeI32(this.lastAccessTime);
    oprot.writeFieldEnd();
    if (this.sd != null) {
      oprot.writeFieldBegin(SD_FIELD_DESC);
      this.sd.write(oprot);
      oprot.writeFieldEnd();
    }
    if (this.parameters != null) {
      oprot.writeFieldBegin(PARAMETERS_FIELD_DESC);
      {
        oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING, this.parameters.size()));
        for (Map.Entry<String, String> _iter83 : this.parameters.entrySet())
        {
          oprot.writeString(_iter83.getKey());
          oprot.writeString(_iter83.getValue());
        }
        oprot.writeMapEnd();
      }
      oprot.writeFieldEnd();
    }
    if (this.privileges != null) {
      if (isSetPrivileges()) {
        oprot.writeFieldBegin(PRIVILEGES_FIELD_DESC);
        this.privileges.write(oprot);
        oprot.writeFieldEnd();
      }
    }
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Partition(");
    boolean first = true;

    sb.append("values:");
    if (this.values == null) {
      sb.append("null");
    } else {
      sb.append(this.values);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("dbName:");
    if (this.dbName == null) {
      sb.append("null");
    } else {
      sb.append(this.dbName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("tableName:");
    if (this.tableName == null) {
      sb.append("null");
    } else {
      sb.append(this.tableName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("createTime:");
    sb.append(this.createTime);
    first = false;
    if (!first) sb.append(", ");
    sb.append("lastAccessTime:");
    sb.append(this.lastAccessTime);
    first = false;
    if (!first) sb.append(", ");
    sb.append("sd:");
    if (this.sd == null) {
      sb.append("null");
    } else {
      sb.append(this.sd);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("parameters:");
    if (this.parameters == null) {
      sb.append("null");
    } else {
      sb.append(this.parameters);
    }
    first = false;
    if (isSetPrivileges()) {
      if (!first) sb.append(", ");
      sb.append("privileges:");
      if (this.privileges == null) {
        sb.append("null");
      } else {
        sb.append(this.privileges);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bit_vector = new BitSet(1);
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

}

