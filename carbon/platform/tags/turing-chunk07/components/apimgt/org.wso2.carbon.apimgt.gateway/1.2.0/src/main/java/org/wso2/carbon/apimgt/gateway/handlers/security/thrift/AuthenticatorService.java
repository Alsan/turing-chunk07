/**
 * Autogenerated by Thrift Compiler (0.7.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package org.wso2.carbon.apimgt.gateway.handlers.security.thrift;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticatorService {

  public interface Iface {

    public String authenticate(String userName, String password) throws AuthenticationException, org.apache.thrift.TException;

  }

  public interface AsyncIface {

    public void authenticate(String userName, String password, org.apache.thrift.async.AsyncMethodCallback<AsyncClient.authenticate_call> resultHandler) throws org.apache.thrift.TException;

  }

  public static class Client extends org.apache.thrift.TServiceClient implements Iface {
    public static class Factory implements org.apache.thrift.TServiceClientFactory<Client> {
      public Factory() {}
      public Client getClient(org.apache.thrift.protocol.TProtocol prot) {
        return new Client(prot);
      }
      public Client getClient(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
        return new Client(iprot, oprot);
      }
    }

    public Client(org.apache.thrift.protocol.TProtocol prot)
    {
      super(prot, prot);
    }

    public Client(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
      super(iprot, oprot);
    }

    public String authenticate(String userName, String password) throws AuthenticationException, org.apache.thrift.TException
    {
      send_authenticate(userName, password);
      return recv_authenticate();
    }

    public void send_authenticate(String userName, String password) throws org.apache.thrift.TException
    {
      authenticate_args args = new authenticate_args();
      args.setUserName(userName);
      args.setPassword(password);
      sendBase("authenticate", args);
    }

    public String recv_authenticate() throws AuthenticationException, org.apache.thrift.TException
    {
      authenticate_result result = new authenticate_result();
      receiveBase(result, "authenticate");
      if (result.isSetSuccess()) {
        return result.success;
      }
      if (result.ae != null) {
        throw result.ae;
      }
      throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "authenticate failed: unknown result");
    }

  }
  public static class AsyncClient extends org.apache.thrift.async.TAsyncClient implements AsyncIface {
    public static class Factory implements org.apache.thrift.async.TAsyncClientFactory<AsyncClient> {
      private org.apache.thrift.async.TAsyncClientManager clientManager;
      private org.apache.thrift.protocol.TProtocolFactory protocolFactory;
      public Factory(org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.protocol.TProtocolFactory protocolFactory) {
        this.clientManager = clientManager;
        this.protocolFactory = protocolFactory;
      }
      public AsyncClient getAsyncClient(org.apache.thrift.transport.TNonblockingTransport transport) {
        return new AsyncClient(protocolFactory, clientManager, transport);
      }
    }

    public AsyncClient(org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.transport.TNonblockingTransport transport) {
      super(protocolFactory, clientManager, transport);
    }

    public void authenticate(String userName, String password, org.apache.thrift.async.AsyncMethodCallback<authenticate_call> resultHandler) throws org.apache.thrift.TException {
      checkReady();
      authenticate_call method_call = new authenticate_call(userName, password, resultHandler, this, ___protocolFactory, ___transport);
      this.___currentMethod = method_call;
      ___manager.call(method_call);
    }

    public static class authenticate_call extends org.apache.thrift.async.TAsyncMethodCall {
      private String userName;
      private String password;
      public authenticate_call(String userName, String password, org.apache.thrift.async.AsyncMethodCallback<authenticate_call> resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
        super(client, protocolFactory, transport, resultHandler, false);
        this.userName = userName;
        this.password = password;
      }

      public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
        prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("authenticate", org.apache.thrift.protocol.TMessageType.CALL, 0));
        authenticate_args args = new authenticate_args();
        args.setUserName(userName);
        args.setPassword(password);
        args.write(prot);
        prot.writeMessageEnd();
      }

      public String getResult() throws AuthenticationException, org.apache.thrift.TException {
        if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
          throw new IllegalStateException("Method call not finished!");
        }
        org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
        org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
        return (new Client(prot)).recv_authenticate();
      }
    }

  }

  public static class Processor<I extends Iface> extends org.apache.thrift.TBaseProcessor implements org.apache.thrift.TProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class.getName());
    public Processor(I iface) {
      super(iface, getProcessMap(new HashMap<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>>()));
    }

    protected Processor(I iface, Map<String,  org.apache.thrift.ProcessFunction<I, ? extends  org.apache.thrift.TBase>> processMap) {
      super(iface, getProcessMap(processMap));
    }

    private static <I extends Iface> Map<String,  org.apache.thrift.ProcessFunction<I, ? extends  org.apache.thrift.TBase>> getProcessMap(Map<String,  org.apache.thrift.ProcessFunction<I, ? extends  org.apache.thrift.TBase>> processMap) {
      processMap.put("authenticate", new authenticate());
      return processMap;
    }

    private static class authenticate<I extends Iface> extends org.apache.thrift.ProcessFunction<I, authenticate_args> {
      public authenticate() {
        super("authenticate");
      }

      protected authenticate_args getEmptyArgsInstance() {
        return new authenticate_args();
      }

      protected authenticate_result getResult(I iface, authenticate_args args) throws org.apache.thrift.TException {
        authenticate_result result = new authenticate_result();
        try {
          result.success = iface.authenticate(args.userName, args.password);
        } catch (AuthenticationException ae) {
          result.ae = ae;
        }
        return result;
      }
    }

  }

  public static class authenticate_args implements org.apache.thrift.TBase<authenticate_args, authenticate_args._Fields>, java.io.Serializable, Cloneable   {
    private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("authenticate_args");

    private static final org.apache.thrift.protocol.TField USER_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("userName", org.apache.thrift.protocol.TType.STRING, (short)1);
    private static final org.apache.thrift.protocol.TField PASSWORD_FIELD_DESC = new org.apache.thrift.protocol.TField("password", org.apache.thrift.protocol.TType.STRING, (short)2);

    public String userName; // required
    public String password; // required

    /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
    public enum _Fields implements org.apache.thrift.TFieldIdEnum {
      USER_NAME((short)1, "userName"),
      PASSWORD((short)2, "password");

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
          case 1: // USER_NAME
            return USER_NAME;
          case 2: // PASSWORD
            return PASSWORD;
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

    public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
    static {
      Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
      tmpMap.put(_Fields.USER_NAME, new org.apache.thrift.meta_data.FieldMetaData("userName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
          new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
      tmpMap.put(_Fields.PASSWORD, new org.apache.thrift.meta_data.FieldMetaData("password", org.apache.thrift.TFieldRequirementType.REQUIRED, 
          new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
      metaDataMap = Collections.unmodifiableMap(tmpMap);
      org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(authenticate_args.class, metaDataMap);
    }

    public authenticate_args() {
    }

    public authenticate_args(
      String userName,
      String password)
    {
      this();
      this.userName = userName;
      this.password = password;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public authenticate_args(authenticate_args other) {
      if (other.isSetUserName()) {
        this.userName = other.userName;
      }
      if (other.isSetPassword()) {
        this.password = other.password;
      }
    }

    public authenticate_args deepCopy() {
      return new authenticate_args(this);
    }

    @Override
    public void clear() {
      this.userName = null;
      this.password = null;
    }

    public String getUserName() {
      return this.userName;
    }

    public authenticate_args setUserName(String userName) {
      this.userName = userName;
      return this;
    }

    public void unsetUserName() {
      this.userName = null;
    }

    /** Returns true if field userName is set (has been assigned a value) and false otherwise */
    public boolean isSetUserName() {
      return this.userName != null;
    }

    public void setUserNameIsSet(boolean value) {
      if (!value) {
        this.userName = null;
      }
    }

    public String getPassword() {
      return this.password;
    }

    public authenticate_args setPassword(String password) {
      this.password = password;
      return this;
    }

    public void unsetPassword() {
      this.password = null;
    }

    /** Returns true if field password is set (has been assigned a value) and false otherwise */
    public boolean isSetPassword() {
      return this.password != null;
    }

    public void setPasswordIsSet(boolean value) {
      if (!value) {
        this.password = null;
      }
    }

    public void setFieldValue(_Fields field, Object value) {
      switch (field) {
      case USER_NAME:
        if (value == null) {
          unsetUserName();
        } else {
          setUserName((String)value);
        }
        break;

      case PASSWORD:
        if (value == null) {
          unsetPassword();
        } else {
          setPassword((String)value);
        }
        break;

      }
    }

    public Object getFieldValue(_Fields field) {
      switch (field) {
      case USER_NAME:
        return getUserName();

      case PASSWORD:
        return getPassword();

      }
      throw new IllegalStateException();
    }

    /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
    public boolean isSet(_Fields field) {
      if (field == null) {
        throw new IllegalArgumentException();
      }

      switch (field) {
      case USER_NAME:
        return isSetUserName();
      case PASSWORD:
        return isSetPassword();
      }
      throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof authenticate_args)
        return this.equals((authenticate_args)that);
      return false;
    }

    public boolean equals(authenticate_args that) {
      if (that == null)
        return false;

      boolean this_present_userName = true && this.isSetUserName();
      boolean that_present_userName = true && that.isSetUserName();
      if (this_present_userName || that_present_userName) {
        if (!(this_present_userName && that_present_userName))
          return false;
        if (!this.userName.equals(that.userName))
          return false;
      }

      boolean this_present_password = true && this.isSetPassword();
      boolean that_present_password = true && that.isSetPassword();
      if (this_present_password || that_present_password) {
        if (!(this_present_password && that_present_password))
          return false;
        if (!this.password.equals(that.password))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public int compareTo(authenticate_args other) {
      if (!getClass().equals(other.getClass())) {
        return getClass().getName().compareTo(other.getClass().getName());
      }

      int lastComparison = 0;
      authenticate_args typedOther = (authenticate_args)other;

      lastComparison = Boolean.valueOf(isSetUserName()).compareTo(typedOther.isSetUserName());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetUserName()) {
        lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.userName, typedOther.userName);
        if (lastComparison != 0) {
          return lastComparison;
        }
      }
      lastComparison = Boolean.valueOf(isSetPassword()).compareTo(typedOther.isSetPassword());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetPassword()) {
        lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.password, typedOther.password);
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
          case 1: // USER_NAME
            if (field.type == org.apache.thrift.protocol.TType.STRING) {
              this.userName = iprot.readString();
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case 2: // PASSWORD
            if (field.type == org.apache.thrift.protocol.TType.STRING) {
              this.password = iprot.readString();
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
      validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.userName != null) {
        oprot.writeFieldBegin(USER_NAME_FIELD_DESC);
        oprot.writeString(this.userName);
        oprot.writeFieldEnd();
      }
      if (this.password != null) {
        oprot.writeFieldBegin(PASSWORD_FIELD_DESC);
        oprot.writeString(this.password);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("authenticate_args(");
      boolean first = true;

      sb.append("userName:");
      if (this.userName == null) {
        sb.append("null");
      } else {
        sb.append(this.userName);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("password:");
      if (this.password == null) {
        sb.append("null");
      } else {
        sb.append(this.password);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws org.apache.thrift.TException {
      // check for required fields
      if (userName == null) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'userName' was not present! Struct: " + toString());
      }
      if (password == null) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'password' was not present! Struct: " + toString());
      }
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
        read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
      } catch (org.apache.thrift.TException te) {
        throw new java.io.IOException(te);
      }
    }

  }

  public static class authenticate_result implements org.apache.thrift.TBase<authenticate_result, authenticate_result._Fields>, java.io.Serializable, Cloneable   {
    private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("authenticate_result");

    private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.STRING, (short)0);
    private static final org.apache.thrift.protocol.TField AE_FIELD_DESC = new org.apache.thrift.protocol.TField("ae", org.apache.thrift.protocol.TType.STRUCT, (short)1);

    public String success; // required
    public AuthenticationException ae; // required

    /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
    public enum _Fields implements org.apache.thrift.TFieldIdEnum {
      SUCCESS((short)0, "success"),
      AE((short)1, "ae");

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
          case 0: // SUCCESS
            return SUCCESS;
          case 1: // AE
            return AE;
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

    public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
    static {
      Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
      tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, 
          new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
      tmpMap.put(_Fields.AE, new org.apache.thrift.meta_data.FieldMetaData("ae", org.apache.thrift.TFieldRequirementType.DEFAULT, 
          new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRUCT)));
      metaDataMap = Collections.unmodifiableMap(tmpMap);
      org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(authenticate_result.class, metaDataMap);
    }

    public authenticate_result() {
    }

    public authenticate_result(
      String success,
      AuthenticationException ae)
    {
      this();
      this.success = success;
      this.ae = ae;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public authenticate_result(authenticate_result other) {
      if (other.isSetSuccess()) {
        this.success = other.success;
      }
      if (other.isSetAe()) {
        this.ae = new AuthenticationException(other.ae);
      }
    }

    public authenticate_result deepCopy() {
      return new authenticate_result(this);
    }

    @Override
    public void clear() {
      this.success = null;
      this.ae = null;
    }

    public String getSuccess() {
      return this.success;
    }

    public authenticate_result setSuccess(String success) {
      this.success = success;
      return this;
    }

    public void unsetSuccess() {
      this.success = null;
    }

    /** Returns true if field success is set (has been assigned a value) and false otherwise */
    public boolean isSetSuccess() {
      return this.success != null;
    }

    public void setSuccessIsSet(boolean value) {
      if (!value) {
        this.success = null;
      }
    }

    public AuthenticationException getAe() {
      return this.ae;
    }

    public authenticate_result setAe(AuthenticationException ae) {
      this.ae = ae;
      return this;
    }

    public void unsetAe() {
      this.ae = null;
    }

    /** Returns true if field ae is set (has been assigned a value) and false otherwise */
    public boolean isSetAe() {
      return this.ae != null;
    }

    public void setAeIsSet(boolean value) {
      if (!value) {
        this.ae = null;
      }
    }

    public void setFieldValue(_Fields field, Object value) {
      switch (field) {
      case SUCCESS:
        if (value == null) {
          unsetSuccess();
        } else {
          setSuccess((String)value);
        }
        break;

      case AE:
        if (value == null) {
          unsetAe();
        } else {
          setAe((AuthenticationException)value);
        }
        break;

      }
    }

    public Object getFieldValue(_Fields field) {
      switch (field) {
      case SUCCESS:
        return getSuccess();

      case AE:
        return getAe();

      }
      throw new IllegalStateException();
    }

    /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
    public boolean isSet(_Fields field) {
      if (field == null) {
        throw new IllegalArgumentException();
      }

      switch (field) {
      case SUCCESS:
        return isSetSuccess();
      case AE:
        return isSetAe();
      }
      throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof authenticate_result)
        return this.equals((authenticate_result)that);
      return false;
    }

    public boolean equals(authenticate_result that) {
      if (that == null)
        return false;

      boolean this_present_success = true && this.isSetSuccess();
      boolean that_present_success = true && that.isSetSuccess();
      if (this_present_success || that_present_success) {
        if (!(this_present_success && that_present_success))
          return false;
        if (!this.success.equals(that.success))
          return false;
      }

      boolean this_present_ae = true && this.isSetAe();
      boolean that_present_ae = true && that.isSetAe();
      if (this_present_ae || that_present_ae) {
        if (!(this_present_ae && that_present_ae))
          return false;
        if (!this.ae.equals(that.ae))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public int compareTo(authenticate_result other) {
      if (!getClass().equals(other.getClass())) {
        return getClass().getName().compareTo(other.getClass().getName());
      }

      int lastComparison = 0;
      authenticate_result typedOther = (authenticate_result)other;

      lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(typedOther.isSetSuccess());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetSuccess()) {
        lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, typedOther.success);
        if (lastComparison != 0) {
          return lastComparison;
        }
      }
      lastComparison = Boolean.valueOf(isSetAe()).compareTo(typedOther.isSetAe());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetAe()) {
        lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.ae, typedOther.ae);
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
          case 0: // SUCCESS
            if (field.type == org.apache.thrift.protocol.TType.STRING) {
              this.success = iprot.readString();
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
            }
            break;
          case 1: // AE
            if (field.type == org.apache.thrift.protocol.TType.STRUCT) {
              this.ae = new AuthenticationException();
              this.ae.read(iprot);
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
      validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
      oprot.writeStructBegin(STRUCT_DESC);

      if (this.isSetSuccess()) {
        oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
        oprot.writeString(this.success);
        oprot.writeFieldEnd();
      } else if (this.isSetAe()) {
        oprot.writeFieldBegin(AE_FIELD_DESC);
        this.ae.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("authenticate_result(");
      boolean first = true;

      sb.append("success:");
      if (this.success == null) {
        sb.append("null");
      } else {
        sb.append(this.success);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("ae:");
      if (this.ae == null) {
        sb.append("null");
      } else {
        sb.append(this.ae);
      }
      first = false;
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
        read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
      } catch (org.apache.thrift.TException te) {
        throw new java.io.IOException(te);
      }
    }

  }

}
