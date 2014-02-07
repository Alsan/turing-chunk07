/**
 * Autogenerated by Thrift Compiler (0.7.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package org.wso2.carbon.identity.entitlement.pep.agent.generatedCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class EntitlementThriftClient {

    public interface Iface {

        public String getDecision(String request, String sessionId) throws EntitlementException, org.apache.thrift.TException;

    }

    public interface AsyncIface {

        public void getDecision(String request, String sessionId, org.apache.thrift.async.AsyncMethodCallback<AsyncClient.getDecision_call> resultHandler) throws org.apache.thrift.TException;

    }

    public static class Client extends org.apache.thrift.TServiceClient implements Iface {
        public static class Factory implements org.apache.thrift.TServiceClientFactory<Client> {
            public Factory() {
            }

            public Client getClient(org.apache.thrift.protocol.TProtocol prot) {
                return new Client(prot);
            }

            public Client getClient(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
                return new Client(iprot, oprot);
            }
        }

        public Client(org.apache.thrift.protocol.TProtocol prot) {
            super(prot, prot);
        }

        public Client(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
            super(iprot, oprot);
        }

        public String getDecision(String request, String sessionId) throws EntitlementException, org.apache.thrift.TException {
            send_getDecision(request, sessionId);
            return recv_getDecision();
        }

        public void send_getDecision(String request, String sessionId) throws org.apache.thrift.TException {
            getDecision_args args = new getDecision_args();
            args.setRequest(request);
            args.setSessionId(sessionId);
            sendBase("getDecision", args);
        }

        public String recv_getDecision() throws EntitlementException, org.apache.thrift.TException {
            getDecision_result result = new getDecision_result();
            receiveBase(result, "getDecision");
            if (result.isSetSuccess()) {
                return result.success;
            }
            if (result.e != null) {
                throw result.e;
            }
            throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "getDecision failed: unknown result");
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

        public void getDecision(String request, String sessionId, org.apache.thrift.async.AsyncMethodCallback<getDecision_call> resultHandler) throws org.apache.thrift.TException {
            checkReady();
            getDecision_call method_call = new getDecision_call(request, sessionId, resultHandler, this, ___protocolFactory, ___transport);
            this.___currentMethod = method_call;
            ___manager.call(method_call);
        }

        public static class getDecision_call extends org.apache.thrift.async.TAsyncMethodCall {
            private String request;
            private String sessionId;

            public getDecision_call(String request, String sessionId, org.apache.thrift.async.AsyncMethodCallback<getDecision_call> resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
                super(client, protocolFactory, transport, resultHandler, false);
                this.request = request;
                this.sessionId = sessionId;
            }

            public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
                prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("getDecision", org.apache.thrift.protocol.TMessageType.CALL, 0));
                getDecision_args args = new getDecision_args();
                args.setRequest(request);
                args.setSessionId(sessionId);
                args.write(prot);
                prot.writeMessageEnd();
            }

            public String getResult() throws EntitlementException, org.apache.thrift.TException {
                if (getState() != State.RESPONSE_READ) {
                    throw new IllegalStateException("Method call not finished!");
                }
                org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
                org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
                return (new Client(prot)).recv_getDecision();
            }
        }

    }

    public static class Processor<I extends Iface> extends org.apache.thrift.TBaseProcessor implements org.apache.thrift.TProcessor {
        private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class.getName());

        public Processor(I iface) {
            super(iface, getProcessMap(new HashMap<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>>()));
        }

        protected Processor(I iface, Map<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> processMap) {
            super(iface, getProcessMap(processMap));
        }

        private static <I extends Iface> Map<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> getProcessMap(Map<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> processMap) {
            processMap.put("getDecision", new getDecision());
            return processMap;
        }

        private static class getDecision<I extends Iface> extends org.apache.thrift.ProcessFunction<I, getDecision_args> {
            public getDecision() {
                super("getDecision");
            }

            protected getDecision_args getEmptyArgsInstance() {
                return new getDecision_args();
            }

            protected getDecision_result getResult(I iface, getDecision_args args) throws org.apache.thrift.TException {
                getDecision_result result = new getDecision_result();
                try {
                    result.success = iface.getDecision(args.request, args.sessionId);
                } catch (EntitlementException e) {
                    result.e = e;
                }
                return result;
            }
        }

    }

    public static class getDecision_args implements org.apache.thrift.TBase<getDecision_args, getDecision_args._Fields>, java.io.Serializable, Cloneable {
        private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("getDecision_args");

        private static final org.apache.thrift.protocol.TField REQUEST_FIELD_DESC = new org.apache.thrift.protocol.TField("request", org.apache.thrift.protocol.TType.STRING, (short) 1);
        private static final org.apache.thrift.protocol.TField SESSION_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("sessionId", org.apache.thrift.protocol.TType.STRING, (short) 2);

        public String request; // required
        public String sessionId; // required

        /**
         * The set of fields this struct contains, along with convenience methods for finding and manipulating them.
         */
        public enum _Fields implements org.apache.thrift.TFieldIdEnum {
            REQUEST((short) 1, "request"),
            SESSION_ID((short) 2, "sessionId");

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
                switch (fieldId) {
                    case 1: // REQUEST
                        return REQUEST;
                    case 2: // SESSION_ID
                        return SESSION_ID;
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
            tmpMap.put(_Fields.REQUEST, new org.apache.thrift.meta_data.FieldMetaData("request", org.apache.thrift.TFieldRequirementType.REQUIRED,
                    new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
            tmpMap.put(_Fields.SESSION_ID, new org.apache.thrift.meta_data.FieldMetaData("sessionId", org.apache.thrift.TFieldRequirementType.REQUIRED,
                    new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
            metaDataMap = Collections.unmodifiableMap(tmpMap);
            org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(getDecision_args.class, metaDataMap);
        }

        public getDecision_args() {
        }

        public getDecision_args(
                String request,
                String sessionId) {
            this();
            this.request = request;
            this.sessionId = sessionId;
        }

        /**
         * Performs a deep copy on <i>other</i>.
         */
        public getDecision_args(getDecision_args other) {
            if (other.isSetRequest()) {
                this.request = other.request;
            }
            if (other.isSetSessionId()) {
                this.sessionId = other.sessionId;
            }
        }

        public getDecision_args deepCopy() {
            return new getDecision_args(this);
        }


        public void clear() {
            this.request = null;
            this.sessionId = null;
        }

        public String getRequest() {
            return this.request;
        }

        public getDecision_args setRequest(String request) {
            this.request = request;
            return this;
        }

        public void unsetRequest() {
            this.request = null;
        }

        /**
         * Returns true if field request is set (has been assigned a value) and false otherwise
         */
        public boolean isSetRequest() {
            return this.request != null;
        }

        public void setRequestIsSet(boolean value) {
            if (!value) {
                this.request = null;
            }
        }

        public String getSessionId() {
            return this.sessionId;
        }

        public getDecision_args setSessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public void unsetSessionId() {
            this.sessionId = null;
        }

        /**
         * Returns true if field sessionId is set (has been assigned a value) and false otherwise
         */
        public boolean isSetSessionId() {
            return this.sessionId != null;
        }

        public void setSessionIdIsSet(boolean value) {
            if (!value) {
                this.sessionId = null;
            }
        }

        public void setFieldValue(_Fields field, Object value) {
            switch (field) {
                case REQUEST:
                    if (value == null) {
                        unsetRequest();
                    } else {
                        setRequest((String) value);
                    }
                    break;

                case SESSION_ID:
                    if (value == null) {
                        unsetSessionId();
                    } else {
                        setSessionId((String) value);
                    }
                    break;

            }
        }

        public Object getFieldValue(_Fields field) {
            switch (field) {
                case REQUEST:
                    return getRequest();

                case SESSION_ID:
                    return getSessionId();

            }
            throw new IllegalStateException();
        }

        /**
         * Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise
         */
        public boolean isSet(_Fields field) {
            if (field == null) {
                throw new IllegalArgumentException();
            }

            switch (field) {
                case REQUEST:
                    return isSetRequest();
                case SESSION_ID:
                    return isSetSessionId();
            }
            throw new IllegalStateException();
        }

        @Override
        public boolean equals(Object that) {
            if (that == null)
                return false;
            if (that instanceof getDecision_args)
                return this.equals((getDecision_args) that);
            return false;
        }

        public boolean equals(getDecision_args that) {
            if (that == null)
                return false;

            boolean this_present_request = true && this.isSetRequest();
            boolean that_present_request = true && that.isSetRequest();
            if (this_present_request || that_present_request) {
                if (!(this_present_request && that_present_request))
                    return false;
                if (!this.request.equals(that.request))
                    return false;
            }

            boolean this_present_sessionId = true && this.isSetSessionId();
            boolean that_present_sessionId = true && that.isSetSessionId();
            if (this_present_sessionId || that_present_sessionId) {
                if (!(this_present_sessionId && that_present_sessionId))
                    return false;
                if (!this.sessionId.equals(that.sessionId))
                    return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        public int compareTo(getDecision_args other) {
            if (!getClass().equals(other.getClass())) {
                return getClass().getName().compareTo(other.getClass().getName());
            }

            int lastComparison = 0;
            getDecision_args typedOther = (getDecision_args) other;

            lastComparison = Boolean.valueOf(isSetRequest()).compareTo(typedOther.isSetRequest());
            if (lastComparison != 0) {
                return lastComparison;
            }
            if (isSetRequest()) {
                lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.request, typedOther.request);
                if (lastComparison != 0) {
                    return lastComparison;
                }
            }
            lastComparison = Boolean.valueOf(isSetSessionId()).compareTo(typedOther.isSetSessionId());
            if (lastComparison != 0) {
                return lastComparison;
            }
            if (isSetSessionId()) {
                lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.sessionId, typedOther.sessionId);
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
            while (true) {
                field = iprot.readFieldBegin();
                if (field.type == org.apache.thrift.protocol.TType.STOP) {
                    break;
                }
                switch (field.id) {
                    case 1: // REQUEST
                        if (field.type == org.apache.thrift.protocol.TType.STRING) {
                            this.request = iprot.readString();
                        } else {
                            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
                        }
                        break;
                    case 2: // SESSION_ID
                        if (field.type == org.apache.thrift.protocol.TType.STRING) {
                            this.sessionId = iprot.readString();
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
            if (this.request != null) {
                oprot.writeFieldBegin(REQUEST_FIELD_DESC);
                oprot.writeString(this.request);
                oprot.writeFieldEnd();
            }
            if (this.sessionId != null) {
                oprot.writeFieldBegin(SESSION_ID_FIELD_DESC);
                oprot.writeString(this.sessionId);
                oprot.writeFieldEnd();
            }
            oprot.writeFieldStop();
            oprot.writeStructEnd();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("getDecision_args(");
            boolean first = true;

            sb.append("request:");
            if (this.request == null) {
                sb.append("null");
            } else {
                sb.append(this.request);
            }
            first = false;
            if (!first) sb.append(", ");
            sb.append("sessionId:");
            if (this.sessionId == null) {
                sb.append("null");
            } else {
                sb.append(this.sessionId);
            }
            first = false;
            sb.append(")");
            return sb.toString();
        }

        public void validate() throws org.apache.thrift.TException {
            // check for required fields
            if (request == null) {
                throw new org.apache.thrift.protocol.TProtocolException("Required field 'request' was not present! Struct: " + toString());
            }
            if (sessionId == null) {
                throw new org.apache.thrift.protocol.TProtocolException("Required field 'sessionId' was not present! Struct: " + toString());
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

    public static class getDecision_result implements org.apache.thrift.TBase<getDecision_result, getDecision_result._Fields>, java.io.Serializable, Cloneable {
        private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("getDecision_result");

        private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.STRING, (short) 0);
        private static final org.apache.thrift.protocol.TField E_FIELD_DESC = new org.apache.thrift.protocol.TField("e", org.apache.thrift.protocol.TType.STRUCT, (short) 1);

        public String success; // required
        public EntitlementException e; // required

        /**
         * The set of fields this struct contains, along with convenience methods for finding and manipulating them.
         */
        public enum _Fields implements org.apache.thrift.TFieldIdEnum {
            SUCCESS((short) 0, "success"),
            E((short) 1, "e");

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
                switch (fieldId) {
                    case 0: // SUCCESS
                        return SUCCESS;
                    case 1: // E
                        return E;
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
            tmpMap.put(_Fields.E, new org.apache.thrift.meta_data.FieldMetaData("e", org.apache.thrift.TFieldRequirementType.DEFAULT,
                    new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRUCT)));
            metaDataMap = Collections.unmodifiableMap(tmpMap);
            org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(getDecision_result.class, metaDataMap);
        }

        public getDecision_result() {
        }

        public getDecision_result(
                String success,
                EntitlementException e) {
            this();
            this.success = success;
            this.e = e;
        }

        /**
         * Performs a deep copy on <i>other</i>.
         */
        public getDecision_result(getDecision_result other) {
            if (other.isSetSuccess()) {
                this.success = other.success;
            }
            if (other.isSetE()) {
                this.e = new EntitlementException(other.e);
            }
        }

        public getDecision_result deepCopy() {
            return new getDecision_result(this);
        }


        public void clear() {
            this.success = null;
            this.e = null;
        }

        public String getSuccess() {
            return this.success;
        }

        public getDecision_result setSuccess(String success) {
            this.success = success;
            return this;
        }

        public void unsetSuccess() {
            this.success = null;
        }

        /**
         * Returns true if field success is set (has been assigned a value) and false otherwise
         */
        public boolean isSetSuccess() {
            return this.success != null;
        }

        public void setSuccessIsSet(boolean value) {
            if (!value) {
                this.success = null;
            }
        }

        public EntitlementException getE() {
            return this.e;
        }

        public getDecision_result setE(EntitlementException e) {
            this.e = e;
            return this;
        }

        public void unsetE() {
            this.e = null;
        }

        /**
         * Returns true if field e is set (has been assigned a value) and false otherwise
         */
        public boolean isSetE() {
            return this.e != null;
        }

        public void setEIsSet(boolean value) {
            if (!value) {
                this.e = null;
            }
        }

        public void setFieldValue(_Fields field, Object value) {
            switch (field) {
                case SUCCESS:
                    if (value == null) {
                        unsetSuccess();
                    } else {
                        setSuccess((String) value);
                    }
                    break;

                case E:
                    if (value == null) {
                        unsetE();
                    } else {
                        setE((EntitlementException) value);
                    }
                    break;

            }
        }

        public Object getFieldValue(_Fields field) {
            switch (field) {
                case SUCCESS:
                    return getSuccess();

                case E:
                    return getE();

            }
            throw new IllegalStateException();
        }

        /**
         * Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise
         */
        public boolean isSet(_Fields field) {
            if (field == null) {
                throw new IllegalArgumentException();
            }

            switch (field) {
                case SUCCESS:
                    return isSetSuccess();
                case E:
                    return isSetE();
            }
            throw new IllegalStateException();
        }

        @Override
        public boolean equals(Object that) {
            if (that == null)
                return false;
            if (that instanceof getDecision_result)
                return this.equals((getDecision_result) that);
            return false;
        }

        public boolean equals(getDecision_result that) {
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

            boolean this_present_e = true && this.isSetE();
            boolean that_present_e = true && that.isSetE();
            if (this_present_e || that_present_e) {
                if (!(this_present_e && that_present_e))
                    return false;
                if (!this.e.equals(that.e))
                    return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        public int compareTo(getDecision_result other) {
            if (!getClass().equals(other.getClass())) {
                return getClass().getName().compareTo(other.getClass().getName());
            }

            int lastComparison = 0;
            getDecision_result typedOther = (getDecision_result) other;

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
            lastComparison = Boolean.valueOf(isSetE()).compareTo(typedOther.isSetE());
            if (lastComparison != 0) {
                return lastComparison;
            }
            if (isSetE()) {
                lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.e, typedOther.e);
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
            while (true) {
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
                    case 1: // E
                        if (field.type == org.apache.thrift.protocol.TType.STRUCT) {
                            this.e = new EntitlementException();
                            this.e.read(iprot);
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
            } else if (this.isSetE()) {
                oprot.writeFieldBegin(E_FIELD_DESC);
                this.e.write(oprot);
                oprot.writeFieldEnd();
            }
            oprot.writeFieldStop();
            oprot.writeStructEnd();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("getDecision_result(");
            boolean first = true;

            sb.append("success:");
            if (this.success == null) {
                sb.append("null");
            } else {
                sb.append(this.success);
            }
            first = false;
            if (!first) sb.append(", ");
            sb.append("e:");
            if (this.e == null) {
                sb.append("null");
            } else {
                sb.append(this.e);
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
