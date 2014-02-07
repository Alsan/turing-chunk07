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
package org.wso2.xkms2;
/*
 * 
 */

public class InvalidReason {

    public static final InvalidReason ISSUER_TRUST =
            new InvalidReason(ReasonOpenEnum.ISSUER_TRUST);
    public static final InvalidReason REVOCATION_STATUS =
            new InvalidReason(ReasonOpenEnum.REVOCATION_STATUS);
    public static final InvalidReason VALIDITY_INTERVAL =
            new InvalidReason(ReasonOpenEnum.VALIDITY_INTERVAL);
    public static final InvalidReason SIGNATURE =
            new InvalidReason(ReasonOpenEnum.SIGNATURE);

    private String anyURI;

    private InvalidReason(String anyURI) {
        this.anyURI = anyURI;
    }

    public String getAnyURI() {
        return anyURI;
    }

    public static InvalidReason validate(String anyURI) throws XKMSException {
        return new InvalidReason(ReasonOpenEnum.validate(anyURI));
    }
}
