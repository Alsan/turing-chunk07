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
package org.wso2.carbon.automation.engine.frameworkutils;

import org.wso2.carbon.automation.engine.FrameworkConstants;
import org.wso2.carbon.automation.engine.FrameworkPathUtil;
import org.wso2.carbon.automation.engine.context.InitialContextPublisher;
import org.wso2.carbon.automation.engine.context.securitycontext.KeyStore;

import java.io.File;

public class FrameworkUtil {
    public static void setKeyStoreProperties() {
        KeyStore keyStore = InitialContextPublisher.getContext().getSecurityContext().
                getKeyStore(FrameworkConstants.DEFAULT_KEY_STORE);
        String keyStoreFileName = keyStore.getFileName();
        File keyStoreFile = new File(String.format("%s%s", FrameworkPathUtil.
                getSystemResourceLocation(), keyStoreFileName));
        System.setProperty("javax.net.ssl.trustStore", keyStoreFile.getAbsolutePath());
        System.setProperty("javax.net.ssl.trustStorePassword", keyStore.getPassword());
        System.setProperty("javax.net.ssl.trustStoreType", keyStore.getType());
    }
}
