/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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

package org.apache.ode.bpel.compiler.bom;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

/**
 * Assignment L/R-value defined in terms of a BPEL partner link.
 */
public class PartnerLinkVal extends ToFrom {
    public enum EndpointReference {
        MYROLE, PARTNERROLE;

        private static final Map<String, EndpointReference> __map = new HashMap<String, EndpointReference>();
        static {
            __map.put("myRole", MYROLE);
            __map.put("partnerRole", PARTNERROLE);
        }
    }

    public PartnerLinkVal(Element el) {
        super(el);
    }

    public String getPartnerLink() {
        return getAttribute("partnerLink", null);
    }

    public EndpointReference getEndpointReference() {
        return getAttribute("endpointReference", EndpointReference.__map, EndpointReference.MYROLE);
    }
}
