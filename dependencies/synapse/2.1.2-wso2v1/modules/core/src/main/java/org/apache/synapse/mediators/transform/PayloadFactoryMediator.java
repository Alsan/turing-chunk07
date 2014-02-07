/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.synapse.mediators.transform;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.http.protocol.HTTP;
import org.apache.synapse.commons.builders.SynapseMessageConverter;
import org.apache.synapse.commons.json.JsonBuilder;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.commons.json.JsonFormatter;
import org.apache.synapse.config.xml.SynapsePath;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.mediators.Value;
import org.apache.synapse.util.AXIOMUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PayloadFactoryMediator extends AbstractMediator {
    private Value formatKey = null;
    private boolean isFormatDynamic = false;
    private String formatRaw;
    private String mediaType = XML_TYPE;
    private final static String JSON_CONTENT_TYPE = "application/json";
    private final static String XML_CONTENT_TYPE  = "application/xml";
    private final static String JSON_TYPE = "json";
    private final static String XML_TYPE = "xml";
    private final static String STRING_TYPE = "str";

    private List<Argument> pathArgumentList = new ArrayList<Argument>();
    private Pattern pattern = Pattern.compile("\\$(\\d)+");

    private final static String ghostFieldPrefix = "pf_ghost_field";
    private final static Pattern ghostFieldPattern = Pattern.compile("pf_ghost_field(\\d+)?");
    private Matcher ghostFieldMatcher;
    //private Pattern ghostFieldPattern = Pattern.compile("\\{?\"pf_ghost_field(\\d)?\"\\s*\\:\\s*\\{?\\s*\"(\\$(\\d+))?\"\\s*\\}?|\\{?\"pf_ghost_field(\\d)?\"\\s*\\:\\s*\\{?");

    private static final Log log = LogFactory.getLog(PayloadFactoryMediator.class);


    /**
     * Contains 2 paths - one when JSON Streaming is in use (mediateJsonStreamPayload) and the other for regular
     * builders (mediatePayload).
     * @param synCtx the current message for mediation
     * @return
     */
    public boolean mediate(MessageContext synCtx) {
        String format = formatRaw;  // This is to preserve the original format,because the format will be altered when parsing to json.

        boolean isStreamBuilder = isStreamBuilder(synCtx);

        if (isStreamBuilder) {
            return mediateJsonStreamPayload(synCtx, format, isStreamBuilder);
        }

        return mediatePayload(synCtx, format, isStreamBuilder);
    }

    /**
     * Check to see whether the builder in use is JSON Streaming or other regular builder (XML or JSON).
     * @param synCtx
     * @return true if JSON_STREAM is in axis2 context.
     */
    private boolean isStreamBuilder(MessageContext synCtx) {
        org.apache.axis2.context.MessageContext a2mc = ((Axis2MessageContext) synCtx).getAxis2MessageContext();
        if(null != a2mc.getProperty(SynapseMessageConverter.JSON_STREAM)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets the content type based on the request content type and payload factory media type. This should be called
     * at the end before returning from the mediate() function.
     * @param synCtx
     */
    private void setContentType(MessageContext synCtx) {
        org.apache.axis2.context.MessageContext a2mc = ((Axis2MessageContext) synCtx).getAxis2MessageContext();
        String requestContentType = (String) a2mc.getProperty(Constants.Configuration.CONTENT_TYPE);
        if (requestContentType == null) {
            requestContentType = (String) a2mc.getProperty(Constants.Configuration.MESSAGE_TYPE);
        }

        if(mediaType.equals(XML_TYPE)) {
            if(requestContentType == null || !requestContentType.contains(mediaType)) {
                a2mc.setProperty(Constants.Configuration.MESSAGE_TYPE, XML_CONTENT_TYPE);
                a2mc.setProperty(Constants.Configuration.CONTENT_TYPE, XML_CONTENT_TYPE);
                handleSpecialProperties(XML_CONTENT_TYPE, a2mc);
            }
        } else if(mediaType.equals(JSON_TYPE) && !requestContentType.equals(JSON_CONTENT_TYPE)) {
            a2mc.setProperty(Constants.Configuration.MESSAGE_TYPE, JSON_CONTENT_TYPE);
            a2mc.setProperty(Constants.Configuration.CONTENT_TYPE, JSON_CONTENT_TYPE);
            handleSpecialProperties(JSON_CONTENT_TYPE, a2mc);
        }
    }

    // This is copied from PropertyMediator, required to change Content-Type
    private void handleSpecialProperties(Object resultValue,
                                         org.apache.axis2.context.MessageContext axis2MessageCtx) {
        axis2MessageCtx.setProperty(org.apache.axis2.Constants.Configuration.CONTENT_TYPE, resultValue);
        Object o = axis2MessageCtx.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
        Map _headers = (Map) o;
        if (_headers != null) {
            _headers.remove(HTTP.CONTENT_TYPE);
            _headers.put(HTTP.CONTENT_TYPE, resultValue);
        }
    }

    /**
     * Processes payloads from regular builders (XML or JSON).
     * @param synCtx
     * @param format
     * @param isStreamBuilder
     * @return
     */
    private boolean mediatePayload(MessageContext synCtx, String format, Boolean isStreamBuilder) {
        SOAPBody soapBody = synCtx.getEnvelope().getBody();
        StringBuffer result = new StringBuffer();
        regexTransform(result, synCtx, format, isStreamBuilder);

        OMElement resultElement = null;
        try {
            if(isXML(result.toString())) {
                resultElement = AXIOMUtil.stringToOM(result.toString());
            } else {
               ByteArrayInputStream bis = new ByteArrayInputStream(result.toString().getBytes());
               resultElement = JsonBuilder.toXml(bis, true);
            }
        } catch (XMLStreamException e) {      /*Use the XMLStreamException and log the proper stack trace*/
            handleException("Unable to create a valid XML payload", synCtx);
        } catch (AxisFault e) {
            handleException("Unable to create a valid XML payload", synCtx);
        } catch(OMException e) {
            handleException("Unable to create a valid XML payload", synCtx);
        }

        for (Iterator itr = soapBody.getChildElements(); itr.hasNext(); ) {
            OMElement child = (OMElement) itr.next();
            child.detach();
        }

        if (resultElement != null) {
            OMElement firstChild = resultElement.getFirstElement();
            if (firstChild != null) {
                QName resultQName = firstChild.getQName();
                if (resultQName.getLocalPart().equals("Envelope") && (
                        resultQName.getNamespaceURI().equals(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI) ||
                                resultQName.getNamespaceURI().
                                        equals(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI))) {
                    SOAPEnvelope soapEnvelope = AXIOMUtils.getSOAPEnvFromOM(resultElement.getFirstElement());
                    if (soapEnvelope != null) {
                        try {
                            synCtx.setEnvelope(soapEnvelope);
                        } catch (AxisFault axisFault) {
                            handleException("Unable to attach SOAPEnvelope", axisFault, synCtx);
                        }
                    }
                } else if (resultElement.getLocalName().equals("jsonObject") || resultElement.getLocalName().equals("jsonArray")) {
                    for(Iterator itr = resultElement.getParent().getChildren(); itr.hasNext();) {
                        OMElement child = (OMElement) itr.next();
                        soapBody.addChild(child);
                    }
                } else {
                    for (Iterator itr = resultElement.getChildElements(); itr.hasNext(); ) {
                        OMElement child = (OMElement) itr.next();
                        soapBody.addChild(child);
                    }
                }
            }
        }

        setContentType(synCtx);

        return true;
    }

    /**
     * Processes payloads from JSON stream builders.
     * @param synCtx
     * @param format
     * @param isStreamBuilder
     * @return
     */
    private boolean mediateJsonStreamPayload(MessageContext synCtx, String format, Boolean isStreamBuilder) {
        org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) synCtx).getAxis2MessageContext();
        StringBuffer stringBuffer = new StringBuffer();
        regexTransform(stringBuffer, synCtx, format, isStreamBuilder);
        if(this.mediaType.equals(XML_TYPE)) {
            try {
                OMElement omXML = AXIOMUtil.stringToOM(stringBuffer.toString());
                axis2MessageContext.getEnvelope().getBody().addChild(omXML.getFirstElement());
            } catch (XMLStreamException e) {
                handleException("Error creating SOAP Envelope from source " + stringBuffer.toString(), synCtx);
            }
        } else {
            InputStream formattedJsonStream = new ByteArrayInputStream(stringBuffer.toString().getBytes());
            axis2MessageContext.setProperty(SynapseMessageConverter.JSON_STREAM, formattedJsonStream);
        }

        setContentType(synCtx);

        return true;
    }


    /**
     * Calls the replace function. isFormatDynamic check is used to remove indentations which come from registry based
     * configurations.
     * @param result
     * @param synCtx
     * @param format
     * @param isStreamBuilder
     */
    private void regexTransform(StringBuffer result, MessageContext synCtx, String format, Boolean isStreamBuilder) {
        if (isFormatDynamic()) {
            String key = formatKey.evaluateValue(synCtx);
            OMElement element = (OMElement) synCtx.getEntry(key);
            removeIndentations(element);
            String format2 = element.toString();
            replace(format2, result, synCtx, isStreamBuilder);
        } else {
            replace(format, result, synCtx, isStreamBuilder);
        }
    }

    /**
     * Helper function to convert OutputStream into a String.
     * @param os
     * @return
     */
    private String outputStreamToString(OutputStream os) {
        StringBuilder returnString = new StringBuilder(os.toString());
        return returnString.toString();
    }


    /**
     * Ideal solution for this may not be recursion.
     * @param json JSONObject
     * @param synCtx Message Context
     * @return Object (valid JSONObject)
     */
    public Object processGhostField(JSONObject json, MessageContext synCtx) {
        if(!json.toString().contains(ghostFieldPrefix)) {
            return json;
        }

        JSONObject replaceBy = new JSONObject();

        Iterator<?> keys = json.keys();
        while(keys.hasNext()) {
            String key = (String) keys.next();
            ghostFieldMatcher = ghostFieldPattern.matcher(key);

            if(ghostFieldMatcher.matches()) {
                try {
                    if(json.get(key) instanceof JSONObject) {
                        JSONObject internalObject = (JSONObject)
                                processGhostField((JSONObject) json.get(key), synCtx);
                        Iterator<?> internalKeys = internalObject.keys();
                        while(internalKeys.hasNext()) {
                            String internalKey = (String)
                                    internalKeys.next();
                            Object obj = internalObject.get(internalKey);
                            if(obj instanceof JSONObject) {
                                replaceBy.put(internalKey, ((JSONObject)
                                        obj));
                            } else if(obj instanceof JSONArray) {
                                replaceBy.put(internalKey, ((JSONArray)
                                        obj));
                            } else if(obj instanceof String) {
                                replaceBy.put(internalKey, obj);
                            }
                        }
                    } else if(json.get(key) instanceof JSONArray) {
                        replaceBy.put(key, json.get(key));
                    } else {
                        replaceBy.put(key, json.get(key));
                    }

                } catch (JSONException e) {
                    handleException("Error while processing Payload Factory ghost field. " + e.getMessage(), synCtx);
                }
            } else {
                try {
                    if(json.get(key) instanceof JSONObject) {
                        Object obj = (Object)
                                processGhostField((JSONObject) json.get(key), synCtx);
                        replaceBy.put(key, obj);
                    } else if(json.get(key) instanceof JSONArray) {
                        Object obj = json.get(key);
                        replaceBy.put(key, ((JSONArray) obj));
                    } else if(json.get(key) instanceof String) {
                        replaceBy.put(key, json.get(key));
                    }
                } catch (JSONException e) {
                    handleException("Error while processing Payload Factory ghost field. " + e.getMessage(), synCtx);
                }
            }
        }
        return replaceBy;
    }

    /**
     * Replaces the payload format with SynapsePath arguments which are evaluated using getArgValues().
     *
     * @param format
     * @param result
     * @param synCtx
     */
    private void replace(String format, StringBuffer result, MessageContext synCtx, boolean isStreamBuilder) {
        HashMap<String, String>[] argValues;
        HashMap<String, String> replacement = null;
        Map.Entry<String, String> replacementEntry;
        String replacementValue = null;

        argValues = getArgValues(synCtx, isStreamBuilder);

        Matcher matcher;

        if (mediaType != null && mediaType.equals(JSON_TYPE)) {
            matcher = pattern.matcher(format);
        } else {
            matcher = pattern.matcher("<dummy>" + format + "</dummy>");
        }

        try {
            while (matcher.find()) {
                String matchSeq = matcher.group();
                int argIndex;
                try {
                    argIndex = Integer.parseInt(matchSeq.substring(1, matchSeq.length()));
                } catch (NumberFormatException e) {
                    argIndex = Integer.parseInt(matchSeq.substring(2, matchSeq.length()-1));
                }
                replacement = argValues[argIndex-1];
                replacementEntry =  replacement.entrySet().iterator().next();
                if(mediaType.equals(JSON_TYPE) && inferReplacementType(replacementEntry).equals(XML_TYPE)) {
                    // XML to JSON conversion here
                    try {
                        replacementValue = "<jsonObject>" + replacementEntry.getKey() + "</jsonObject>";
                        OMElement omXML = AXIOMUtil.stringToOM(replacementValue);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        JsonFormatter.toJson(omXML, bos);
                        replacementValue = outputStreamToString(bos);
                    } catch (XMLStreamException e) {
                        handleException("Error parsing XML for JSON conversion, please check your xPath expressions return valid XML: ", synCtx);
                    } catch (AxisFault e) {
                        handleException("Error converting XML to JSON", synCtx);
                    }
                } else if(mediaType.equals(XML_TYPE) && inferReplacementType(replacementEntry).equals(JSON_TYPE)) {
                    // JSON to XML conversion here
                    try {
                        ByteArrayInputStream bis = new ByteArrayInputStream(replacementEntry.getKey().getBytes());
                        OMElement omXML = JsonBuilder.toXml(bis, false);
                        replacementValue = omXML.toString();
                    } catch (AxisFault e) {
                        handleException("Error converting JSON to XML, please check your JSON Path expressions return valid JSON: ", synCtx);
                    }
                } else {
                    // No conversion required, as path evaluates to regular String.
                    replacementValue = replacementEntry.getKey();
                }

                matcher.appendReplacement(result, replacementValue);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Mis-match detected between number of formatters and arguments", e);
        }
        matcher.appendTail(result);

        String returnStr = null;
        try {
            if(result.toString().contains("pf_ghost_field")) {
                returnStr = processGhostField(new JSONObject(result.toString()), synCtx).toString();
                result.setLength(0);
                result.append(returnStr);
            }
        } catch (JSONException e) {
            handleException("Error while processing Payload Factory ghost field. " + e.getMessage(), synCtx);
        }
    }

    /**
     * Helper function that takes a Map of String, String where key contains the value of an evaluated SynapsePath
     * expression and value contains the type of SynapsePath in use.
     *
     * It returns the type of conversion required (XML | JSON | String) based on the actual returned value and the path
     * type.
     *
     * @param entry
     * @return
     */
    private String inferReplacementType(Map.Entry<String, String> entry) {
        if(entry.getValue().equals(SynapsePath.X_PATH) && isXML(entry.getKey())) {
            return XML_TYPE;
        } else if(entry.getValue().equals(SynapsePath.X_PATH) && !isXML(entry.getKey())) {
            return STRING_TYPE;
        } else if(entry.getValue().equals(SynapsePath.JSON_PATH) && isJson(entry.getKey())) {
            return JSON_TYPE;
        } else if(entry.getValue().equals(SynapsePath.JSON_PATH) && !isJson((entry.getKey()))) {
            return STRING_TYPE;
        } else {
            return STRING_TYPE;
        }
    }

    /**
     * Helper function that returns true if value passed is of JSON type.
     * @param value
     * @return
     */
    private boolean isJson(String value) {
        if(value.trim().charAt(0) == '{' || value.trim().charAt(0) == '[') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper function to remove indentations.
     * @param element
     */
    private void removeIndentations(OMElement element) {
        List<OMText> removables = new ArrayList<OMText>();
        removeIndentations(element, removables);
        for (OMText node : removables) {
            node.detach();
        }
    }

    /**
     * Helper function to remove indentations.
     * @param element
     * @param removables
     */
    private void removeIndentations(OMElement element, List<OMText> removables) {
        Iterator children = element.getChildren();
        while (children.hasNext()) {
            Object next = children.next();
            if (next instanceof OMText) {
                OMText text = (OMText) next;
                if (text.getText().trim().equals("")) {
                    removables.add(text);
                }
            } else if (next instanceof OMElement) {
                removeIndentations((OMElement) next, removables);
            }
        }
    }

    /**
     * Goes through SynapsePath argument list, evaluating each by calling stringValueOf and returns a HashMap String, String
     * array where each item will contain a hash map with key "evaluated expression" and value "SynapsePath type".
     * @param synCtx
     * @param isStreamBuilder
     * @return
     */
    private HashMap<String, String>[] getArgValues(MessageContext synCtx, boolean isStreamBuilder) {

        HashMap<String, String>[] argValues = new HashMap[pathArgumentList.size()];
        HashMap<String, String> valueMap = null;
        String value = "";
        for (int i = 0; i < pathArgumentList.size(); ++i) {       /*ToDo use foreach*/
            Argument arg = pathArgumentList.get(i);
            if (arg.getValue() != null) {
                value = arg.getValue();
                if (!isXML(value)) {
                    value = StringEscapeUtils.escapeXml(value);
                }
                value = Matcher.quoteReplacement(value);

            } else if (arg.getExpression() != null) {
                value = arg.getExpression().stringValueOf(synCtx);
                if (value != null) {
                    // escaping string unless there might be exceptions when tries to insert values
                    // such as string with & (XML special char) and $ (regex special char)
                    if (!isXML(value) && !arg.getExpression().getPathType().equals(SynapsePath.JSON_PATH)) {
                        value = StringEscapeUtils.escapeXml(value);
                    }
                    value = Matcher.quoteReplacement(value);
                } else {
                    value = "";
                }
            } else {
                handleException("Unexpected arg type detected", synCtx);
            }
            //value = value.replace(String.valueOf((char) 160), " ").trim();
            valueMap = new HashMap<String, String>();
            if(null != arg.getExpression()) {
                valueMap.put(value, arg.getExpression().getPathType());
            } else {
                valueMap.put(value, SynapsePath.X_PATH);
            }
            argValues[i] = valueMap;
        }
        return argValues;
    }

    public String getFormat() {
        return formatRaw;
    }

    public void setFormat(String format) {
        this.formatRaw = format;
    }

    public void addPathArgument(Argument arg) {
        pathArgumentList.add(arg);
    }

    public List<Argument> getPathArgumentList() {
        return pathArgumentList;
    }

    /**
     * Helper function that returns true if value passed is of XML Type.
     * @param value
     * @return
     */
    private boolean isXML(String value) {
        try {
            AXIOMUtil.stringToOM(value);
        } catch (XMLStreamException ignore) {
            // means not a xml
            return false;
        } catch (OMException ignore) {
            // means not a xml
            return false;
        }
        return true;
    }

    public String getType() {
        return mediaType;
    }

    public void setType(String type) {
        this.mediaType = type;
    }

    /**
     * To get the key which is used to pick the format definition from the local registry
     *
     * @return return the key which is used to pick the format definition from the local registry
     */
    public Value getFormatKey() {
        return formatKey;
    }

    /**
     * To set the local registry key in order to pick the format definition
     *
     * @param key the local registry key
     */
    public void setFormatKey(Value key) {
        this.formatKey = key;
    }

    public void setFormatDynamic(boolean formatDynamic) {
        this.isFormatDynamic = formatDynamic;
    }

    public boolean isFormatDynamic() {
        return isFormatDynamic;
    }


}
