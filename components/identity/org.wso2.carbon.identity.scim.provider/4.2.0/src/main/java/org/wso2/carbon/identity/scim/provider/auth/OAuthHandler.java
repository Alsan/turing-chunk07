/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.identity.scim.provider.auth;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.message.Message;
import org.wso2.carbon.identity.oauth2.OAuth2TokenValidationService;
import org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationRequestDTO;
import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationResponseDTO;
import org.wso2.carbon.identity.scim.provider.util.SCIMProviderConstants;
import org.wso2.charon.core.schema.SCIMConstants;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class OAuthHandler implements SCIMAuthenticationHandler {

    private static Log log = LogFactory.getLog(BasicAuthHandler.class);

    /*properties map to be initialized*/
    private Map<String, String> properties;

    /*properties specific to this authenticator*/
    private String remoteServiceURL;
    private int priority;
    private String userName;
    private String password;

    /*constants specific to this authenticator*/
    private final String BEARER_AUTH_HEADER = "Bearer";
    private final String LOCAL_PREFIX = "local";
    private final int DEFAULT_PRIORITY = 10;
    private final String LOCAL_AUTH_SERVER = "local://services";

    //Ideally this should be configurable. For the moment, hard code the priority.

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setDefaultPriority() {
        this.priority = DEFAULT_PRIORITY;
    }

    public void setDefaultAuthzServer() {
        this.remoteServiceURL = LOCAL_AUTH_SERVER;
    }

    public boolean canHandle(Message message, ClassResourceInfo classResourceInfo) {
        //check the "Authorization" header and if "Bearer" is there, can be handled.

        //get the map of protocol headers
        TreeMap protocolHeaders = (TreeMap) message.get(Message.PROTOCOL_HEADERS);
        //get the value for Authorization Header
        ArrayList authzHeaders = (ArrayList) protocolHeaders.get(SCIMConstants.AUTHORIZATION_HEADER);
        if (authzHeaders != null) {
            //get the authorization header value, if provided
            String authzHeader = (String) authzHeaders.get(0);
            if (authzHeader != null && authzHeader.contains(BEARER_AUTH_HEADER)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAuthenticated(Message message, ClassResourceInfo classResourceInfo) {
        //get the map of protocol headers
        TreeMap protocolHeaders = (TreeMap) message.get(Message.PROTOCOL_HEADERS);
        //get the value for Authorization Header
        ArrayList authzHeaders = (ArrayList) protocolHeaders.get(SCIMConstants.AUTHORIZATION_HEADER);
        if (authzHeaders != null) {
            //get the authorization header value, if provided
            String authzHeader = (String) authzHeaders.get(0);

            //extract access token
            String accessToken = authzHeader.substring(7).trim();
            //validate access token
            try {
                OAuth2TokenValidationResponseDTO validationResponse = this.validateAccessToken(accessToken);
                if (validationResponse != null) {
                    if (validationResponse.getValid()) {
                        String userName = validationResponse.getAuthorizedUser();
                        authzHeaders.set(0, userName);
                        return true;
                    }
                }
            } catch (Exception e) {
                String error = "Error in validating OAuth access token.";
                log.error(error, e);
            }
        }
        return false;
    }

    /**
     * To set the properties specific to each authenticator
     *
     * @param authenticatorProperties
     */
    public void setProperties(Map<String, String> authenticatorProperties) {
        this.properties = authenticatorProperties;
        String priorityString = properties.get(SCIMProviderConstants.PROPERTY_NAME_PRIORITY);
        if (priorityString != null) {
            priority = Integer.parseInt(priorityString);
        } else {
            priority = DEFAULT_PRIORITY;
        }
        String remoteURLString = properties.get(SCIMProviderConstants.PROPERTY_NAME_AUTH_SERVER);
        if (remoteURLString != null) {
            remoteServiceURL = remoteURLString;
        } else {
            remoteServiceURL = LOCAL_AUTH_SERVER;
        }
        userName = properties.get(SCIMProviderConstants.PROPERTY_NAME_USERNAME);
        password = properties.get(SCIMProviderConstants.PROPERTY_NAME_PASSWORD);
    }

    private String getOAuthAuthzServerURL() {
        if (remoteServiceURL != null) {
            if (!remoteServiceURL.endsWith("/")) {
                remoteServiceURL += "/";
            }
        }
        return remoteServiceURL;
    }

    private OAuth2TokenValidationResponseDTO validateAccessToken(String accessToken)
            throws Exception {
        //if it is specified to use local authz server (i.e: local://services)
        if (remoteServiceURL.startsWith(LOCAL_PREFIX)) {
            OAuth2TokenValidationRequestDTO oauthValidationRequest = new OAuth2TokenValidationRequestDTO();
            oauthValidationRequest.setAccessToken(accessToken);
            oauthValidationRequest.setTokenType(OAuthServiceClient.BEARER_TOKEN_TYPE);

            OAuth2TokenValidationService oauthValidationService = new OAuth2TokenValidationService();
            org.wso2.carbon.identity.oauth2.dto.OAuth2TokenValidationResponseDTO oauthValidationResponse =
                    oauthValidationService.validate(oauthValidationRequest);

            //need to convert to stub.dto
            OAuth2TokenValidationResponseDTO oauthValidationResp = new OAuth2TokenValidationResponseDTO();
            oauthValidationResp.setAuthorizedUser(oauthValidationResponse.getAuthorizedUser());
            oauthValidationResp.setValid(oauthValidationResponse.isValid());
            return oauthValidationResp;
        }
        //else do a web service call to the remote authz server
        try {
            ConfigurationContext configContext =
                    ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
            OAuthServiceClient oauthClient = new OAuthServiceClient(getOAuthAuthzServerURL(),
                                                                    userName, password, configContext);
            OAuth2TokenValidationResponseDTO validationResponse = oauthClient.validateAccessToken(accessToken);
            return validationResponse;
        } catch (AxisFault axisFault) {
            throw axisFault;
        } catch (Exception exception) {
            throw exception;
        }
    }
}

