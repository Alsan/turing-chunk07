/*
 * Copyright 2001-2004 The Apache Software Foundation.
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
import org.wso2.xkms2.RecoverKeyBinding;
import org.wso2.xkms2.XKMSElement;
import org.wso2.xkms2.XKMSException;

public class RecoverKeyBindingBuilder implements ElementBuilder {

    public static final RecoverKeyBindingBuilder INSTANCE = new RecoverKeyBindingBuilder();

    private RecoverKeyBindingBuilder() {
    }

    public XKMSElement buildElement(OMElement element) throws XKMSException {
        RecoverKeyBinding recoverKeyBinding = new RecoverKeyBinding();
        KeyBindingBuilder.INSTANCE.buildElement(element, recoverKeyBinding);
        return recoverKeyBinding;
    }
}
