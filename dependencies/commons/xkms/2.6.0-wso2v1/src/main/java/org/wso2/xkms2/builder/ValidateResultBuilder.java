/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.xkms2.builder;

import org.apache.axiom.om.OMElement;
import org.wso2.xkms2.*;

import javax.xml.namespace.QName;
import java.util.Iterator;
/*
 * 
 */

public class ValidateResultBuilder extends ResultTypeBuilder {
    public static final ValidateResultBuilder INSTANCE = new ValidateResultBuilder();
    
    private ValidateResult validateResult;

    private ValidateResultBuilder() {        
    }

    public XKMSElement buildElement(OMElement element) throws XKMSException {
        validateResult = new ValidateResult();
        
        QName qName = element.getQName();
        if (!(qName.getLocalPart().equals("ValidateResult") &&
              qName.getNamespaceURI().equals(XKMS2Constants.XKMS2_NS))) {
            throw new XKMSException("ValidateResult element is not found");

        }
        super.buildElement(element, validateResult);

        for (Iterator iterator =
                element.getChildrenWithName(new QName(XKMS2Constants.XKMS2_NS, "KeyBinding"));
             iterator.hasNext();) {
            OMElement e = (OMElement)iterator.next();
            validateResult.addKeyBinding((KeyBinding) KeyBindingBuilder.INSTANCE.buildElement(e));

        }

        return validateResult;

    }

}
