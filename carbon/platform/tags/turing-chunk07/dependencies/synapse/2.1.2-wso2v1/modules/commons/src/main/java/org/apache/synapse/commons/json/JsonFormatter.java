/**
 *  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.synapse.commons.json;

import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLOutputFactory;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMOutputFormat;
import org.apache.axiom.om.OMText;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.MessageFormatter;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.commons.builders.MessageConversionException;
import org.apache.synapse.commons.builders.SynapseMessageConverter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;

public class JsonFormatter implements MessageFormatter {
    private static final Log logger = LogFactory.getLog(JsonFormatter.class.getName());

    private static final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

    /** This configuration is used to format the JSON output produced by the JSON writer. */
    private static final JsonXMLConfig jsonConfig = new JsonXMLConfigBuilder()
            .multiplePI(true)
            .autoArray(true)
            .autoPrimitive(true)
            .namespaceDeclarations(false)
            .namespaceSeparator('_')
            .build();

    private static final JsonXMLOutputFactory jsonOutputFactory = new JsonXMLOutputFactory(jsonConfig);

    public byte[] getBytes(MessageContext messageContext, OMOutputFormat omOutputFormat)
            throws AxisFault {
        OMElement element = messageContext.getEnvelope().getBody().getFirstElement();
        if (logger.isDebugEnabled()) {
            logger.debug("Converting to Byte Array.");
        }
        if (element == null) {
            return new JsonStreamFormatter().getBytes(messageContext, omOutputFormat);
        } else {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            toJson(element, outputStream);
            return outputStream.toByteArray();
        }
    }

    public void writeTo(MessageContext messageContext, OMOutputFormat omOutputFormat,
                        OutputStream outputStream, boolean b) throws AxisFault {
        OMElement element = messageContext.getEnvelope().getBody().getFirstElement();
        if (logger.isDebugEnabled()) {
            logger.debug("Writing JSON message to output stream.");
        }
        if (element == null) {
            JsonStreamFormatter.toJson(messageContext, outputStream);
            return;
        }
        toJson(element, outputStream);
    }

    public static void toJson(OMElement element, OutputStream outputStream) throws AxisFault {
        if (logger.isDebugEnabled()) {
            logger.debug("Converting OMElement to JSON output stream.");
        }
        if (element == null) {
            logger.error("OMElement is null. Cannot convert to JSON.");
            throw new AxisFault("OMElement is null.");
        }
        transformElement(element, true);
        boolean isJsonObject = true;
        boolean isNone = false;
        if (element.getLocalName().equals(
                JsonBuilder.K_OBJECT.substring(1, JsonBuilder.K_OBJECT.length() - 1))) {
            isJsonObject = true; // built by the JsonBuilder and is a jsonObject
        } else if (element.getLocalName().equals(
                JsonBuilder.K_ARRAY.substring(1, JsonBuilder.K_ARRAY.length() - 1))) {
            isJsonObject = false;  // built by the JsonBuilder and is a jsonArray
        } else {
            isNone = true; // a payload that has not been built by the JsonBuilder.
        }
        try {
            ByteArrayOutputStream xmlStream = new ByteArrayOutputStream();
            element.serialize(xmlStream);
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(
                    new XmlReaderDelegate(xmlInputFactory.createXMLStreamReader(
                            new ByteArrayInputStream(xmlStream.toByteArray())
                    ))
            );
            OutputStream out;
            if (isNone) {
                out = outputStream;
            } else {
                out = new ByteArrayOutputStream();
            }
            XMLEventWriter jsonWriter = jsonOutputFactory.createXMLEventWriter(out);
            jsonWriter.add(xmlEventReader);
            xmlEventReader.close();
            jsonWriter.close();
            if (isNone) {
                return;
            }
            byte[] stream = ((ByteArrayOutputStream) out).toByteArray();
            int high = (isJsonObject ? JsonDataSourceImpl.END_OBJECT.length()
                                     : JsonDataSourceImpl.END_ARRAY.length());
            int low = (isJsonObject ? (JsonDataSourceImpl.WRAPPER_OBJECT.length())
                                    : (JsonDataSourceImpl.WRAPPER_ARRAY.length()));
            if (logger.isDebugEnabled()) {
                logger.debug("Converted JSON >>>\n"
                             + new String(stream, low, stream.length - high - low));
            }
            outputStream.write(stream, low, stream.length - high - low);
        } catch (XMLStreamException e) {
            logger.error("Could not convert OMElement to JSON. Invalid XML payload. " +
                         "Error>>>\n" + e.getLocalizedMessage());
            throw AxisFault.makeFault(e);
        } catch (IOException e) {
            logger.error("Could not write JSON stream. Error>>>\n" + e.getLocalizedMessage());
            throw AxisFault.makeFault(e);
        }
    }

    /**
     * Converts the OMElement to its JSON representation and returns as a String
     * @param element OMElement to be converted to JSON.
     * @return A String builder instance that contains the converted JSON string.
     */
    public static StringBuilder toJson(OMElement element) throws AxisFault {
        if (logger.isDebugEnabled()) {
            logger.debug("Converting OMElement to JSON String.");
        }
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        toJson(element, byteStream);
        return new StringBuilder(new String(byteStream.toByteArray()));
    }

    public String getContentType(MessageContext messageContext, OMOutputFormat format,
                                 String soapActionString) {
        String contentType = (String)messageContext.getProperty(Constants.Configuration.CONTENT_TYPE);
        String encoding = format.getCharSetEncoding();
        if (contentType == null) {
            contentType = (String)messageContext.getProperty(Constants.Configuration.MESSAGE_TYPE);
        }
        if (encoding != null) {
            contentType += "; charset=" + encoding;
        }
        return contentType;
    }

    public URL getTargetAddress(MessageContext messageContext, OMOutputFormat omOutputFormat,
                                URL url) throws AxisFault {
        if (logger.isDebugEnabled()) {
            logger.debug("Not implemented. #getTargetAddress()");
        }
        return null;
    }

    public String formatSOAPAction(MessageContext messageContext, OMOutputFormat omOutputFormat,
                                   String s) {
        if (logger.isDebugEnabled()) {
            logger.debug("Not implemented. #formatSOAPAction()");
        }
        return null;
    }

    private static void transformElement(OMElement element, boolean processAttrbs) {
        removeIndentations(element);
        removeNamespaces(element, processAttrbs);
    }

    private static void removeIndentations(OMElement elem) {
        Iterator children = elem.getChildren();
        while (children.hasNext()) {
            OMNode child = (OMNode)children.next();
            if (child instanceof OMText) {
                if ("".equals(((OMText) child).getText().trim())) {
                    children.remove();
                }
            } else if (child instanceof OMElement) {
                removeIndentations((OMElement) child);
            }
        }
    }

    private static void removeNamespaces(OMElement element, boolean processAttrbs) {
        OMNamespace ns = element.getNamespace();
        Iterator i  = element.getAllDeclaredNamespaces();
        while (i.hasNext()) {
            Object o = i.next();
            i.remove();
        }
        String prefix;
        if (ns != null) {
            prefix = "";//element.getNamespace().getPrefix();
            element.setNamespace(element.getOMFactory().createOMNamespace("", prefix));
        }
        Iterator children = element.getChildElements();
        while (children.hasNext()) {
            removeNamespaces((OMElement)children.next(), processAttrbs);
        }
        if (!processAttrbs) {
            return;
        }
        Iterator attrbs = element.getAllAttributes();
        while (attrbs.hasNext()) {
            OMAttribute attrb = (OMAttribute)attrbs.next();
            prefix = "";//attrb.getQName().getPrefix();
            attrb.setOMNamespace(attrb.getOMFactory().createOMNamespace("", prefix));
            element.removeAttribute(attrb);
        }
    }

    public void convert(OMElement element, OutputStream outputStream, String fromMediaType,
                        String toMediaType) throws MessageConversionException {
        if (SynapseMessageConverter.MEDIA_TYPE_APP_XML.equals(fromMediaType)
            && SynapseMessageConverter.MEDIA_TYPE_JSON.equals(toMediaType)) {
            try {
                toJson(element, outputStream);
            } catch (AxisFault e) {
                throw new MessageConversionException(e);
            }
        } else {
            logger.warn("Cannot convert from " + fromMediaType + " to " + toMediaType);
            throw new MessageConversionException("Cannot convert from " + fromMediaType + " to " + toMediaType);
        }
    }

    public void convert(MessageContext messageContext, OutputStream outputStream,
                        String fromMediaType, String toMediaType) throws MessageConversionException {
        if (messageContext.getEnvelope().getBody().getFirstElement() != null) {
            convert(messageContext.getEnvelope().getBody().getFirstElement(), outputStream,
                    fromMediaType, toMediaType);
        }
    }

    public StringBuilder convert(OMElement element, String fromMediaType, String toMediaType) throws MessageConversionException {
        if (SynapseMessageConverter.MEDIA_TYPE_APP_XML.equals(fromMediaType)
            && SynapseMessageConverter.MEDIA_TYPE_JSON.equals(toMediaType)) {
            try {
                return toJson(element);
            } catch (AxisFault e) {
                throw new MessageConversionException(e);
            }
        } else {
            logger.warn("Cannot convert from " + fromMediaType + " to " + toMediaType);
            throw new MessageConversionException("Cannot convert from " + fromMediaType + " to " + toMediaType);
        }
    }

    public StringBuilder convert(MessageContext messageContext, String fromMediaType,
                                 String toMediaType) throws MessageConversionException {
        if (messageContext.getEnvelope().getBody().getFirstElement() != null) {
            return convert(messageContext.getEnvelope().getBody().getFirstElement(),
                           fromMediaType, toMediaType);
        }
        return null;
    }
}
