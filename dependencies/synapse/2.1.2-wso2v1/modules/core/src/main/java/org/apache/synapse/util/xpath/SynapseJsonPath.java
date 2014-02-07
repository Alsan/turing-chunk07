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
package org.apache.synapse.util.xpath;

import com.jayway.jsonpath.JsonPath;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.commons.json.JsonFormatter;
import org.apache.synapse.config.SynapsePropertiesLoader;
import org.apache.synapse.config.xml.SynapsePath;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.jaxen.JaxenException;

import javax.xml.stream.XMLStreamException;
import java.io.*;

public class SynapseJsonPath extends SynapsePath {

    private final static String JSON_STREAM = "JSON_STREAM";
    private static final Log log = LogFactory.getLog(SynapseJsonPath.class);

    private String enableStreamingJsonPath = SynapsePropertiesLoader.loadSynapseProperties().
    getProperty(SynapseConstants.STREAMING_JSONPATH_PROCESSING);

    private JsonPath jsonPath;

    public SynapseJsonPath(String jsonPathExpression)  throws JaxenException {
        super(jsonPathExpression, SynapsePath.JSON_PATH, log);
        this.contentAware = true;
        this.expression = jsonPathExpression;
        jsonPath = JsonPath.compile(jsonPathExpression);
        this.setPathType(SynapsePath.JSON_PATH);
    }

    public String stringValueOf(final String jsonString){

        Object read;
        read = jsonPath.read(jsonString);
        return read.toString();

    }

    public String stringValueOf(MessageContext synCtx) {
        org.apache.axis2.context.MessageContext amc = ((Axis2MessageContext) synCtx).getAxis2MessageContext();
        InputStream stream = null;
        if(amc.getProperty(JSON_STREAM) == null || "true".equals(enableStreamingJsonPath)) {
            try {
                if(null == amc.getEnvelope().getBody().getFirstElement()) {
                    // Get message from PT Pipe.
                    stream = getMessageInputStreamPT(amc);
                } else {
                    // Message Already built.
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    JsonFormatter.toJson(amc.getEnvelope().getBody().getFirstElement(), bos);
                    stream = new ByteArrayInputStream(bos.toByteArray());
                }
                return stringValueOf(stream);
            } catch (IOException e) {
                handleException("Could not find JSON Stream in PassThrough Pipe during JSON path evaluation.", e);
            }
        } else {
            stream = (InputStream) amc.getProperty(JSON_STREAM);
            try {
                return stringValueOf(new ByteArrayInputStream(copyStreamAndReturnByteArray(synCtx, stream)));
            } catch (IOException e) {
                handleException("Failed to read input stream during JSON path evaluation.", e);
            }
        }

        return null;
    }

    public String stringValueOf(final InputStream jsonStream) {
        Object read;
        try {
            read = jsonPath.read(jsonStream);
            return read.toString();
        } catch (IOException e) {
            handleException("Error evaluating JSON Path", e);
        }

        return null;
    }

    private byte[] copyStreamAndReturnByteArray(MessageContext synCtx, InputStream stream) throws IOException {
        // We will set the stream into JSON_STREAM for any further processing.
        byte[] streamByteArray = IOUtils.toByteArray(stream);
        org.apache.axis2.context.MessageContext amc = ((Axis2MessageContext) synCtx).getAxis2MessageContext();
        amc.setProperty(JSON_STREAM, new BufferedInputStream(new ByteArrayInputStream(streamByteArray)));
        return streamByteArray;
    }

    public String getJsonPathExpression() {
        return expression;
    }

    public void setJsonPathExpression(String jsonPathExpression) {
        this.expression = jsonPathExpression;
    }
}
