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

import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;

/**
 * <Code>TokenPersistenceProcessor</Code> is used to modify the token before it is stored
 * in the database. For instance, tokens need to be encrypted, hashed, etc before storing in the
 * database. There can be one active <Code>TokenPersistenceProcessor</Code> at a given runtime
 * and it is configured through the identity.xml.
 * @see PlainTextProcessor
 */
public interface TokenPersistenceProcessor {

    /**
     * get the preprocessed token value for a given token.
     * @param processedToken plain token
     * @return preprocessed token.
     */
    public String getPreprocessedToken(String processedToken) throws IdentityOAuth2Exception;

    /**
     * get the processed token value for a given plain token.
     * @param plainToken plain token
     * @return preprocessed token.
     */
    public String getProcessedToken(String plainToken) throws IdentityOAuth2Exception;

}
