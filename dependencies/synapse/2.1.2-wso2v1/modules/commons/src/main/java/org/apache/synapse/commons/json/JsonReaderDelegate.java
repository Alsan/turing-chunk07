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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

/**
 * Processes special JSON keys that start with digits. <br/>
 * eg. "12X12" <br/>
 */
public class JsonReaderDelegate extends StreamReaderDelegate {
    public static final String ID = "_JsonReader";
    /** Used when the local name starts with a digit character. */
    public static final String PRECEDING_DIGIT_S = "_PD_";
    /** Final prefix for local names that have preceding digits */
    public static final String PRECEDING_DIGIT = ID + PRECEDING_DIGIT_S;

    private static final Log logger = LogFactory.getLog(JsonReaderDelegate.class.getName());

    public JsonReaderDelegate(XMLStreamReader reader) {
        super(reader);
        if (logger.isDebugEnabled()) {
            logger.debug("Setting XMLStreamReader " + reader.getClass().getName());
        }
    }

    public String getLocalName() {
        String localName = super.getLocalName();
        if (localName == null || "".equals(localName)) {
            return localName;
        }
        if (Character.isDigit(localName.charAt(0))) {
            return PRECEDING_DIGIT + localName;
        }
        return localName;
    }

    public QName getName() {
        QName qName = super.getName();
        String localName = qName.getLocalPart();
        if (localName == null || "".equals(localName)) {
            return qName;
        }
        if (Character.isDigit(localName.charAt(0))) {
            localName = PRECEDING_DIGIT + localName;
            return new QName(qName.getNamespaceURI(), localName, qName.getPrefix());

        }
        return qName;
    }
}
