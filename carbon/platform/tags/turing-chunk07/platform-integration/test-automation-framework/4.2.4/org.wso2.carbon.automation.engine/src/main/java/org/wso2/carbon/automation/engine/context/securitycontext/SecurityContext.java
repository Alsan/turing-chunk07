/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.carbon.automation.engine.context.securitycontext;

import org.wso2.carbon.automation.engine.context.AutomationContext;

import java.util.LinkedHashMap;

/*
 * Represents the data structure for Cluster node in automation.xml
 */
public class SecurityContext extends AutomationContext {
    private LinkedHashMap<String, KeyStore> keyStoreList = new LinkedHashMap<String, KeyStore>();
    private LinkedHashMap<String, TrustStore> trustStoreList = new LinkedHashMap<String, TrustStore>();

    public LinkedHashMap<String, KeyStore> getAllKeyStores() {
        return keyStoreList;
    }

    public void setKeyStore(LinkedHashMap<String, KeyStore> keyStoreList) {
        this.keyStoreList = keyStoreList;
    }

    public LinkedHashMap<String, TrustStore> getAllTrustStores() {
        return trustStoreList;
    }

    public void setTrustStore(LinkedHashMap<String, TrustStore> trustStoreList) {
        this.trustStoreList = trustStoreList;
    }

    public KeyStore getKeyStore(String keyStoreId) {
        return keyStoreList.get(keyStoreId);
    }

    public TrustStore getTrustStore(String trustStoreId) {
        return trustStoreList.get(trustStoreId);
    }
}
