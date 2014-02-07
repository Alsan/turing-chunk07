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

import org.apache.axiom.om.OMOutputFormat;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.MessageFormatter;
import org.apache.axis2.util.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class JsonStreamFormatter implements MessageFormatter {
    private static final Log logger = LogFactory.getLog(JsonStreamFormatter.class.getName());

    public byte[] getBytes(MessageContext messageContext, OMOutputFormat format) throws AxisFault {
        if (messageContext.getProperty(JsonStreamBuilder.JSON_STRING) != null) {
            String jsonResponse = (String) messageContext.getProperty(JsonStreamBuilder.JSON_STRING);
            return jsonResponse.getBytes();
        } else if (messageContext.getProperty(JsonStreamBuilder.JSON_STREAM) != null) {
            try {
                InputStream stream = (InputStream) messageContext.getProperty(JsonStreamBuilder.JSON_STREAM);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int c;
                while ((c = stream.read()) != -1) {
                    outputStream.write(c);
                }
                outputStream.flush();
                return outputStream.toByteArray();
            } catch (IOException e) {
                throw AxisFault.makeFault(e);
            }
        }
        throw new AxisFault("Could not find the JSON response.");
    }

    public String getContentType(MessageContext messageContext, OMOutputFormat format,
                                 String soapActionString) {
        String contentType = (String) messageContext.getProperty(Constants.Configuration.CONTENT_TYPE);
        String encoding = format.getCharSetEncoding();
        if (contentType == null) {
            contentType = (String) messageContext.getProperty(Constants.Configuration.MESSAGE_TYPE);
        }
        if (encoding != null) {
            contentType += "; charset=" + encoding;
        }
        return contentType;
    }

    public void writeTo(MessageContext messageContext, OMOutputFormat format,
                        OutputStream out, boolean preserve) throws AxisFault {
        toJson(messageContext, out);
    }

    public static void toJson(MessageContext messageContext, OutputStream out) throws AxisFault {
        if (messageContext.getProperty(JsonStreamBuilder.JSON_STRING) != null) {
            String jsonResponse = (String) messageContext.getProperty(JsonStreamBuilder.JSON_STRING);
            try {
                out.write(jsonResponse.getBytes());
            } catch (IOException e) {
                throw AxisFault.makeFault(e);
            }
        } else if (messageContext.getProperty(JsonStreamBuilder.JSON_STREAM) != null) {
            try {
                IOUtils.copy((InputStream) messageContext.getProperty(
                        JsonStreamBuilder.JSON_STREAM), out, false);
            } catch (IOException e) {
                throw AxisFault.makeFault(e);
            }
        } else {
            SOAPBody body = messageContext.getEnvelope().getBody();
            if (logger.isDebugEnabled()) {
                logger.debug("Converting SOAP body to JSON.>>\n" + body.toString());
            }
            JsonFormatter.toJson(body.getFirstElement(), out);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Wrote JSON input stream to output stream.");
        }
    }

    public URL getTargetAddress(MessageContext messageContext, OMOutputFormat format, URL targetURL)
            throws AxisFault {
        if (logger.isDebugEnabled()) {
            logger.debug("Not implemented. #getTargetAddress()");
        }
        return targetURL;
    }

    public String formatSOAPAction(MessageContext messageContext, OMOutputFormat omOutputFormat,
                                   String s) {
        if (logger.isDebugEnabled()) {
            logger.debug("Not implemented. #formatSOAPAction()");
        }
        return null;
    }
}
