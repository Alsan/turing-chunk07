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

package org.wso2.carbon.identity.oauth;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.core.AbstractAdmin;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.model.OAuthAppDO;
import org.wso2.carbon.identity.oauth.cache.BaseCache;
import org.wso2.carbon.identity.oauth.cache.OAuthCache;
import org.wso2.carbon.identity.oauth.cache.OAuthCacheKey;
import org.wso2.carbon.identity.oauth.common.OAuth2ErrorCodes;
import org.wso2.carbon.identity.oauth.common.exception.InvalidOAuthClientException;
import org.wso2.carbon.identity.oauth.config.OAuthServerConfiguration;
import org.wso2.carbon.identity.oauth.dao.OAuthAppDAO;
import org.wso2.carbon.identity.oauth.dto.OAuthConsumerAppDTO;
import org.wso2.carbon.identity.oauth.dto.OAuthRevocationRequestDTO;
import org.wso2.carbon.identity.oauth.dto.OAuthRevocationResponseDTO;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.dao.TokenMgtDAO;
import org.wso2.carbon.utils.ServerConstants;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class OAuthAdminService extends AbstractAdmin {

    protected Log log = LogFactory.getLog(OAuthAdminService.class);

    private static List<String> allowedGrants = null;

    private BaseCache<String, OAuthAppDO> appInfoCache = new BaseCache<String,OAuthAppDO>("AppInfoCache");

    /**
     * Registers an consumer secret against the logged in user. A given user can only have a single
     * consumer secret at a time. Calling this method again and again will update the existing
     * consumer secret key.
     *
     * @return An array containing the consumer key and the consumer secret correspondingly.
     * @throws Exception    Error when persisting the data in the persistence store.
     */
    public String[] registerOAuthConsumer() throws Exception {

        String loggedInUser = getLoggedInUser();

        if (log.isDebugEnabled()) {
            log.debug("Adding a consumer secret for the logged in user " + loggedInUser);
        }

        String tenantUser = MultitenantUtils.getTenantAwareUsername(loggedInUser);
        int tenantId = CarbonContext.getCurrentContext().getTenantId();
        OAuthAppDAO dao = new OAuthAppDAO();
        return dao.addOAuthConsumer(tenantUser, tenantId);
    }

    /**
     * Get all registered OAuth applications for the logged in user.
     *
     * @return  An array of <code>OAuthConsumerAppDTO</code> objecting containing the application
     * information of the user
     * @throws Exception    Error when reading the data from the persistence store.
     */
    public OAuthConsumerAppDTO[] getAllOAuthApplicationData() throws Exception {

        String userName = getLoggedInUser();
        OAuthConsumerAppDTO[] dtos = new OAuthConsumerAppDTO[0];

        if (userName == null) {
            if (log.isErrorEnabled()) {
                log.debug("User not logged in");
            }
            throw new Exception("User not logged in");
        }

        String tenantUser = MultitenantUtils.getTenantAwareUsername(userName);
        int tenantId = CarbonContext.getCurrentContext().getTenantId();
        OAuthAppDAO dao = new OAuthAppDAO();
        OAuthAppDO[] apps = dao.getOAuthConsumerAppsOfUser(tenantUser, tenantId);
        if (apps != null && apps.length > 0) {
            dtos = new OAuthConsumerAppDTO[apps.length];
            OAuthConsumerAppDTO dto = null;
            OAuthAppDO app = null;
            for (int i = 0; i < apps.length; i++) {
                app = apps[i];
                dto = new OAuthConsumerAppDTO();
                dto.setApplicationName(app.getApplicationName());
                dto.setCallbackUrl(app.getCallbackUrl());
                dto.setOauthConsumerKey(app.getOauthConsumerKey());
                dto.setOauthConsumerSecret(app.getOauthConsumerSecret());
                dto.setOAuthVersion(app.getOauthVersion());
                dto.setGrantTypes(app.getGrantTypes());
                dtos[i] = dto;
            }
        }
        return dtos;
    }

    /**
     * Get OAuth application data by the consumer key.
     *
     * @param consumerKey Consumer Key
     * @return  <code>OAuthConsumerAppDTO</code> with application information
     * @throws Exception Error when reading application information from persistence store.
     */
    public OAuthConsumerAppDTO getOAuthApplicationData(String consumerKey) throws Exception {
        OAuthConsumerAppDTO dto = new OAuthConsumerAppDTO();
        OAuthAppDAO dao = new OAuthAppDAO();
        OAuthAppDO app = dao.getAppInformation(consumerKey);
        if (app != null) {
            dto.setApplicationName(app.getApplicationName());
            dto.setCallbackUrl(app.getCallbackUrl());
            dto.setOauthConsumerKey(app.getOauthConsumerKey());
            dto.setOauthConsumerSecret(app.getOauthConsumerSecret());
            dto.setOAuthVersion(app.getOauthVersion());
            dto.setGrantTypes(app.getGrantTypes());
        }
        return dto;
    }

    /**
     * Registers an OAuth consumer application.
     *
     * @param application   <code>OAuthConsumerAppDTO</code> with application information
     * @throws Exception    Error when persisting the application information to the persistence store
     */
    public void registerOAuthApplicationData(OAuthConsumerAppDTO application) throws Exception {
        String userName = getLoggedInUser();
        if (userName != null) {
            String tenantUser = MultitenantUtils.getTenantAwareUsername(userName);
            int tenantId = CarbonContext.getCurrentContext().getTenantId();

            OAuthAppDAO dao = new OAuthAppDAO();
            OAuthAppDO app = new OAuthAppDO();
            if (application != null) {
                app.setApplicationName(application.getApplicationName());
                if(application.getGrantTypes().contains("authorization_code") || application.getGrantTypes().contains("implicit")){
                    if(application.getCallbackUrl() == null || application.getCallbackUrl().equals("")){
                        throw new IdentityOAuthAdminException("Callback Url is required for Code or Implicit grant types");
                    }
                }
                app.setCallbackUrl(application.getCallbackUrl());
                if (application.getOauthConsumerKey() == null) {
                    app.setOauthConsumerKey(OAuthUtil.getRandomNumber());
                    app.setOauthConsumerSecret(OAuthUtil.getRandomNumber());
                } else {
                    app.setOauthConsumerKey(application.getOauthConsumerKey());
                    app.setOauthConsumerSecret(application.getOauthConsumerSecret());
                }
                app.setUserName(tenantUser);
                app.setTenantId(tenantId);
                if (application.getOAuthVersion() != null) {
                    app.setOauthVersion(application.getOAuthVersion());
                } else {   // by default, assume OAuth 2.0, if it is not set.
                    app.setOauthVersion(OAuthConstants.OAuthVersions.VERSION_2);
                }
                if(OAuthConstants.OAuthVersions.VERSION_2.equals(application.getOAuthVersion())){
                    List<String> allowedGrants = new ArrayList<String>(Arrays.asList(getAllowedGrantTypes()));
                    String[] requestGrants = application.getGrantTypes().split("\\s");
                    for(String requestedGrant:requestGrants){
                        if(requestedGrant.trim().equals("")){
                            continue;
                        }
                        if(!allowedGrants.contains(requestedGrant)){
                            throw new Exception(requestedGrant + " not allowed");
                        }
                    }
                    app.setGrantTypes(application.getGrantTypes());
                }
                dao.addOAuthApplication(app);
                if(OAuthServerConfiguration.getInstance().isCacheEnabled()) {
                    appInfoCache.addToCache(app.getOauthConsumerKey(), app);
                }
            }
        }
    }

    /**
     * Update existing consumer application.
     *
     * @param consumerAppDTO <code>OAuthConsumerAppDTO</code> with updated application information
     * @throws IdentityOAuthAdminException Error when updating the underlying identity persistence store.
     */
    public void updateConsumerApplication(OAuthConsumerAppDTO consumerAppDTO) throws Exception {
        String userName = getLoggedInUser();
        String tenantAwareUsername = MultitenantUtils.getTenantAwareUsername(userName);
        int tenantId = CarbonContext.getCurrentContext().getTenantId();
        OAuthAppDAO dao = new OAuthAppDAO();
        OAuthAppDO oauthappdo = new OAuthAppDO();
        oauthappdo.setUserName(tenantAwareUsername);
        oauthappdo.setTenantId(tenantId);
        oauthappdo.setOauthConsumerKey(consumerAppDTO.getOauthConsumerKey());
        oauthappdo.setOauthConsumerSecret(consumerAppDTO.getOauthConsumerSecret());
        oauthappdo.setCallbackUrl(consumerAppDTO.getCallbackUrl());
        oauthappdo.setApplicationName(consumerAppDTO.getApplicationName());
        if(OAuthConstants.OAuthVersions.VERSION_2.equals(consumerAppDTO.getOAuthVersion())){
            List<String> allowedGrants = new ArrayList<String>(Arrays.asList(getAllowedGrantTypes()));
            String[] requestGrants = consumerAppDTO.getGrantTypes().split("\\s");
            for(String requestedGrant:requestGrants){
                if(requestedGrant.trim().equals("")){
                    continue;
                }
                if(!allowedGrants.contains(requestedGrant)){
                    throw new Exception(requestedGrant + " not allowed");
                }
            }
            oauthappdo.setGrantTypes(consumerAppDTO.getGrantTypes());
        }
        dao.updateConsumerApplication(oauthappdo);
        if(OAuthServerConfiguration.getInstance().isCacheEnabled()){
            appInfoCache.addToCache(oauthappdo.getOauthConsumerKey(), oauthappdo);
        }
    }

    /**
     * Removes an OAuth consumer application.
     *
     * @param consumerKey   Consumer Key
     * @throws Exception    Error when removing the consumer information from the database.
     */
    public void removeOAuthApplicationData(String consumerKey) throws Exception {
        OAuthAppDAO dao = new OAuthAppDAO();
        dao.removeConsumerApplication(consumerKey);
        // remove client credentials from cache
        if(OAuthServerConfiguration.getInstance().isCacheEnabled()){
            OAuthCache.getInstance().clearCacheEntry(new OAuthCacheKey(consumerKey));
            appInfoCache.clearCacheEntry(consumerKey);
            if (log.isDebugEnabled()) {
                log.debug("Client credentials are removed from the cache.");
            }
        }
    }

    private String getLoggedInUser() {
        MessageContext msgContext = MessageContext.getCurrentMessageContext();
        HttpServletRequest request = (HttpServletRequest) msgContext
                .getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
        HttpSession httpSession = request.getSession(false);

        if (httpSession != null) {
            return (String) httpSession.getAttribute(ServerConstants.USER_LOGGED_IN);
        }
        return null;
    }

    /**
     * Get apps that are authorized by the given user
     * @return OAuth applications authorized by the user that have tokens in ACTIVE or EXPIRED state
     */
    public OAuthConsumerAppDTO[] getAppsAuthorizedByUser() throws IdentityOAuth2Exception {

        TokenMgtDAO tokenMgtDAO = new TokenMgtDAO();
        OAuthAppDAO appDAO = new OAuthAppDAO();

        String tenantDomain = PrivilegedCarbonContext.getCurrentContext().getTenantDomain();
        String tenantAwareUserName = PrivilegedCarbonContext.getCurrentContext().getUsername();
        String username = tenantAwareUserName + "@" + tenantDomain;
        username = username.toLowerCase();
        OAuthAppDO[] appDOs = tokenMgtDAO.getAppsAuthorizedByUser(username);
        OAuthConsumerAppDTO[] appDTOs = new OAuthConsumerAppDTO[appDOs.length];
        for(int i = 0; i < appDTOs.length ; i++){
            try {
                OAuthAppDO appDO = appDAO.getAppInformation(appDOs[i].getOauthConsumerKey());
                OAuthConsumerAppDTO appDTO = new OAuthConsumerAppDTO();
                appDTO.setApplicationName(appDO.getApplicationName());
                appDTO.setUsername(appDO.getUserName());
                appDTO.setGrantTypes(appDO.getGrantTypes());
                appDTOs[i] = appDTO;
            } catch (IdentityOAuthAdminException e) {
                log.error(e.getMessage());
            } catch (InvalidOAuthClientException e) {
                log.error(e.getMessage());
            }
        }
        return appDTOs;
    }

    /**
     * Revoke authorization for OAuth apps by resource owners
     * @param revokeRequestDTO DTO representing authorized user and apps[]
     * @return revokeRespDTO DTO representing success or failure message
     */
    public OAuthRevocationResponseDTO revokeAuthzForAppsByResoureOwner(OAuthRevocationRequestDTO revokeRequestDTO) {

        TokenMgtDAO tokenMgtDAO = new TokenMgtDAO();
        try{
            if(revokeRequestDTO.getApps() != null && revokeRequestDTO.getApps().length > 0) {
                String tenantDomain = PrivilegedCarbonContext.getCurrentContext().getTenantDomain();
                String tenantAwareUserName = PrivilegedCarbonContext.getCurrentContext().getUsername();
                String userName = tenantAwareUserName + "@" + tenantDomain;
                userName = userName.toLowerCase();
                tokenMgtDAO.revokeTokensByResourceOwner(revokeRequestDTO.getApps(), userName);
            } else {
                OAuthRevocationResponseDTO revokeRespDTO = new OAuthRevocationResponseDTO();
                revokeRespDTO.setError(true);
                revokeRespDTO.setErrorCode(OAuth2ErrorCodes.INVALID_REQUEST);
                revokeRespDTO.setErrorMsg("Invalid revocation request");
                return revokeRespDTO;
            }
            return new OAuthRevocationResponseDTO();
        } catch (IdentityException e) {
            log.error(e.getMessage(), e);
            OAuthRevocationResponseDTO revokeRespDTO = new OAuthRevocationResponseDTO();
            revokeRespDTO.setError(true);
            revokeRespDTO.setErrorCode(OAuth2ErrorCodes.SERVER_ERROR);
            revokeRespDTO.setErrorMsg("Error occurred while revoking authorization grant for applications");
            return revokeRespDTO;
        } catch (InvalidOAuthClientException e) {
            log.error(e.getMessage(), e);
            OAuthRevocationResponseDTO revokeRespDTO = new OAuthRevocationResponseDTO();
            revokeRespDTO.setError(true);
            revokeRespDTO.setErrorCode(OAuth2ErrorCodes.SERVER_ERROR);
            revokeRespDTO.setErrorMsg("Error occurred while revoking authorization grant for applications");
            return revokeRespDTO;
        }
    }

    public String[] getAllowedGrantTypes(){
        if(allowedGrants == null){
            allowedGrants = OAuthServerConfiguration.getInstance().getSupportedGrantTypes();
            if(OAuthServerConfiguration.getInstance().getSupportedResponseTypes().contains("token")){
                allowedGrants.add("implicit");
            }
        }
        return allowedGrants.toArray(new String[allowedGrants.size()]);
    }
}
