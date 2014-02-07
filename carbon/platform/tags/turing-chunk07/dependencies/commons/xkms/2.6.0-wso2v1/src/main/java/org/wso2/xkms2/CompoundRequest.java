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
package org.wso2.xkms2;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;

import java.util.ArrayList;
import java.util.List;

public class CompoundRequest extends RequestAbstractType {
    
    private List requestList = null;
    
    public void addReqest(RequestAbstractType request) {
        if (requestList == null) {
            requestList = new ArrayList();
        }
        requestList.add(request);
    }
    
    public List getRequest() {
        return requestList;
    }

    public OMElement serialize(OMFactory factory) throws XKMSException {
        return null;  //TODO Saminda or Sanka 
    }
}
