/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.wso2.carbon.appmgt.keymgt.service.thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
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

public class URITemplate implements org.apache.thrift.TBase<URITemplate, URITemplate._Fields>, java.io.Serializable, Cloneable {
    private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("URITemplate");

    private static final org.apache.thrift.protocol.TField URI_TEMPLATE_FIELD_DESC = new org.apache.thrift.protocol.TField("uriTemplate", org.apache.thrift.protocol.TType.STRING, (short)1);
    private static final org.apache.thrift.protocol.TField RESOURCE_URI_FIELD_DESC = new org.apache.thrift.protocol.TField("resourceURI", org.apache.thrift.protocol.TType.STRING, (short)2);
    private static final org.apache.thrift.protocol.TField RESOURCE_SANDBOX_URI_FIELD_DESC = new org.apache.thrift.protocol.TField("resourceSandboxURI", org.apache.thrift.protocol.TType.STRING, (short)3);
    private static final org.apache.thrift.protocol.TField HTTP_VERB_FIELD_DESC = new org.apache.thrift.protocol.TField("httpVerb", org.apache.thrift.protocol.TType.STRING, (short)4);
    private static final org.apache.thrift.protocol.TField AUTH_TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("authType", org.apache.thrift.protocol.TType.STRING, (short)5);
    private static final org.apache.thrift.protocol.TField THROTTLING_TIER_FIELD_DESC = new org.apache.thrift.protocol.TField("throttlingTier", org.apache.thrift.protocol.TType.STRING, (short)6);

    private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
    static {
        schemes.put(StandardScheme.class, new URITemplateStandardSchemeFactory());
        schemes.put(TupleScheme.class, new URITemplateTupleSchemeFactory());
    }

    public String uriTemplate; // optional
    public String resourceURI; // optional
    public String resourceSandboxURI; // optional
    public String httpVerb; // optional
    public String authType; // optional
    public String throttlingTier; // optional

    public String getThrottlingTier() {
        return throttlingTier;
    }

    public void setThrottlingTier(String throttlingTier) {
        this.throttlingTier = throttlingTier;
    }


    /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
    public enum _Fields implements org.apache.thrift.TFieldIdEnum {
        URI_TEMPLATE((short)1, "uriTemplate"),
        RESOURCE_URI((short)2, "resourceURI"),
        RESOURCE_SANDBOX_URI((short)3, "resourceSandboxURI"),
        HTTP_VERB((short)4, "httpVerb"),
        AUTH_TYPE((short)5, "authType"),
        THROTTLING_TIER((short)6, "throttlingTier");

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
                case 1: // URI_TEMPLATE
                    return URI_TEMPLATE;
                case 2: // RESOURCE_URI
                    return RESOURCE_URI;
                case 3: // RESOURCE_SANDBOX_URI
                    return RESOURCE_SANDBOX_URI;
                case 4: // HTTP_VERB
                    return HTTP_VERB;
                case 5: // AUTH_TYPE
                    return AUTH_TYPE;
                case 6: // THROTTLING_TIER
                    return THROTTLING_TIER;
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
    private _Fields optionals[] = {_Fields.URI_TEMPLATE,_Fields.RESOURCE_URI,_Fields.RESOURCE_SANDBOX_URI,_Fields.HTTP_VERB,_Fields.AUTH_TYPE,_Fields.THROTTLING_TIER};
    public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
    static {
        Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
        tmpMap.put(_Fields.URI_TEMPLATE, new org.apache.thrift.meta_data.FieldMetaData("uriTemplate", org.apache.thrift.TFieldRequirementType.OPTIONAL,
                                                                                       new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
        tmpMap.put(_Fields.RESOURCE_URI, new org.apache.thrift.meta_data.FieldMetaData("resourceURI", org.apache.thrift.TFieldRequirementType.OPTIONAL,
                                                                                       new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
        tmpMap.put(_Fields.RESOURCE_SANDBOX_URI, new org.apache.thrift.meta_data.FieldMetaData("resourceSandboxURI", org.apache.thrift.TFieldRequirementType.OPTIONAL,
                                                                                               new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
        tmpMap.put(_Fields.HTTP_VERB, new org.apache.thrift.meta_data.FieldMetaData("httpVerb", org.apache.thrift.TFieldRequirementType.OPTIONAL,
                                                                                    new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
        tmpMap.put(_Fields.AUTH_TYPE, new org.apache.thrift.meta_data.FieldMetaData("authType", org.apache.thrift.TFieldRequirementType.OPTIONAL,
                                                                                    new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
        tmpMap.put(_Fields.THROTTLING_TIER, new org.apache.thrift.meta_data.FieldMetaData("throttlingTier", org.apache.thrift.TFieldRequirementType.OPTIONAL,
                                new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
        metaDataMap = Collections.unmodifiableMap(tmpMap);
        org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(URITemplate.class, metaDataMap);
    }

    public URITemplate() {
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public URITemplate(URITemplate other) {
        if (other.isSetUriTemplate()) {
            this.uriTemplate = other.uriTemplate;
        }
        if (other.isSetResourceURI()) {
            this.resourceURI = other.resourceURI;
        }
        if (other.isSetResourceSandboxURI()) {
            this.resourceSandboxURI = other.resourceSandboxURI;
        }
        if (other.isSetHttpVerb()) {
            this.httpVerb = other.httpVerb;
        }
        if (other.isSetAuthType()) {
            this.authType = other.authType;
        }
        if (other.isSetThrottlingTier()) {
            this.throttlingTier = other.throttlingTier;
        }
    }

    public URITemplate deepCopy() {
        return new URITemplate(this);
    }

    @Override
    public void clear() {
        this.uriTemplate = null;
        this.resourceURI = null;
        this.resourceSandboxURI = null;
        this.httpVerb = null;
        this.authType = null;
        this.throttlingTier = null;
    }

    public String getUriTemplate() {
        return this.uriTemplate;
    }

    public URITemplate setUriTemplate(String uriTemplate) {
        this.uriTemplate = uriTemplate;
        return this;
    }

    public void unsetUriTemplate() {
        this.uriTemplate = null;
    }

    /** Returns true if field uriTemplate is set (has been assigned a value) and false otherwise */
    public boolean isSetUriTemplate() {
        return this.uriTemplate != null;
    }

    public void setUriTemplateIsSet(boolean value) {
        if (!value) {
            this.uriTemplate = null;
        }
    }

    public String getResourceURI() {
        return this.resourceURI;
    }

    public URITemplate setResourceURI(String resourceURI) {
        this.resourceURI = resourceURI;
        return this;
    }

    public void unsetResourceURI() {
        this.resourceURI = null;
    }

    /** Returns true if field resourceURI is set (has been assigned a value) and false otherwise */
    public boolean isSetResourceURI() {
        return this.resourceURI != null;
    }

    public void setResourceURIIsSet(boolean value) {
        if (!value) {
            this.resourceURI = null;
        }
    }

    public String getResourceSandboxURI() {
        return this.resourceSandboxURI;
    }

    public URITemplate setResourceSandboxURI(String resourceSandboxURI) {
        this.resourceSandboxURI = resourceSandboxURI;
        return this;
    }

    public void unsetResourceSandboxURI() {
        this.resourceSandboxURI = null;
    }

    /** Returns true if field resourceSandboxURI is set (has been assigned a value) and false otherwise */
    public boolean isSetResourceSandboxURI() {
        return this.resourceSandboxURI != null;
    }

    public void setResourceSandboxURIIsSet(boolean value) {
        if (!value) {
            this.resourceSandboxURI = null;
        }
    }

    public String getHttpVerb() {
        return this.httpVerb;
    }

    public URITemplate setHttpVerb(String httpVerb) {
        this.httpVerb = httpVerb;
        return this;
    }

    public void unsetHttpVerb() {
        this.httpVerb = null;
    }

    /** Returns true if field httpVerb is set (has been assigned a value) and false otherwise */
    public boolean isSetHttpVerb() {
        return this.httpVerb != null;
    }

    public void setHttpVerbIsSet(boolean value) {
        if (!value) {
            this.httpVerb = null;
        }
    }

    public String getAuthType() {
        return this.authType;
    }

    public URITemplate setAuthType(String authType) {
        this.authType = authType;
        return this;
    }

    public void unsetAuthType() {
        this.authType = null;
    }

    /** Returns true if field authType is set (has been assigned a value) and false otherwise */
    public boolean isSetAuthType() {
        return this.authType != null;
    }

    public void setAuthTypeIsSet(boolean value) {
        if (!value) {
            this.authType = null;
        }
    }

    public void unsetThrottlingTier() {
        this.throttlingTier = null;
    }

    /**
     * Returns true if field throttlingTier is set (has been assigned a value) and false otherwise
     */
    public boolean isSetThrottlingTier() {
        return this.throttlingTier != null;
    }

    public void setThrottlingTierIsSet(boolean value) {
        if (!value) {
            this.throttlingTier = null;
        }
    }
    public void setFieldValue(_Fields field, Object value) {
        switch (field) {
            case URI_TEMPLATE:
                if (value == null) {
                    unsetUriTemplate();
                } else {
                    setUriTemplate((String)value);
                }
                break;

            case RESOURCE_URI:
                if (value == null) {
                    unsetResourceURI();
                } else {
                    setResourceURI((String)value);
                }
                break;

            case RESOURCE_SANDBOX_URI:
                if (value == null) {
                    unsetResourceSandboxURI();
                } else {
                    setResourceSandboxURI((String)value);
                }
                break;

            case HTTP_VERB:
                if (value == null) {
                    unsetHttpVerb();
                } else {
                    setHttpVerb((String)value);
                }
                break;

            case AUTH_TYPE:
                if (value == null) {
                    unsetAuthType();
                } else {
                    setAuthType((String)value);
                }
                break;
            case THROTTLING_TIER:
                                if (value == null) {
                                    unsetThrottlingTier();
                                } else {
                                    setThrottlingTier((String)value);
                                }
                                break;

        }
    }

    public Object getFieldValue(_Fields field) {
        switch (field) {
            case URI_TEMPLATE:
                return getUriTemplate();

            case RESOURCE_URI:
                return getResourceURI();

            case RESOURCE_SANDBOX_URI:
                return getResourceSandboxURI();

            case HTTP_VERB:
                return getHttpVerb();

            case AUTH_TYPE:
                return getAuthType();
            case THROTTLING_TIER:
                              return getThrottlingTier();

        }
        throw new IllegalStateException();
    }

    /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
    public boolean isSet(_Fields field) {
        if (field == null) {
            throw new IllegalArgumentException();
        }

        switch (field) {
            case URI_TEMPLATE:
                return isSetUriTemplate();
            case RESOURCE_URI:
                return isSetResourceURI();
            case RESOURCE_SANDBOX_URI:
                return isSetResourceSandboxURI();
            case HTTP_VERB:
                return isSetHttpVerb();
            case AUTH_TYPE:
                return isSetAuthType();
            case THROTTLING_TIER:
                               return isSetThrottlingTier();
        }
        throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object that) {
        if (that == null)
            return false;
        if (that instanceof URITemplate)
            return this.equals((URITemplate)that);
        return false;
    }

    public boolean equals(URITemplate that) {
        if (that == null)
            return false;

        boolean this_present_uriTemplate = true && this.isSetUriTemplate();
        boolean that_present_uriTemplate = true && that.isSetUriTemplate();
        if (this_present_uriTemplate || that_present_uriTemplate) {
            if (!(this_present_uriTemplate && that_present_uriTemplate))
                return false;
            if (!this.uriTemplate.equals(that.uriTemplate))
                return false;
        }

        boolean this_present_resourceURI = true && this.isSetResourceURI();
        boolean that_present_resourceURI = true && that.isSetResourceURI();
        if (this_present_resourceURI || that_present_resourceURI) {
            if (!(this_present_resourceURI && that_present_resourceURI))
                return false;
            if (!this.resourceURI.equals(that.resourceURI))
                return false;
        }

        boolean this_present_resourceSandboxURI = true && this.isSetResourceSandboxURI();
        boolean that_present_resourceSandboxURI = true && that.isSetResourceSandboxURI();
        if (this_present_resourceSandboxURI || that_present_resourceSandboxURI) {
            if (!(this_present_resourceSandboxURI && that_present_resourceSandboxURI))
                return false;
            if (!this.resourceSandboxURI.equals(that.resourceSandboxURI))
                return false;
        }

        boolean this_present_httpVerb = true && this.isSetHttpVerb();
        boolean that_present_httpVerb = true && that.isSetHttpVerb();
        if (this_present_httpVerb || that_present_httpVerb) {
            if (!(this_present_httpVerb && that_present_httpVerb))
                return false;
            if (!this.httpVerb.equals(that.httpVerb))
                return false;
        }

        boolean this_present_authType = true && this.isSetAuthType();
        boolean that_present_authType = true && that.isSetAuthType();
        if (this_present_authType || that_present_authType) {
            if (!(this_present_authType && that_present_authType))
                return false;
            if (!this.authType.equals(that.authType))
                return false;
        }
        boolean this_present_throttlingTier = true && this.isSetThrottlingTier();
        boolean that_present_throttlingTier = true && that.isSetThrottlingTier();
        if (this_present_throttlingTier || that_present_throttlingTier) {
            if (!(this_present_throttlingTier && that_present_throttlingTier))
                return false;
            if (!this.throttlingTier.equals(that.throttlingTier))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public int compareTo(URITemplate other) {
        if (!getClass().equals(other.getClass())) {
            return getClass().getName().compareTo(other.getClass().getName());
        }

        int lastComparison = 0;
        URITemplate typedOther = (URITemplate)other;

        lastComparison = Boolean.valueOf(isSetUriTemplate()).compareTo(typedOther.isSetUriTemplate());
        if (lastComparison != 0) {
            return lastComparison;
        }
        if (isSetUriTemplate()) {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.uriTemplate, typedOther.uriTemplate);
            if (lastComparison != 0) {
                return lastComparison;
            }
        }
        lastComparison = Boolean.valueOf(isSetResourceURI()).compareTo(typedOther.isSetResourceURI());
        if (lastComparison != 0) {
            return lastComparison;
        }
        if (isSetResourceURI()) {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.resourceURI, typedOther.resourceURI);
            if (lastComparison != 0) {
                return lastComparison;
            }
        }
        lastComparison = Boolean.valueOf(isSetResourceSandboxURI()).compareTo(typedOther.isSetResourceSandboxURI());
        if (lastComparison != 0) {
            return lastComparison;
        }
        if (isSetResourceSandboxURI()) {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.resourceSandboxURI, typedOther.resourceSandboxURI);
            if (lastComparison != 0) {
                return lastComparison;
            }
        }
        lastComparison = Boolean.valueOf(isSetHttpVerb()).compareTo(typedOther.isSetHttpVerb());
        if (lastComparison != 0) {
            return lastComparison;
        }
        if (isSetHttpVerb()) {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.httpVerb, typedOther.httpVerb);
            if (lastComparison != 0) {
                return lastComparison;
            }
        }
        lastComparison = Boolean.valueOf(isSetAuthType()).compareTo(typedOther.isSetAuthType());
        if (lastComparison != 0) {
            return lastComparison;
        }
        if (isSetAuthType()) {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.authType, typedOther.authType);
            if (lastComparison != 0) {
                return lastComparison;
            }
        }
        if (isSetThrottlingTier()) {
            lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.throttlingTier, typedOther.throttlingTier);
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
        schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
        schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("URITemplate(");
        boolean first = true;

        if (isSetUriTemplate()) {
            sb.append("uriTemplate:");
            if (this.uriTemplate == null) {
                sb.append("null");
            } else {
                sb.append(this.uriTemplate);
            }
            first = false;
        }
        if (isSetResourceURI()) {
            if (!first) sb.append(", ");
            sb.append("resourceURI:");
            if (this.resourceURI == null) {
                sb.append("null");
            } else {
                sb.append(this.resourceURI);
            }
            first = false;
        }
        if (isSetResourceSandboxURI()) {
            if (!first) sb.append(", ");
            sb.append("resourceSandboxURI:");
            if (this.resourceSandboxURI == null) {
                sb.append("null");
            } else {
                sb.append(this.resourceSandboxURI);
            }
            first = false;
        }
        if (isSetHttpVerb()) {
            if (!first) sb.append(", ");
            sb.append("httpVerb:");
            if (this.httpVerb == null) {
                sb.append("null");
            } else {
                sb.append(this.httpVerb);
            }
            first = false;
        }
        if (isSetAuthType()) {
            if (!first) sb.append(", ");
            sb.append("authType:");
            if (this.authType == null) {
                sb.append("null");
            } else {
                sb.append(this.authType);
            }
            first = false;
        }
        if (isSetThrottlingTier()) {
            if (!first) sb.append(", ");
            sb.append("throttlingTier:");
            if (this.throttlingTier == null) {
                sb.append("null");
            } else {
                sb.append(this.throttlingTier);
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
            read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
        } catch (org.apache.thrift.TException te) {
            throw new java.io.IOException(te);
        }
    }

    private static class URITemplateStandardSchemeFactory implements SchemeFactory {
        public URITemplateStandardScheme getScheme() {
            return new URITemplateStandardScheme();
        }
    }

    private static class URITemplateStandardScheme extends StandardScheme<URITemplate> {

        public void read(org.apache.thrift.protocol.TProtocol iprot, URITemplate struct) throws org.apache.thrift.TException {
            org.apache.thrift.protocol.TField schemeField;
            iprot.readStructBegin();
            while (true)
            {
                schemeField = iprot.readFieldBegin();
                if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
                    break;
                }
                switch (schemeField.id) {
                    case 1: // URI_TEMPLATE
                        if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
                            struct.uriTemplate = iprot.readString();
                            struct.setUriTemplateIsSet(true);
                        } else {
                            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                        }
                        break;
                    case 2: // RESOURCE_URI
                        if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
                            struct.resourceURI = iprot.readString();
                            struct.setResourceURIIsSet(true);
                        } else {
                            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                        }
                        break;
                    case 3: // RESOURCE_SANDBOX_URI
                        if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
                            struct.resourceSandboxURI = iprot.readString();
                            struct.setResourceSandboxURIIsSet(true);
                        } else {
                            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                        }
                        break;
                    case 4: // HTTP_VERB
                        if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
                            struct.httpVerb = iprot.readString();
                            struct.setHttpVerbIsSet(true);
                        } else {
                            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                        }
                        break;
                    case 5: // AUTH_TYPE
                        if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
                            struct.authType = iprot.readString();
                            struct.setAuthTypeIsSet(true);
                        } else {
                            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                        }
                        break;
                    case 6: // THROTTLING_TIER
                        if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
                            struct.throttlingTier = iprot.readString();
                            struct.setThrottlingTierIsSet(true);
                        } else {
                            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                        }
                        break;
                    default:
                        org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
                }
                iprot.readFieldEnd();
            }
            iprot.readStructEnd();

            // check for required fields of primitive type, which can't be checked in the validate method
            struct.validate();
        }

        public void write(org.apache.thrift.protocol.TProtocol oprot, URITemplate struct) throws org.apache.thrift.TException {
            struct.validate();

            oprot.writeStructBegin(STRUCT_DESC);
            if (struct.uriTemplate != null) {
                if (struct.isSetUriTemplate()) {
                    oprot.writeFieldBegin(URI_TEMPLATE_FIELD_DESC);
                    oprot.writeString(struct.uriTemplate);
                    oprot.writeFieldEnd();
                }
            }
            if (struct.resourceURI != null) {
                if (struct.isSetResourceURI()) {
                    oprot.writeFieldBegin(RESOURCE_URI_FIELD_DESC);
                    oprot.writeString(struct.resourceURI);
                    oprot.writeFieldEnd();
                }
            }
            if (struct.resourceSandboxURI != null) {
                if (struct.isSetResourceSandboxURI()) {
                    oprot.writeFieldBegin(RESOURCE_SANDBOX_URI_FIELD_DESC);
                    oprot.writeString(struct.resourceSandboxURI);
                    oprot.writeFieldEnd();
                }
            }
            if (struct.httpVerb != null) {
                if (struct.isSetHttpVerb()) {
                    oprot.writeFieldBegin(HTTP_VERB_FIELD_DESC);
                    oprot.writeString(struct.httpVerb);
                    oprot.writeFieldEnd();
                }
            }
            if (struct.authType != null) {
                if (struct.isSetAuthType()) {
                    oprot.writeFieldBegin(AUTH_TYPE_FIELD_DESC);
                    oprot.writeString(struct.authType);
                    oprot.writeFieldEnd();
                }
            }
            if (struct.throttlingTier != null) {
                if (struct.isSetThrottlingTier()) {
                    oprot.writeFieldBegin(THROTTLING_TIER_FIELD_DESC);
                    oprot.writeString(struct.throttlingTier);
                    oprot.writeFieldEnd();
                }
            }
            oprot.writeFieldStop();
            oprot.writeStructEnd();
        }

    }

    private static class URITemplateTupleSchemeFactory implements SchemeFactory {
        public URITemplateTupleScheme getScheme() {
            return new URITemplateTupleScheme();
        }
    }

    private static class URITemplateTupleScheme extends TupleScheme<URITemplate> {

        @Override
        public void write(org.apache.thrift.protocol.TProtocol prot, URITemplate struct) throws org.apache.thrift.TException {
            TTupleProtocol oprot = (TTupleProtocol) prot;
            BitSet optionals = new BitSet();
            if (struct.isSetUriTemplate()) {
                optionals.set(0);
            }
            if (struct.isSetResourceURI()) {
                optionals.set(1);
            }
            if (struct.isSetResourceSandboxURI()) {
                optionals.set(2);
            }
            if (struct.isSetHttpVerb()) {
                optionals.set(3);
            }
            if (struct.isSetAuthType()) {
                optionals.set(4);
            }
            if (struct.isSetThrottlingTier()) {
                optionals.set(5);
            }
            oprot.writeBitSet(optionals, 6);
            if (struct.isSetUriTemplate()) {
                oprot.writeString(struct.uriTemplate);
            }
            if (struct.isSetResourceURI()) {
                oprot.writeString(struct.resourceURI);
            }
            if (struct.isSetResourceSandboxURI()) {
                oprot.writeString(struct.resourceSandboxURI);
            }
            if (struct.isSetHttpVerb()) {
                oprot.writeString(struct.httpVerb);
            }
            if (struct.isSetAuthType()) {
                oprot.writeString(struct.authType);
            }
        }

        @Override
        public void read(org.apache.thrift.protocol.TProtocol prot, URITemplate struct) throws org.apache.thrift.TException {
            TTupleProtocol iprot = (TTupleProtocol) prot;
            BitSet incoming = iprot.readBitSet(6);
            if (incoming.get(0)) {
                struct.uriTemplate = iprot.readString();
                struct.setUriTemplateIsSet(true);
            }
            if (incoming.get(1)) {
                struct.resourceURI = iprot.readString();
                struct.setResourceURIIsSet(true);
            }
            if (incoming.get(2)) {
                struct.resourceSandboxURI = iprot.readString();
                struct.setResourceSandboxURIIsSet(true);
            }
            if (incoming.get(3)) {
                struct.httpVerb = iprot.readString();
                struct.setHttpVerbIsSet(true);
            }
            if (incoming.get(4)) {
                struct.authType = iprot.readString();
                struct.setAuthTypeIsSet(true);
            }
            if (incoming.get(5)) {
                struct.throttlingTier = iprot.readString();
                struct.setThrottlingTierIsSet(true);
            }
        }
    }

}
