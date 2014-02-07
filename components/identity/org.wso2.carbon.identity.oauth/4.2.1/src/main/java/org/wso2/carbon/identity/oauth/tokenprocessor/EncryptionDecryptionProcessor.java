/*
*Copyright (c) 2005-2013, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.oauth.tokenprocessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.core.util.CryptoException;
import org.wso2.carbon.core.util.CryptoUtil;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;

/**
 * An implementation of <Code>TokenPersistencePreprocessor</Code> which is used when storing encrypted tokens.
 * @see TokenPersistenceProcessor
 */
public class EncryptionDecryptionProcessor implements TokenPersistenceProcessor {

    protected Log log = LogFactory.getLog(EncryptionDecryptionProcessor.class);

    public String getPreprocessedToken(String processedToken) throws IdentityOAuth2Exception {
        try {
            return new String(CryptoUtil.getDefaultCryptoUtil().base64DecodeAndDecrypt(processedToken));
        } catch (CryptoException e) {
            log.debug(e.getMessage(), e);
            throw new IdentityOAuth2Exception(e.getMessage());
        }
    }

    @Override
    public String getProcessedToken(String preprocessedToken) throws IdentityOAuth2Exception {
        try {
            return CryptoUtil.getDefaultCryptoUtil().encryptAndBase64Encode(preprocessedToken.getBytes());
        } catch (CryptoException e) {
            log.debug(e.getMessage(), e);
            throw new IdentityOAuth2Exception(e.getMessage());
        }
    }
}
