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
import org.wso2.xkms2.ReissueRequest;
import org.wso2.xkms2.XKMSException;
import org.wso2.xkms2.XKRRSTestCase;

public class ReissueRequestBuilderTest extends XKRRSTestCase {

    public ReissueRequestBuilderTest() {
        super("ReissueRequestBuilderTest");
    }

    public void testBuild() throws XKMSException {
        OMElement reissueRequestElem = getResourceAsElement("T3_ReissueRequest-http.xml");
        ReissueRequest reissueRequest = (ReissueRequest) ReissueRequestBuilder.INSTANCE
                .buildElement(reissueRequestElem);
        assertNotNull(reissueRequest);
    }
}
