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

package org.wso2.carbon.identity.oauth2.token.handlers.grant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.oauth.internal.OAuthComponentServiceHolder;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.dto.OAuth2AccessTokenReqDTO;
import org.wso2.carbon.identity.oauth2.token.OAuthTokenReqMessageContext;
import org.wso2.carbon.identity.oauth2.util.OAuth2Util;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.user.core.util.UserCoreUtil;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

/**
 * Handles the Password Grant Type of the OAuth 2.0 specification. Resource owner sends his
 * credentials in the token request which is validated against the corresponding user store.
 * Grant Type : password
 */
public class PasswordGrantHandler extends AbstractAuthorizationGrantHandler {

    private static Log log = LogFactory.getLog(PasswordGrantHandler.class);

    public PasswordGrantHandler() throws IdentityOAuth2Exception {
        super();
    }

    @Override
    public boolean validateGrant(OAuthTokenReqMessageContext tokReqMsgCtx)
            throws IdentityOAuth2Exception {

        OAuth2AccessTokenReqDTO oAuth2AccessTokenReqDTO = tokReqMsgCtx.getOauth2AccessTokenReqDTO();
        String username = oAuth2AccessTokenReqDTO.getResourceOwnerUsername();
        String credentialType = oAuth2AccessTokenReqDTO.getCredentialType();
        String tenantDomain = MultitenantUtils.getTenantDomain(username);
        String tenantAwareUserName = MultitenantUtils.getTenantAwareUsername(username);
        username = tenantAwareUserName + "@" + tenantDomain;
        username = username.toLowerCase();
        int tenantId;
        try {
            tenantId = IdentityUtil.getTenantIdOFUser(username);
        } catch (IdentityException e) {
            throw new IdentityOAuth2Exception(e.getMessage(), e);
        }

        // tenantId == -1, means an invalid tenant.
        if(tenantId == -1){
            if (log.isDebugEnabled()) {
                log.debug("Token request with Password Grant Type for an invalid tenant : " +
                        MultitenantUtils.getTenantDomain(username));
            }
            return false; 
        }

        RealmService realmService = OAuthComponentServiceHolder.getRealmService();
        boolean authStatus;
        try {
            UserStoreManager userStoreManager = realmService.getTenantUserRealm(tenantId).getUserStoreManager();
            if(credentialType != null && !credentialType.equals("")) { // TODO : a hack to get multiple cred working
            	username = credentialType + ":" + username;
            }
            authStatus = userStoreManager.authenticate(tenantAwareUserName, oAuth2AccessTokenReqDTO.getResourceOwnerPassword());

            if(log.isDebugEnabled()){
                log.debug("Token request with Password Grant Type received. " +
                        "Username : " + username +
                        "Scope : " + OAuth2Util.buildScopeString(oAuth2AccessTokenReqDTO.getScope()) +
                        ", Authentication State : " + authStatus);
            }

        } catch (UserStoreException e) {
            throw new IdentityOAuth2Exception("Error when authenticating the user credentials.", e);
        }
        if(authStatus){
            if(username.indexOf(CarbonConstants.DOMAIN_SEPARATOR) < 0){
                if(UserCoreUtil.getDomainFromThreadLocal() != null && !UserCoreUtil.getDomainFromThreadLocal().equals("")){
                    tokReqMsgCtx.setAuthorizedUser(UserCoreUtil.getDomainFromThreadLocal() + CarbonConstants.DOMAIN_SEPARATOR + username);
                } else {
                    tokReqMsgCtx.setAuthorizedUser(username);
                }
            }else if(username.indexOf(CarbonConstants.DOMAIN_SEPARATOR) > 0){
                tokReqMsgCtx.setAuthorizedUser(username);
            }
        }
        tokReqMsgCtx.setScope(oAuth2AccessTokenReqDTO.getScope());
        return authStatus;
    }

}