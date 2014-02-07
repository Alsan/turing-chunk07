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

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.builder.Builder;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

public class JsonBuilder implements Builder {
    /** The JSON Key for wrapper type JSON Object */
    public static final String K_OBJECT = "\"jsonObject\"";
    /** The JSON Key for wrapper type anonymous JSON array */
    public static final String K_ARRAY = "\"jsonArray\"";
    /** The JSON Key for wrapper type anonymous JSON array elements */
    public static final String K_ARRAY_ELEM = "\"jsonElement\"";

    private static final Log logger = LogFactory.getLog(JsonBuilder.class.getName());

    public OMElement processDocument(InputStream inputStream, String s,
                                     MessageContext messageContext) throws AxisFault {
        return toXml(inputStream, true);
    }

    /**
     * Converts the JSON input stream to XML element.
     * @param jsonStream JSON input stream
     * @param pIs Whether or not to add XML processing instructions to the output XML.<br/>
     *            This property is useful when converting JSON payloads with array objects.
     * @return OMElement that is the XML representation of the input JSON data.
     */
    public static OMElement toXml(InputStream jsonStream, boolean pIs) throws AxisFault {
        if (jsonStream == null) {
            logger.error("JSON input stream is null.");
            return null;
        }
        JsonDataSourceImpl jsonData = new JsonDataSourceImpl(jsonStream);
        OMElement element;
        try {
            XMLStreamReader streamReader = jsonData.getReader(pIs);
            element = new StAXOMBuilder(streamReader).getDocumentElement();
        } catch (XMLStreamException e) {
            logger.error("Could not create OMElement from JSON Stream. JSON input is invalid. " +
                         "Error>>>\n" + e.getLocalizedMessage());
            throw AxisFault.makeFault(e);
        }
        return element;
    }
}

