/*
*  Licensed to the Apache Software Foundation (ASF) under one
*  or more contributor license agreements.  See the NOTICE file
*  distributed with this work for additional information
*  regarding copyright ownership.  The ASF licenses this file
*  to you under the Apache License, Version 2.0 (the
*  "License"); you may not use this file except in compliance
*  with the License.  You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/
package org.wso2.carbon.identity.oauth.util;

import org.apache.axiom.util.base64.Base64Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.base.ServerConfiguration;
import org.wso2.carbon.identity.oauth.common.exception.OAuthClientException;

public class OAuthEndPointClientUtil {

    private static Log log = LogFactory.getLog(OAuthEndPointClientUtil.class);

    /**
     * Extracts the username and password info from the HTTP Authorization Header
     * @param authorizationHeader "Basic " + base64encode(username + ":" + password)
     * @return String array with username and password.
     * @throws org.wso2.carbon.identity.base.IdentityException If the decoded data is null.
     */
    public static String[] extractCredentialsFromAuthzHeader(String authorizationHeader)
            throws OAuthClientException {
        String[] splitValues = authorizationHeader.trim().split(" ");
        byte[] decodedBytes = Base64Utils.decode(splitValues[1].trim());
        if (decodedBytes != null) {
            String userNamePassword = new String(decodedBytes);
            return userNamePassword.split(":");
        } else {
            String errMsg = "Error decoding authorization header. " +
                    "Could not retrieve user name and password.";
            log.debug(errMsg);
            throw new OAuthClientException(errMsg);
        }
    }

    public static String getRealmInfo(){
        ServerConfiguration serverConfig = ServerConfiguration.getInstance();
        String hostname = serverConfig.getFirstProperty("HostName");
        return "Basic realm=" + hostname;
    }

}
