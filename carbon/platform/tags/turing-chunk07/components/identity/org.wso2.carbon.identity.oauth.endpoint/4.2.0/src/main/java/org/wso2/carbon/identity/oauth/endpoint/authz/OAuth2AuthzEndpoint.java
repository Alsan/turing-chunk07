/*
 * Copyright (c) 2005-2013, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * 
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.identity.oauth.endpoint.authz;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.amber.oauth2.as.request.OAuthAuthzRequest;
import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.amber.oauth2.common.message.types.ResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oltu.openidconnect.as.OIDC;
import org.apache.oltu.openidconnect.as.util.OIDCAuthzServerUtil;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.oauth.cache.CacheKey;
import org.wso2.carbon.identity.oauth.cache.SessionDataCache;
import org.wso2.carbon.identity.oauth.cache.SessionDataCacheEntry;
import org.wso2.carbon.identity.oauth.cache.SessionDataCacheKey;
import org.wso2.carbon.identity.oauth.common.OAuth2ErrorCodes;
import org.wso2.carbon.identity.oauth.endpoint.OAuthRequestWrapper;
import org.wso2.carbon.identity.oauth2.model.OAuth2Parameters;
import org.wso2.carbon.identity.oauth.util.EndpointUtil;
import org.wso2.carbon.identity.oauth.util.OpenIDConnectConstant;
import org.wso2.carbon.identity.oauth.util.OpenIDConnectUserRPStore;
import org.wso2.carbon.identity.oauth2.dto.OAuth2AuthorizeReqDTO;
import org.wso2.carbon.identity.oauth2.dto.OAuth2AuthorizeRespDTO;
import org.wso2.carbon.identity.oauth2.dto.OAuth2ClientValidationResponseDTO;
import org.wso2.carbon.identity.oauth2.util.OAuth2Constants;
import org.wso2.carbon.registry.core.utils.UUIDGenerator;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

@Path("/authorize")
public class OAuth2AuthzEndpoint {

    private static Log log = LogFactory.getLog(OAuth2AuthzEndpoint.class);

    @GET
    @Path("/")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    public Response authorize(@Context HttpServletRequest request) throws URISyntaxException {

        try {
            PrivilegedCarbonContext.startTenantFlow();
            PrivilegedCarbonContext carbonContext = PrivilegedCarbonContext
                    .getThreadLocalCarbonContext();
            carbonContext.setTenantId(MultitenantConstants.SUPER_TENANT_ID);
            carbonContext.setTenantDomain(MultitenantConstants.SUPER_TENANT_DOMAIN_NAME);

            SessionDataCacheEntry sessionDataCacheEntry = null;
            String sessionDataKey = EndpointUtil.getSafeText((String) request
                    .getAttribute(OAuth2Constants.SESSION_DATA_KEY));
            String consent = EndpointUtil.getSafeText(request.getParameter("consent"));

            if (sessionDataKey != null) {
                CacheKey cacheKey = new SessionDataCacheKey(sessionDataKey);
                Object result = SessionDataCache.getInstance().getValueFromCache(cacheKey);
                if (result != null) {
                    sessionDataCacheEntry = ((SessionDataCacheEntry) result);
                } else {
                    return showSessionError(request, sessionDataKey);
                }
            } else if (consent != null) {
                /*
                 * For security reasons we allow only from the consent page to send the session data
                 * key over HTTP POST query parameter.
                 */
                sessionDataKey = EndpointUtil.getSafeText(request
                        .getParameter(OAuth2Constants.SESSION_DATA_KEY));
                if (sessionDataKey != null) {
                    CacheKey cacheKey = new SessionDataCacheKey(sessionDataKey);
                    Object result = SessionDataCache.getInstance().getValueFromCache(cacheKey);
                    if (result != null) {
                        sessionDataCacheEntry = ((SessionDataCacheEntry) result);
                    } else {
                        return showSessionError(request, sessionDataKey);
                    }
                } else {
                    return showSessionError(request, sessionDataKey);
                }
            } else {
                sessionDataCacheEntry = new SessionDataCacheEntry();
            }

            String clientId = EndpointUtil.getSafeText(request.getParameter("client_id"));
            OAuth2Parameters oauth2Params = sessionDataCacheEntry.getoAuth2Parameters();

            try {
                if (clientId != null) { // request from the client

                    String redirectURL = EndpointUtil.getSafeText(request
                            .getParameter("redirect_uri"));
                    try {
                        redirectURL = handleOAuthAuthorizationRequest(request, clientId,
                                redirectURL, sessionDataCacheEntry);
                    } catch (OAuthProblemException e) {
                        log.debug(e.getError(), e.getCause());
                        redirectURL = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                                .error(e).location(redirectURL).buildQueryMessage()
                                .getLocationUri();
                    }
                    return Response.status(HttpServletResponse.SC_FOUND)
                            .location(new URI(redirectURL)).build();

                } else if (consent != null) { // request from the consent page

                    String returnUrl = null;
                    if (sessionDataCacheEntry.getOidcRequest() != null) {
                        returnUrl = handleUserConsent(consent, request, oauth2Params,
                                sessionDataCacheEntry);
                    } else {
                        returnUrl = handleUserAuthzParams(consent, request, sessionDataCacheEntry);
                    }
                    SessionDataCache.getInstance().clearCacheEntry(
                            new SessionDataCacheKey(sessionDataKey));
                    return Response.status(HttpServletResponse.SC_FOUND)
                            .location(new URI(returnUrl)).build();

                } else if (oauth2Params != null) { // request from the login page

                    String redirectURL = null;
                    if ((Boolean) request.getAttribute("commonAuthAuthenticated") == true) {

                        String username = (String) request.getAttribute("authenticatedUser");

                        String tenantDomain = MultitenantUtils.getTenantDomain(username);
                        String tenantAwareUserName = MultitenantUtils.getTenantAwareUsername(username);
                        username = tenantAwareUserName + "@" + tenantDomain;
                        username = username.toLowerCase();
                        sessionDataCacheEntry.setOidcLoggedInUser(username);
                        redirectURL = doUserAuthz(request, oauth2Params, sessionDataKey,
                                sessionDataCacheEntry);
                    } else {
                        OAuthProblemException oauthException = null;
                        
                        if (sessionDataCacheEntry.getOidcRequest() != null
                                && oauth2Params.getPrompt() != null
                                && oauth2Params.getPrompt().contains(OIDC.Prompt.NONE)) {
                            
                            oauthException = OAuthProblemException.error(OIDC.Error.LOGIN_REQUIRED,
                                    "No authenticated user found");
                            
                        } else {
                            oauthException = OAuthProblemException.error(OAuth2ErrorCodes.ACCESS_DENIED,
                                    " The end-user or authorization server denied the request");
                        }
                        
                        redirectURL = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                                .error(oauthException).location(oauth2Params.getRedirectURI())
                                .setState(oauth2Params.getState()).buildQueryMessage()
                                .getLocationUri();
                    }
                    return Response.status(HttpServletResponse.SC_FOUND)
                            .location(new URI(redirectURL)).build();
                    
                } else {
                    SessionDataCache.getInstance().clearCacheEntry(new SessionDataCacheKey(sessionDataKey));
                    log.error("Invalid Authorization Request");
                    return Response
                            .status(HttpServletResponse.SC_FOUND)
                            .location(
                                    new URI(EndpointUtil
                                            .getErrorPageURL(request, oauth2Params,
                                                    OAuth2ErrorCodes.INVALID_REQUEST,
                                                    "Invalid Authorization Request"))).build();
                }
            } catch (OAuthSystemException e) {
                SessionDataCache.getInstance().clearCacheEntry(
                        new SessionDataCacheKey(sessionDataKey));
                log.error(e.getMessage(), e);
                return Response
                        .status(HttpServletResponse.SC_FOUND)
                        .location(
                                new URI(EndpointUtil.getErrorPageURL(request, oauth2Params,
                                        OAuth2ErrorCodes.INVALID_REQUEST, e.getMessage()))).build();
            }

        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }

    }

        @POST
        @Path("/")
        @Consumes("application/x-www-form-urlencoded")
        @Produces("text/html")
        public Response authorizePost(@Context HttpServletRequest request, MultivaluedMap paramMap) throws URISyntaxException {
            HttpServletRequestWrapper httpRequest = new OAuthRequestWrapper(request, paramMap);
            return authorize(httpRequest);
        }

	/**
	 * 
	 * @param consent
	 * @param request
	 * @param oauth2Params
	 * @return
	 * @throws OAuthSystemException
	 */
	private String handleUserConsent(String consent, HttpServletRequest request, OAuth2Parameters oauth2Params,
                                     SessionDataCacheEntry sessionDataCacheEntry) throws OAuthSystemException {

		String returnUrl = sessionDataCacheEntry.getOidcResponse();
		String applicationName = sessionDataCacheEntry.getOidcRP();
		String loggedInUser = sessionDataCacheEntry.getOidcLoggedInUser();

		if (OpenIDConnectConstant.Consent.DENY.equals(consent)) {
			// return an error if user denied
			return OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
			                      .setError(OAuth2ErrorCodes.ACCESS_DENIED)
			                      .location(oauth2Params.getRedirectURI()).setState(oauth2Params.getState())
			                      .buildQueryMessage().getLocationUri();
		}

		boolean approvedAlways = OpenIDConnectConstant.Consent.APPROVE_ALWAYS.equals(consent) ? true : false;
		OpenIDConnectUserRPStore.getInstance()
		                        .putUserRPToStore(loggedInUser, applicationName, approvedAlways);

		return returnUrl;
	}

	/**
	 * http://tools.ietf.org/html/rfc6749#section-4.1.2
	 * 
	 * 4.1.2.1. Error Response
	 * 
	 * If the request fails due to a missing, invalid, or mismatching
	 * redirection URI, or if the client identifier is missing or invalid,
	 * the authorization server SHOULD inform the resource owner of the
	 * error and MUST NOT automatically redirect the user-agent to the
	 * invalid redirection URI.
	 * 
	 * If the resource owner denies the access request or if the request
	 * fails for reasons other than a missing or invalid redirection URI,
	 * the authorization server informs the client by adding the following
	 * parameters to the query component of the redirection URI using the
	 * "application/x-www-form-urlencoded" format
	 * 
	 * @param req
	 * @param clientId 
	 * @param callbackURL 
	 * @return
	 * @throws OAuthSystemException
	 * @throws OAuthProblemException
	 */
	private String handleOAuthAuthorizationRequest(HttpServletRequest req, String clientId, String callbackURL,
                    SessionDataCacheEntry sessionDataCacheEntry) throws OAuthSystemException, OAuthProblemException {
		
		OAuth2ClientValidationResponseDTO clientDTO = null;
		if (clientId != null) {
			clientDTO = validateClient(req, clientId, callbackURL);
		} else {
			log.warn("Client Id is not present in the authorization request.");
			return EndpointUtil.getErrorPageURL(req, null, OAuth2ErrorCodes.INVALID_REQUEST,
			                                    "Invalid Request. Client Id is not present in the request");
		}
		if (!clientDTO.isValidClient()) {
			return EndpointUtil.getErrorPageURL(req, null, clientDTO.getErrorCode(), clientDTO.getErrorMsg());
		}

		// Now the client is valid, redirect him to the authorization page.
		OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(req);
		OAuth2Parameters params = new OAuth2Parameters();
		params.setApplicationName(clientDTO.getApplicationName());
		params.setRedirectURI(clientDTO.getCallbackURL());
		params.setResponseType(oauthRequest.getResponseType());
		params.setScopes(oauthRequest.getScopes());
		params.setState(oauthRequest.getState());
		params.setClientId(clientId);

		boolean forceAuthenticate = false;
		boolean checkAuthentication = false;

		// OpenID Connect request parameters
		if (OIDCAuthzServerUtil.isOIDCAuthzRequest(oauthRequest.getScopes())) {
            sessionDataCacheEntry.setOidcRequest("true");
        }
        sessionDataCacheEntry.setOidcRP(params.getApplicationName());
        params.setNonce(oauthRequest.getParam(OIDC.AuthZRequest.NONCE));
        params.setDisplay(oauthRequest.getParam(OIDC.AuthZRequest.DISPLAY));
        params.setRequest(oauthRequest.getParam(OIDC.AuthZRequest.REQUEST));
        params.setRequestURI(oauthRequest.getParam(OIDC.AuthZRequest.REQUEST_URI));
        params.setIDTokenHint(oauthRequest.getParam(OIDC.AuthZRequest.ID_TOKEN_HINT));
        params.setLoginHint(oauthRequest.getParam(OIDC.AuthZRequest.LOGIN_HINT));
        String prompt = oauthRequest.getParam(OIDC.AuthZRequest.PROMPT);
        params.setPrompt(prompt);

        /**
         * The prompt parameter can be used by the Client to make sure
         * that the End-User is still present for the current session or
         * to bring attention to the request. If this parameter contains
         * none with any other value, an error is returned
         *
         * http://openid.net/specs/openid-connect-messages-
         * 1_0-14.html#anchor6
         *
         * prompt : none
         * The Authorization Server MUST NOT display any authentication or
         * consent user interface pages. An error is returned if the
         * End-User is not already authenticated or the Client does not have
         * pre-configured consent for the requested scopes. This can be used
         * as a method to check for existing authentication and/or consent.
         *
         * prompt : login
         * The Authorization Server MUST prompt the End-User for
         * reauthentication.
         *
         * Error : login_required
         * The Authorization Server requires End-User authentication. This
         * error MAY be returned when the prompt parameter in the
         * Authorization Request is set to none to request that the
         * Authorization Server should not display any user interfaces to
         * the End-User, but the Authorization Request cannot be completed
         * without displaying a user interface for user authentication.
         *
         */
        if (prompt != null) {
            // values {none, login, consent, select_profile}
            String[] prompts = prompt.trim().split(" ");
            if (prompts.length < 1) {
                String error = "Invalid prompt variable value. ";
                log.debug(error + prompt);
                return OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                        .setError(OAuth2ErrorCodes.INVALID_REQUEST)
                        .setErrorDescription(error).location(params.getRedirectURI())
                        .setState(params.getState()).buildQueryMessage().getLocationUri();
            }
            boolean contains_none = prompt.contains(OIDC.Prompt.NONE);
            if (prompts.length > 1 && contains_none) {
                String error = "Invalid prompt variable combination. The value none cannot be used with others. ";
                log.debug(error + prompt);
                return OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                        .setError(OAuth2ErrorCodes.INVALID_REQUEST)
                        .setErrorDescription(error).location(params.getRedirectURI())
                        .setState(params.getState()).buildQueryMessage().getLocationUri();
            }

            if(prompt.contains(OIDC.Prompt.LOGIN)) { // prompt for authentication
                checkAuthentication = false;
                forceAuthenticate = true;

            } else if(contains_none || prompt.contains(OIDC.Prompt.CONSENT)) {
                checkAuthentication = true;
                forceAuthenticate = false;
            }

			/*Object loggedInUser = req.getSession().getAttribute(
					OpenIDConnectConstant.Session.OIDC_LOGGED_IN_USER);

			if (contains_none && loggedInUser == null) {
				String error = "Received prompt none but no authenticated user found. ";
				log.debug(error + prompt);
				return OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
				                      .setError(OIDC.Error.LOGIN_REQUIRED)
				                      .setErrorDescription(error).location(params.getRedirectURI())
				                      .setState(params.getState()).buildQueryMessage().getLocationUri();
			}
			if (!prompt.contains(OIDC.Prompt.LOGIN)) {
				sessionDataCacheEntry.setoAuth2Parameters(params);
				return CarbonUIUtil.getAdminConsoleURL("/") + "../oauth2/authorize";
			}*/
        }

        String sessionDataKey = UUIDGenerator.generateUUID();
        CacheKey cacheKey = new SessionDataCacheKey(sessionDataKey);
        sessionDataCacheEntry.setoAuth2Parameters(params);
        SessionDataCache.getInstance().addToCache(cacheKey, sessionDataCacheEntry);;

		return EndpointUtil.getLoginPageURL(sessionDataKey, forceAuthenticate, checkAuthentication);
    }

	/**
	 * Validates the client using the oauth2 service
	 * 
	 * @param req
	 * @param clientId
	 * @param callbackURL
	 * @return
	 */
	private OAuth2ClientValidationResponseDTO validateClient(HttpServletRequest req, String clientId, String callbackURL) {
		return EndpointUtil.getOAuth2Service().validateClientInfo(clientId, callbackURL);
	}

	/**
	 * 
	 * @param request
	 * @param sessionDataCacheEntry
	 * @return
	 * @throws OAuthSystemException
	 */
    public String handleUserAuthzParams(String consent, HttpServletRequest request, SessionDataCacheEntry sessionDataCacheEntry)
            throws OAuthSystemException {

		OAuth2Parameters oauth2Params = sessionDataCacheEntry.getoAuth2Parameters();
        String loggedInUser = sessionDataCacheEntry.getOidcLoggedInUser();

        if (OpenIDConnectConstant.Consent.DENY.equals(consent)) {
            // return an error if user denied
            return OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                    .setError(OAuth2ErrorCodes.ACCESS_DENIED)
                    .location(oauth2Params.getRedirectURI()).setState(oauth2Params.getState())
                    .buildQueryMessage().getLocationUri();
        }

        boolean approvedAlways = OpenIDConnectConstant.Consent.APPROVE_ALWAYS.equals(consent) ? true : false;
        OpenIDConnectUserRPStore.getInstance()
                .putUserRPToStore(loggedInUser, oauth2Params.getApplicationName(), approvedAlways);

        OAuthResponse oauthResponse = null;
        OAuth2AuthorizeRespDTO authzRespDTO = null;
        
		// authorizing the request
		authzRespDTO = authorize(oauth2Params, sessionDataCacheEntry);

        if (authzRespDTO != null && authzRespDTO.isAuthorized()) {
		    OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
                    .authorizationResponse(request, HttpServletResponse.SC_FOUND);
		    // all went okay
			if (ResponseType.CODE.toString().equals(oauth2Params.getResponseType())) {
				builder.setCode(authzRespDTO.getAuthorizationCode());
			} else if (ResponseType.TOKEN.toString().equals(oauth2Params.getResponseType())) {
				builder.setAccessToken(authzRespDTO.getAccessToken());
				builder.setExpiresIn(String.valueOf(60 * 60));
			}
			builder.setParam("state", oauth2Params.getState());
			String redirectURL = authzRespDTO.getCallbackURI();
			oauthResponse = builder.location(redirectURL).buildQueryMessage();

		} else {
			// Authorization failure due to various reasons
			OAuthProblemException oauthException = OAuthProblemException.error(
					authzRespDTO.getErrorCode(), authzRespDTO.getErrorMsg());
			oauthResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND).error(oauthException)
		    		.location(oauth2Params.getRedirectURI()).setState(oauth2Params.getState())
					.buildQueryMessage();
        }
        return oauthResponse.getLocationUri();
    }

	/**
	 * prompt : none
	 * The Authorization Server MUST NOT display any authentication
	 * or consent user interface pages. An error is returned if the
	 * End-User is not already authenticated or the Client does not
	 * have pre-configured consent for the requested scopes. This
	 * can be used as a method to check for existing authentication
	 * and/or consent.
	 * 
	 * prompt : consent
	 * The Authorization Server MUST prompt the End-User for consent before
	 * returning information to the Client.
	 * 
	 * prompt Error : consent_required
	 * The Authorization Server requires End-User consent. This
	 * error MAY be returned when the prompt parameter in the
	 * Authorization Request is set to none to request that the
	 * Authorization Server should not display any user
	 * interfaces to the End-User, but the Authorization Request
	 * cannot be completed without displaying a user interface
	 * for End-User consent.
	 *
	 * @param oauth2Params
	 * @return
	 * @throws OAuthSystemException
	 */
	private String doUserAuthz(HttpServletRequest request, OAuth2Parameters oauth2Params, String sessionDataKey,
                               SessionDataCacheEntry sessionDataCacheEntry) throws OAuthSystemException {

        String loggedInUser = sessionDataCacheEntry.getOidcLoggedInUser();

        OAuthResponse oauthResponse = null;
        OAuth2AuthorizeRespDTO authzRespDTO = null;

        // authorizing the request
        authzRespDTO = authorize(oauth2Params, sessionDataCacheEntry);

        if (authzRespDTO != null && authzRespDTO.isAuthorized()) {
            OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
                    .authorizationResponse(request, HttpServletResponse.SC_FOUND);
            // all went okay
            if (ResponseType.CODE.toString().equals(oauth2Params.getResponseType())) {
                builder.setCode(authzRespDTO.getAuthorizationCode());
            } else if (ResponseType.TOKEN.toString().equals(oauth2Params.getResponseType())) {
                builder.setAccessToken(authzRespDTO.getAccessToken());
                builder.setExpiresIn(String.valueOf(60 * 60));
            }
            builder.setParam("state", oauth2Params.getState());
            String redirectURL = authzRespDTO.getCallbackURI();
            oauthResponse = builder.location(redirectURL).buildQueryMessage();

        } else {
            // Authorization failure due to various reasons
            OAuthProblemException oauthException = OAuthProblemException.error(
                    authzRespDTO.getErrorCode(), authzRespDTO.getErrorMsg());
            oauthResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND).error(oauthException)
                    .location(oauth2Params.getRedirectURI()).setState(oauth2Params.getState())
                    .buildQueryMessage();
        }

        String redirectUrl = oauthResponse.getLocationUri();

        if("true".equals(sessionDataCacheEntry.getOidcRequest())){

            if (oauth2Params.getPrompt() == null || oauth2Params.getPrompt().contains(OIDC.Prompt.CONSENT) ||
                    !oauth2Params.getPrompt().contains(OIDC.Prompt.NONE)) {

                sessionDataCacheEntry.setOidcResponse(redirectUrl);
                SessionDataCache.getInstance().addToCache(new SessionDataCacheKey(sessionDataKey), sessionDataCacheEntry);
                return EndpointUtil.getUserConsentURL(oauth2Params, loggedInUser, sessionDataKey, true);

            } else if (oauth2Params.getPrompt().contains(OIDC.Prompt.NONE)) {  // should not prompt for consent if approved always
                // load the users approved applications to skip consent
                String appName = oauth2Params.getApplicationName();
                boolean skipConsent = EndpointUtil.getOAuthServerConfiguration().getOpenIDConnectSkipeUserConsentConfig();
                boolean hasUserApproved = true;
                if (!skipConsent) {
                    hasUserApproved = OpenIDConnectUserRPStore.getInstance().hasUserApproved(loggedInUser, appName);
                }
                if (hasUserApproved) {
                    return redirectUrl;
                } else {
                    // returning error
                    return OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                            .setError(OIDC.Error.CONSENT_REQUIRED)
                            .location(oauth2Params.getRedirectURI())
                            .setState(oauth2Params.getState()).buildQueryMessage()
                            .getLocationUri();
                }
            } else {
                // cannot reach here
            }

        } else {

            if (oauth2Params.getPrompt() == null || oauth2Params.getPrompt().contains(OIDC.Prompt.CONSENT) ||
                    !oauth2Params.getPrompt().contains(OIDC.Prompt.NONE)) {

                SessionDataCache.getInstance().addToCache(new SessionDataCacheKey(sessionDataKey), sessionDataCacheEntry);
                return EndpointUtil.getUserConsentURL(oauth2Params, loggedInUser, sessionDataKey, false);

            } else if (oauth2Params.getPrompt().contains(OIDC.Prompt.NONE)) {  // should not prompt for consent
                // load the users approved applications to skip consent
                String appName = sessionDataCacheEntry.getOidcRP();
                boolean skipConsent = EndpointUtil.getOAuthServerConfiguration()
                        .getOpenIDConnectSkipeUserConsentConfig();
                boolean hasUserApproved = true;
                if (!skipConsent) {
                    hasUserApproved = OpenIDConnectUserRPStore.getInstance().hasUserApproved(loggedInUser, appName);
                }
                if (hasUserApproved) {
                    return redirectUrl;
                } else {
                    // returning error
                    return OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                            .setError(OIDC.Error.CONSENT_REQUIRED)
                            .location(oauth2Params.getRedirectURI())
                            .setState(oauth2Params.getState()).buildQueryMessage()
                            .getLocationUri();
                }
            } else {
                // cannot reach here
            }
        }
        return OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                .setError(OAuth2ErrorCodes.ACCESS_DENIED)
                .location(oauth2Params.getRedirectURI())
                .setState(oauth2Params.getState()).buildQueryMessage()
                .getLocationUri();
	}

	/**
	 * Here we set the authenticated user to the session data
	 *
	 * @param oauth2Params
	 * @return
	 */
    private OAuth2AuthorizeRespDTO authorize(OAuth2Parameters oauth2Params, SessionDataCacheEntry sessionDataCacheEntry) {
    	
        OAuth2AuthorizeReqDTO authzReqDTO = new OAuth2AuthorizeReqDTO();
        authzReqDTO.setCallbackUrl(oauth2Params.getRedirectURI());
        authzReqDTO.setConsumerKey(oauth2Params.getClientId());
        authzReqDTO.setResponseType(oauth2Params.getResponseType());
        authzReqDTO.setScopes(oauth2Params.getScopes().toArray(new String[oauth2Params.getScopes().size()]));
        authzReqDTO.setUsername(sessionDataCacheEntry.getOidcLoggedInUser());
        return EndpointUtil.getOAuth2Service().authorize(authzReqDTO);
    }
    
    private Response showSessionError(HttpServletRequest request, String sessionDataKey) throws URISyntaxException {
    	  log.debug("Session data not found in SessionDataCache for " + sessionDataKey);
          return Response.status(HttpServletResponse.SC_FOUND)
                  .location(new URI(EndpointUtil.getErrorPageURL(request, null, OAuth2ErrorCodes.ACCESS_DENIED, "Session Timed Out")))
                  .build();
    }
}
