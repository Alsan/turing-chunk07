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

import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.axiom.util.base64.Base64Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oauth.cache.CacheKey;
import org.wso2.carbon.identity.oauth.cache.OAuthCacheKey;
import org.wso2.carbon.identity.oauth.config.OAuthServerConfiguration;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.dto.OAuth2AccessTokenReqDTO;
import org.wso2.carbon.identity.oauth2.dto.OAuth2AccessTokenRespDTO;
import org.wso2.carbon.identity.oauth2.model.AccessTokenDO;
import org.wso2.carbon.identity.oauth2.model.RefreshTokenValidationDataDO;
import org.wso2.carbon.identity.oauth2.token.OAuthTokenReqMessageContext;
import org.wso2.carbon.identity.oauth2.util.OAuth2Constants;
import org.wso2.carbon.identity.oauth2.util.OAuth2Util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Grant Type handler for Grant Type refresh_token which is used to get a new access token.
 */
public class RefreshGrantTypeHandler extends AbstractAuthorizationGrantHandler {

    private static Log log = LogFactory.getLog(RefreshGrantTypeHandler.class);
    private static final String PREV_ACCESS_TOKEN = "previousAccessToken";

    public RefreshGrantTypeHandler() throws IdentityOAuth2Exception {
        super();
    }

    @Override
    public boolean validateGrant(OAuthTokenReqMessageContext tokReqMsgCtx)
            throws IdentityOAuth2Exception {
        OAuth2AccessTokenReqDTO tokenReqDTO = tokReqMsgCtx.getOauth2AccessTokenReqDTO();

        String refreshToken = tokenReqDTO.getRefreshToken();

        String preprocessedRefreshToken = tokenPersistencePreprocessor
                .getPreprocessedToken(refreshToken);

        RefreshTokenValidationDataDO validationDataDO = tokenMgtDAO.validateRefreshToken(
                tokenReqDTO.getClientId(),
                preprocessedRefreshToken);

        if (validationDataDO.getAccessToken() == null) {
            log.debug("Invalid Refresh Token provided for Client with " +
                    "Client Id : " + tokenReqDTO.getClientId());
            return false;
        }

        if (validationDataDO.getRefreshTokenState() != null
                && (!validationDataDO.getRefreshTokenState().equals( "ACTIVE") &&
                !validationDataDO.getRefreshTokenState().equals("EXPIRED"))) {
            log.debug("Refresh Token is not in 'ACTIVE' or 'EXPIRED' state for Client with " +
                    "Client Id : " + tokenReqDTO.getClientId() + " " +
                    "Refresh Token: " + tokenReqDTO.getRefreshToken());
            return false;
        }

        if (log.isDebugEnabled()) {
            log.debug("Refresh token validation successful for " +
                    "Client id : " + tokenReqDTO.getClientId() +
                    ", Authorized User : " + validationDataDO.getAuthorizedUser() +
                    ", Token Scope : " + OAuth2Util.buildScopeString(validationDataDO.getScope()));
        }

        tokReqMsgCtx.setAuthorizedUser(validationDataDO.getAuthorizedUser());
        tokReqMsgCtx.setScope(validationDataDO.getScope());
        // Store the old access token as a OAuthTokenReqMessageContext property, this is already
        // a preprocessed token.
        tokReqMsgCtx.addProperty(PREV_ACCESS_TOKEN, validationDataDO.getAccessToken());
        return true;
    }

    @Override
    public OAuth2AccessTokenRespDTO issue(OAuthTokenReqMessageContext tokReqMsgCtx)
            throws IdentityOAuth2Exception {
        OAuth2AccessTokenRespDTO tokenRespDTO = new OAuth2AccessTokenRespDTO();
        OAuth2AccessTokenReqDTO oauth2AccessTokenReqDTO = tokReqMsgCtx.getOauth2AccessTokenReqDTO();

        String accessToken;
        String refreshToken;
        String userStoreDomain = null;

        try {
            accessToken = oauthIssuerImpl.accessToken();
            refreshToken = oauthIssuerImpl.refreshToken();
        } catch (OAuthSystemException e) {
            throw new IdentityOAuth2Exception("Error when generating the tokens.", e);
        }

        if(OAuth2Util.checkUserNameAssertionEnabled()) {
            String userName = tokReqMsgCtx.getAuthorizedUser();
            //use ':' for token & userStoreDomain separation
            String accessTokenStrToEncode = accessToken + ":" + userName;
            accessToken = Base64Utils.encode(accessTokenStrToEncode.getBytes());

            String refreshTokenStrToEncode = refreshToken + ":" + userName;
            refreshToken = Base64Utils.encode(refreshTokenStrToEncode.getBytes());

            //logic to store access token into different tables when multiple user stores are configured.
            if (OAuth2Util.checkAccessTokenPartitioningEnabled()) {
                userStoreDomain = OAuth2Util.getUserStoreDomainFromUserId(userName);
            }
        }

        boolean isValidGrant = this.validateGrant(tokReqMsgCtx);
        if (!isValidGrant) {
            throw new IdentityOAuth2Exception("Provided refresh token is invalid: "+
                    oauth2AccessTokenReqDTO.getRefreshToken());
        }

        Timestamp timestamp = new Timestamp(new Date().getTime());

        // Default Validity Period (in seconds)
        long validityPeriod = OAuthServerConfiguration.getInstance()
                .getDefaultAccessTokenValidityPeriodInSeconds();

        // if a VALID validity period is set through the callback, then use it
        long callbackValidityPeriod = tokReqMsgCtx.getValidityPeriod();
        if ((callbackValidityPeriod != OAuth2Constants.UNASSIGNED_VALIDITY_PERIOD)
                && callbackValidityPeriod > 0) {
            validityPeriod = callbackValidityPeriod;
        }

        //String refreshToken = oauth2AccessTokenReqDTO.getRefreshToken();

        String preprocessedAccessToken = tokenPersistencePreprocessor
                .getPreprocessedToken(accessToken);
        String preprocessedRefreshToken = tokenPersistencePreprocessor
                .getPreprocessedToken(refreshToken);

        AccessTokenDO accessTokenDO = new AccessTokenDO(oauth2AccessTokenReqDTO.getClientId(),tokReqMsgCtx.getAuthorizedUser(),
                tokReqMsgCtx.getScope(), timestamp, validityPeriod);
        accessTokenDO.setTokenState(OAuth2Constants.TokenStates.TOKEN_STATE_ACTIVE);
        accessTokenDO.setRefreshToken(preprocessedRefreshToken);
        accessTokenDO.setAccessToken(preprocessedAccessToken);

        String clientId = oauth2AccessTokenReqDTO.getClientId();
        String oldAccessToken = (String)tokReqMsgCtx.getProperty(PREV_ACCESS_TOKEN);

        String consumerKey = tokReqMsgCtx.getOauth2AccessTokenReqDTO().getClientId();
        String authorizedUser = tokReqMsgCtx.getAuthorizedUser();
        // set the previous access token state to "INACTIVE"
        tokenMgtDAO.setAccessTokenState(consumerKey, authorizedUser, "INACTIVE",
                UUID.randomUUID().toString(), 
                OAuth2Util.USER_TYPE_FOR_USER_TOKEN, userStoreDomain);

        // store the new access token
        tokenMgtDAO.storeAccessToken(preprocessedAccessToken, clientId, accessTokenDO, userStoreDomain);

        // Remove the previous access token (this is already a preprocessed token)
        //tokenMgtDAO.cleanUpAccessToken(oldAccessToken);

        //remove the previous access token from cache and add the access token info to the cache,
        // if it's enabled.
        if(cacheEnabled){
            CacheKey cacheKey = new OAuthCacheKey(consumerKey + ":" + authorizedUser);
            // Remove the old access token from the cache
            oauthCache.clearCacheEntry(cacheKey);
            // Add new access token to the cache
            oauthCache.addToCache(cacheKey, accessTokenDO);

            if(log.isDebugEnabled()){
                log.debug("Access Token info for the refresh token was added to the cache for " +
                        "the client id : " + clientId + ". Old access token entry was " +
                        "also removed from the cache.");
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Persisted an access token for the refresh token, " +
                    "Client ID : " + clientId +
                    "authorized user : " + tokReqMsgCtx.getAuthorizedUser() +
                    "timestamp : " + timestamp +
                    "validity period : " + validityPeriod +
                    "scope : " + OAuth2Util.buildScopeString(tokReqMsgCtx.getScope()) +
                    "Token State : " + OAuth2Constants.TokenStates.TOKEN_STATE_ACTIVE);
        }

        tokenRespDTO.setAccessToken(accessToken);
        tokenRespDTO.setRefreshToken(refreshToken);
        tokenRespDTO.setExpiresIn(validityPeriod);
        return tokenRespDTO;
    }
}
