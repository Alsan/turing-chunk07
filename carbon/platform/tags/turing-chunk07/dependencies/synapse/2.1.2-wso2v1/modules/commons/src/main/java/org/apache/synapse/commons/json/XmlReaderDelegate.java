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
 * Detaches the special prefix applied by the JsonReaderDelegate.<br/>
 * Hence this returns the original key value that was read in.
 */
public class XmlReaderDelegate extends StreamReaderDelegate {
    private static final Log logger = LogFactory.getLog(XmlReaderDelegate.class.getName());

    public XmlReaderDelegate(XMLStreamReader reader) {
        super(reader);
        /** Possible reader implementations include;
            com.ctc.wstx.sr.ValidatingStreamReader
            de.odysseus.staxon.json.JsonXMLStreamReader
            com.sun.org.apache.xerces.internal.impl.XMLStreamReaderImpl
         */
        if (logger.isDebugEnabled()) {
            logger.debug("Setting XMLStreamReader " + reader.getClass().getName());
        }
    }

    public String getLocalName() {
        String localName = super.getLocalName();
        if (localName == null || "".equals(localName)) {
            return localName;
        }
        if (localName.charAt(0) == '_') {
            if (localName.startsWith(JsonReaderDelegate.PRECEDING_DIGIT)) {
                return localName.substring(JsonReaderDelegate.PRECEDING_DIGIT.length(),
                                           localName.length());
            }
        }
        return localName;
    }

    public QName getName() {
        QName qName = super.getName();
        String localName = qName.getLocalPart();
        if (localName == null || "".equals(localName)) {
            return qName;
        }
        if (localName.charAt(0) == '_') {
            if (localName.startsWith(JsonReaderDelegate.PRECEDING_DIGIT)) {
                localName =  localName.substring(JsonReaderDelegate.PRECEDING_DIGIT.length(),
                                                 localName.length());
                return new QName(qName.getNamespaceURI(), localName, qName.getPrefix());
            }
        }
        return qName;
    }
}
