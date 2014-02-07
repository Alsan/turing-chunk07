/*
 *  Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.entitlement.mediator.callback;

import java.security.cert.X509Certificate;

import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;

public class X509EntitlementCallbackHandler extends EntitlementCallbackHandler {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.wso2.carbon.identity.entitlement.mediator.callback.EntitlementCallbackHandler#getUserName(org.
     * apache.synapse.MessageContext)
     */
    public String getUserName(MessageContext synCtx) {
        org.apache.axis2.context.MessageContext msgContext;
        Axis2MessageContext axis2Msgcontext = null;
        axis2Msgcontext = (Axis2MessageContext) synCtx;
        msgContext = axis2Msgcontext.getAxis2MessageContext();
        // For WS-Security
        X509Certificate cert = (X509Certificate) msgContext.getProperty("X509Certificate");
        if(cert == null){
            // For mutual authentication
            Object sslCertObject = msgContext.getProperty("ssl.client.auth.cert.X509");
            javax.security.cert.X509Certificate[] certs = (javax.security.cert.X509Certificate[]) sslCertObject;
            if(certs != null && certs.length > 0) {
                return certs[0].getSubjectDN().getName();
            }
        } else {
            return cert.getSubjectDN().getName();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.wso2.carbon.identity.entitlement.mediator.callback.EntitlementCallbackHandler#findEnvironment(
     * org.apache.synapse.MessageContext)
     */
    public String[] findEnvironment(MessageContext synCtx) {
        org.apache.axis2.context.MessageContext msgContext;
        Axis2MessageContext axis2Msgcontext = null;
        axis2Msgcontext = (Axis2MessageContext) synCtx;
        msgContext = axis2Msgcontext.getAxis2MessageContext();
        // For WS-Security
        X509Certificate cert = (X509Certificate) msgContext.getProperty("X509Certificate");
        if(cert == null){
            // For mutual authentication
            Object sslCertObject = msgContext.getProperty("ssl.client.auth.cert.X509");
            javax.security.cert.X509Certificate[] certs = (javax.security.cert.X509Certificate[]) sslCertObject;
            if(certs != null && certs.length > 0) {
                String issuer = certs[0].getIssuerDN().getName();
                String signatureAlgo = certs[0].getSigAlgName();
                return new String[] { "Issuer#" + issuer, "SignatureAlgo#" + signatureAlgo };
            }
        } else {
            String issuer = cert.getIssuerDN().getName();
            String signatureAlgo = cert.getSigAlgName();
            return new String[] { "Issuer#" + issuer, "SignatureAlgo#" + signatureAlgo };
        }
        return null;
    }
}
