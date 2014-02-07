/**
 * Autogenerated by Thrift Compiler (0.7.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package org.apache.cassandra.thrift;
/*
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */


import org.apache.commons.lang.builder.HashCodeBuilder;
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

/**
 * The semantics of start keys and tokens are slightly different.
 * Keys are start-inclusive; tokens are start-exclusive.  Token
 * ranges may also wrap -- that is, the end token may be less
 * than the start one.  Thus, a range from keyX to keyX is a
 * one-element range, but a range from tokenY to tokenY is the
 * full ring.
 */
public class KeyRange implements org.apache.thrift.TBase<KeyRange, KeyRange._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("KeyRange");

  private static final org.apache.thrift.protocol.TField START_KEY_FIELD_DESC = new org.apache.thrift.protocol.TField("start_key", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField END_KEY_FIELD_DESC = new org.apache.thrift.protocol.TField("end_key", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField START_TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("start_token", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField END_TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("end_token", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField ROW_FILTER_FIELD_DESC = new org.apache.thrift.protocol.TField("row_filter", org.apache.thrift.protocol.TType.LIST, (short)6);
  private static final org.apache.thrift.protocol.TField COUNT_FIELD_DESC = new org.apache.thrift.protocol.TField("count", org.apache.thrift.protocol.TType.I32, (short)5);

  public ByteBuffer start_key; // required
  public ByteBuffer end_key; // required
  public String start_token; // required
  public String end_token; // required
  public List<IndexExpression> row_filter; // required
  public int count; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    START_KEY((short)1, "start_key"),
    END_KEY((short)2, "end_key"),
    START_TOKEN((short)3, "start_token"),
    END_TOKEN((short)4, "end_token"),
    ROW_FILTER((short)6, "row_filter"),
    COUNT((short)5, "count");

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
        case 1: // START_KEY
          return START_KEY;
        case 2: // END_KEY
          return END_KEY;
        case 3: // START_TOKEN
          return START_TOKEN;
        case 4: // END_TOKEN
          return END_TOKEN;
        case 6: // ROW_FILTER
          return ROW_FILTER;
        case 5: // COUNT
          return COUNT;
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
  private static final int __COUNT_ISSET_ID = 0;
  private BitSet __isset_bit_vector = new BitSet(1);

  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.START_KEY, new org.apache.thrift.meta_data.FieldMetaData("start_key", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING        , true)));
    tmpMap.put(_Fields.END_KEY, new org.apache.thrift.meta_data.FieldMetaData("end_key", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING        , true)));
    tmpMap.put(_Fields.START_TOKEN, new org.apache.thrift.meta_data.FieldMetaData("start_token", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.END_TOKEN, new org.apache.thrift.meta_data.FieldMetaData("end_token", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ROW_FILTER, new org.apache.thrift.meta_data.FieldMetaData("row_filter", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, IndexExpression.class))));
    tmpMap.put(_Fields.COUNT, new org.apache.thrift.meta_data.FieldMetaData("count", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(KeyRange.class, metaDataMap);
  }

  public KeyRange() {
    this.count = 100;

  }

  public KeyRange(
    int count)
  {
    this();
    this.count = count;
    setCountIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public KeyRange(KeyRange other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    if (other.isSetStart_key()) {
      this.start_key = org.apache.thrift.TBaseHelper.copyBinary(other.start_key);
;
    }
    if (other.isSetEnd_key()) {
      this.end_key = org.apache.thrift.TBaseHelper.copyBinary(other.end_key);
;
    }
    if (other.isSetStart_token()) {
      this.start_token = other.start_token;
    }
    if (other.isSetEnd_token()) {
      this.end_token = other.end_token;
    }
    if (other.isSetRow_filter()) {
      List<IndexExpression> __this__row_filter = new ArrayList<IndexExpression>();
      for (IndexExpression other_element : other.row_filter) {
        __this__row_filter.add(new IndexExpression(other_element));
      }
      this.row_filter = __this__row_filter;
    }
    this.count = other.count;
  }

  public KeyRange deepCopy() {
    return new KeyRange(this);
  }

  @Override
  public void clear() {
    this.start_key = null;
    this.end_key = null;
    this.start_token = null;
    this.end_token = null;
    this.row_filter = null;
    this.count = 100;

  }

  public byte[] getStart_key() {
    setStart_key(org.apache.thrift.TBaseHelper.rightSize(start_key));
    return start_key == null ? null : start_key.array();
  }

  public ByteBuffer bufferForStart_key() {
    return start_key;
  }

  public KeyRange setStart_key(byte[] start_key) {
    setStart_key(start_key == null ? (ByteBuffer)null : ByteBuffer.wrap(start_key));
    return this;
  }

  public KeyRange setStart_key(ByteBuffer start_key) {
    this.start_key = start_key;
    return this;
  }

  public void unsetStart_key() {
    this.start_key = null;
  }

  /** Returns true if field start_key is set (has been assigned a value) and false otherwise */
  public boolean isSetStart_key() {
    return this.start_key != null;
  }

  public void setStart_keyIsSet(boolean value) {
    if (!value) {
      this.start_key = null;
    }
  }

  public byte[] getEnd_key() {
    setEnd_key(org.apache.thrift.TBaseHelper.rightSize(end_key));
    return end_key == null ? null : end_key.array();
  }

  public ByteBuffer bufferForEnd_key() {
    return end_key;
  }

  public KeyRange setEnd_key(byte[] end_key) {
    setEnd_key(end_key == null ? (ByteBuffer)null : ByteBuffer.wrap(end_key));
    return this;
  }

  public KeyRange setEnd_key(ByteBuffer end_key) {
    this.end_key = end_key;
    return this;
  }

  public void unsetEnd_key() {
    this.end_key = null;
  }

  /** Returns true if field end_key is set (has been assigned a value) and false otherwise */
  public boolean isSetEnd_key() {
    return this.end_key != null;
  }

  public void setEnd_keyIsSet(boolean value) {
    if (!value) {
      this.end_key = null;
    }
  }

  public String getStart_token() {
    return this.start_token;
  }

  public KeyRange setStart_token(String start_token) {
    this.start_token = start_token;
    return this;
  }

  public void unsetStart_token() {
    this.start_token = null;
  }

  /** Returns true if field start_token is set (has been assigned a value) and false otherwise */
  public boolean isSetStart_token() {
    return this.start_token != null;
  }

  public void setStart_tokenIsSet(boolean value) {
    if (!value) {
      this.start_token = null;
    }
  }

  public String getEnd_token() {
    return this.end_token;
  }

  public KeyRange setEnd_token(String end_token) {
    this.end_token = end_token;
    return this;
  }

  public void unsetEnd_token() {
    this.end_token = null;
  }

  /** Returns true if field end_token is set (has been assigned a value) and false otherwise */
  public boolean isSetEnd_token() {
    return this.end_token != null;
  }

  public void setEnd_tokenIsSet(boolean value) {
    if (!value) {
      this.end_token = null;
    }
  }

  public int getRow_filterSize() {
    return (this.row_filter == null) ? 0 : this.row_filter.size();
  }

  public java.util.Iterator<IndexExpression> getRow_filterIterator() {
    return (this.row_filter == null) ? null : this.row_filter.iterator();
  }

  public void addToRow_filter(IndexExpression elem) {
    if (this.row_filter == null) {
      this.row_filter = new ArrayList<IndexExpression>();
    }
    this.row_filter.add(elem);
  }

  public List<IndexExpression> getRow_filter() {
    return this.row_filter;
  }

  public KeyRange setRow_filter(List<IndexExpression> row_filter) {
    this.row_filter = row_filter;
    return this;
  }

  public void unsetRow_filter() {
    this.row_filter = null;
  }

  /** Returns true if field row_filter is set (has been assigned a value) and false otherwise */
  public boolean isSetRow_filter() {
    return this.row_filter != null;
  }

  public void setRow_filterIsSet(boolean value) {
    if (!value) {
      this.row_filter = null;
    }
  }

  public int getCount() {
    return this.count;
  }

  public KeyRange setCount(int count) {
    this.count = count;
    setCountIsSet(true);
    return this;
  }

  public void unsetCount() {
    __isset_bit_vector.clear(__COUNT_ISSET_ID);
  }

  /** Returns true if field count is set (has been assigned a value) and false otherwise */
  public boolean isSetCount() {
    return __isset_bit_vector.get(__COUNT_ISSET_ID);
  }

  public void setCountIsSet(boolean value) {
    __isset_bit_vector.set(__COUNT_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case START_KEY:
      if (value == null) {
        unsetStart_key();
      } else {
        setStart_key((ByteBuffer)value);
      }
      break;

    case END_KEY:
      if (value == null) {
        unsetEnd_key();
      } else {
        setEnd_key((ByteBuffer)value);
      }
      break;

    case START_TOKEN:
      if (value == null) {
        unsetStart_token();
      } else {
        setStart_token((String)value);
      }
      break;

    case END_TOKEN:
      if (value == null) {
        unsetEnd_token();
      } else {
        setEnd_token((String)value);
      }
      break;

    case ROW_FILTER:
      if (value == null) {
        unsetRow_filter();
      } else {
        setRow_filter((List<IndexExpression>)value);
      }
      break;

    case COUNT:
      if (value == null) {
        unsetCount();
      } else {
        setCount((Integer)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case START_KEY:
      return getStart_key();

    case END_KEY:
      return getEnd_key();

    case START_TOKEN:
      return getStart_token();

    case END_TOKEN:
      return getEnd_token();

    case ROW_FILTER:
      return getRow_filter();

    case COUNT:
      return Integer.valueOf(getCount());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case START_KEY:
      return isSetStart_key();
    case END_KEY:
      return isSetEnd_key();
    case START_TOKEN:
      return isSetStart_token();
    case END_TOKEN:
      return isSetEnd_token();
    case ROW_FILTER:
      return isSetRow_filter();
    case COUNT:
      return isSetCount();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof KeyRange)
      return this.equals((KeyRange)that);
    return false;
  }

  public boolean equals(KeyRange that) {
    if (that == null)
      return false;

    boolean this_present_start_key = true && this.isSetStart_key();
    boolean that_present_start_key = true && that.isSetStart_key();
    if (this_present_start_key || that_present_start_key) {
      if (!(this_present_start_key && that_present_start_key))
        return false;
      if (!this.start_key.equals(that.start_key))
        return false;
    }

    boolean this_present_end_key = true && this.isSetEnd_key();
    boolean that_present_end_key = true && that.isSetEnd_key();
    if (this_present_end_key || that_present_end_key) {
      if (!(this_present_end_key && that_present_end_key))
        return false;
      if (!this.end_key.equals(that.end_key))
        return false;
    }

    boolean this_present_start_token = true && this.isSetStart_token();
    boolean that_present_start_token = true && that.isSetStart_token();
    if (this_present_start_token || that_present_start_token) {
      if (!(this_present_start_token && that_present_start_token))
        return false;
      if (!this.start_token.equals(that.start_token))
        return false;
    }

    boolean this_present_end_token = true && this.isSetEnd_token();
    boolean that_present_end_token = true && that.isSetEnd_token();
    if (this_present_end_token || that_present_end_token) {
      if (!(this_present_end_token && that_present_end_token))
        return false;
      if (!this.end_token.equals(that.end_token))
        return false;
    }

    boolean this_present_row_filter = true && this.isSetRow_filter();
    boolean that_present_row_filter = true && that.isSetRow_filter();
    if (this_present_row_filter || that_present_row_filter) {
      if (!(this_present_row_filter && that_present_row_filter))
        return false;
      if (!this.row_filter.equals(that.row_filter))
        return false;
    }

    boolean this_present_count = true;
    boolean that_present_count = true;
    if (this_present_count || that_present_count) {
      if (!(this_present_count && that_present_count))
        return false;
      if (this.count != that.count)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();

    boolean present_start_key = true && (isSetStart_key());
    builder.append(present_start_key);
    if (present_start_key)
      builder.append(start_key);

    boolean present_end_key = true && (isSetEnd_key());
    builder.append(present_end_key);
    if (present_end_key)
      builder.append(end_key);

    boolean present_start_token = true && (isSetStart_token());
    builder.append(present_start_token);
    if (present_start_token)
      builder.append(start_token);

    boolean present_end_token = true && (isSetEnd_token());
    builder.append(present_end_token);
    if (present_end_token)
      builder.append(end_token);

    boolean present_row_filter = true && (isSetRow_filter());
    builder.append(present_row_filter);
    if (present_row_filter)
      builder.append(row_filter);

    boolean present_count = true;
    builder.append(present_count);
    if (present_count)
      builder.append(count);

    return builder.toHashCode();
  }

  public int compareTo(KeyRange other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    KeyRange typedOther = (KeyRange)other;

    lastComparison = Boolean.valueOf(isSetStart_key()).compareTo(typedOther.isSetStart_key());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStart_key()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.start_key, typedOther.start_key);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetEnd_key()).compareTo(typedOther.isSetEnd_key());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEnd_key()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.end_key, typedOther.end_key);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetStart_token()).compareTo(typedOther.isSetStart_token());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStart_token()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.start_token, typedOther.start_token);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetEnd_token()).compareTo(typedOther.isSetEnd_token());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEnd_token()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.end_token, typedOther.end_token);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetRow_filter()).compareTo(typedOther.isSetRow_filter());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRow_filter()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.row_filter, typedOther.row_filter);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCount()).compareTo(typedOther.isSetCount());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCount()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.count, typedOther.count);
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
        case 1: // START_KEY
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.start_key = iprot.readBinary();
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 2: // END_KEY
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.end_key = iprot.readBinary();
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 3: // START_TOKEN
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.start_token = iprot.readString();
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 4: // END_TOKEN
          if (field.type == org.apache.thrift.protocol.TType.STRING) {
            this.end_token = iprot.readString();
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 6: // ROW_FILTER
          if (field.type == org.apache.thrift.protocol.TType.LIST) {
            {
              org.apache.thrift.protocol.TList _list16 = iprot.readListBegin();
              this.row_filter = new ArrayList<IndexExpression>(_list16.size);
              for (int _i17 = 0; _i17 < _list16.size; ++_i17)
              {
                IndexExpression _elem18; // required
                _elem18 = new IndexExpression();
                _elem18.read(iprot);
                this.row_filter.add(_elem18);
              }
              iprot.readListEnd();
            }
          } else { 
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 5: // COUNT
          if (field.type == org.apache.thrift.protocol.TType.I32) {
            this.count = iprot.readI32();
            setCountIsSet(true);
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

    // check for required fields of primitive type, which can't be checked in the validate method
    if (!isSetCount()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'count' was not found in serialized data! Struct: " + toString());
    }
    validate();
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    validate();

    oprot.writeStructBegin(STRUCT_DESC);
    if (this.start_key != null) {
      if (isSetStart_key()) {
        oprot.writeFieldBegin(START_KEY_FIELD_DESC);
        oprot.writeBinary(this.start_key);
        oprot.writeFieldEnd();
      }
    }
    if (this.end_key != null) {
      if (isSetEnd_key()) {
        oprot.writeFieldBegin(END_KEY_FIELD_DESC);
        oprot.writeBinary(this.end_key);
        oprot.writeFieldEnd();
      }
    }
    if (this.start_token != null) {
      if (isSetStart_token()) {
        oprot.writeFieldBegin(START_TOKEN_FIELD_DESC);
        oprot.writeString(this.start_token);
        oprot.writeFieldEnd();
      }
    }
    if (this.end_token != null) {
      if (isSetEnd_token()) {
        oprot.writeFieldBegin(END_TOKEN_FIELD_DESC);
        oprot.writeString(this.end_token);
        oprot.writeFieldEnd();
      }
    }
    oprot.writeFieldBegin(COUNT_FIELD_DESC);
    oprot.writeI32(this.count);
    oprot.writeFieldEnd();
    if (this.row_filter != null) {
      if (isSetRow_filter()) {
        oprot.writeFieldBegin(ROW_FILTER_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, this.row_filter.size()));
          for (IndexExpression _iter19 : this.row_filter)
          {
            _iter19.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
    }
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("KeyRange(");
    boolean first = true;

    if (isSetStart_key()) {
      sb.append("start_key:");
      if (this.start_key == null) {
        sb.append("null");
      } else {
        org.apache.thrift.TBaseHelper.toString(this.start_key, sb);
      }
      first = false;
    }
    if (isSetEnd_key()) {
      if (!first) sb.append(", ");
      sb.append("end_key:");
      if (this.end_key == null) {
        sb.append("null");
      } else {
        org.apache.thrift.TBaseHelper.toString(this.end_key, sb);
      }
      first = false;
    }
    if (isSetStart_token()) {
      if (!first) sb.append(", ");
      sb.append("start_token:");
      if (this.start_token == null) {
        sb.append("null");
      } else {
        sb.append(this.start_token);
      }
      first = false;
    }
    if (isSetEnd_token()) {
      if (!first) sb.append(", ");
      sb.append("end_token:");
      if (this.end_token == null) {
        sb.append("null");
      } else {
        sb.append(this.end_token);
      }
      first = false;
    }
    if (isSetRow_filter()) {
      if (!first) sb.append(", ");
      sb.append("row_filter:");
      if (this.row_filter == null) {
        sb.append("null");
      } else {
        sb.append(this.row_filter);
      }
      first = false;
    }
    if (!first) sb.append(", ");
    sb.append("count:");
    sb.append(this.count);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // alas, we cannot check 'count' because it's a primitive and you chose the non-beans generator.
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

