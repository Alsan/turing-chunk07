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
package org.wso2.carbon.appmgt.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.catalina.connector.Request;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.appmgt.api.APIManagementException;
import org.wso2.carbon.appmgt.core.APIManagerErrorConstants;
import org.wso2.carbon.appmgt.core.authenticate.APITokenValidator;
import org.wso2.carbon.appmgt.core.usage.APIStatsPublisher;
import org.wso2.carbon.appmgt.impl.APIConstants;
import org.wso2.carbon.appmgt.impl.dto.APIKeyValidationInfoDTO;
import org.wso2.carbon.appmgt.interceptor.utils.APIManagetInterceptorUtils;
import org.wso2.carbon.appmgt.interceptor.valve.APIFaultException;
import org.wso2.carbon.appmgt.interceptor.valve.APIThrottleHandler;
import org.wso2.carbon.appmgt.interceptor.valve.internal.DataHolder;
import org.wso2.carbon.appmgt.usage.publisher.APIMgtUsageDataPublisher;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.identity.base.IdentityException;
import org.wso2.carbon.identity.core.util.IdentityUtil;

/**
 * APIManagement operations
 *
 */

public class APIManagerInterceptorOps {

	private APIKeyValidationInfoDTO apiKeyValidationDTO;
	
	
	private static final Log log = LogFactory.getLog(APIManagerInterceptorOps.class);

	public APIManagerInterceptorOps() {			
	}

	/**
	 * Authenticate the request
	 * 
	 * @param context
	 * @param version
	 * @param accessToken
	 * @param requiredAuthenticationLevel
	 * @param clientDomain
	 * @return
	 * @throws APIManagementException
	 * @throws APIFaultException
	 */
	public boolean doAuthenticate(String context, String version, String accessToken,
	                              String requiredAuthenticationLevel, String clientDomain)
	                                                                                      throws APIManagementException,
	                                                                                      APIFaultException {

		if (APIConstants.AUTH_NO_AUTHENTICATION.equals(requiredAuthenticationLevel)) {
			return true;
		}
		APITokenValidator tokenValidator = new APITokenValidator();
		apiKeyValidationDTO = tokenValidator.validateKey(context, version, accessToken,
		                                                 requiredAuthenticationLevel, clientDomain);
		if (apiKeyValidationDTO.isAuthorized()) {
			String userName = apiKeyValidationDTO.getEndUserName();
			PrivilegedCarbonContext.getThreadLocalCarbonContext()
			                       .setUsername(apiKeyValidationDTO.getEndUserName());
			try {
				PrivilegedCarbonContext.getThreadLocalCarbonContext()
				                       .setTenantId(IdentityUtil.getTenantIdOFUser(userName));
			} catch (IdentityException e) {
				log.error("Error while retrieving Tenant Id", e);
				return false;
			}
			return true;
		} else {
			throw new APIFaultException(apiKeyValidationDTO.getValidationStatus(),
			                            "Access failure for WebApp: " + context + ", version: " +
			                                    version + " with key: " + accessToken);
		}
	}

	/**
	 * Throttle out the request
	 * 
	 * @param request
	 * @param accessToken
	 * @return
	 * @throws APIFaultException
	 */
	public boolean doThrottle(Request request, String accessToken) throws APIFaultException {

		String apiName = request.getContextPath();
		String apiVersion = APIManagetInterceptorUtils.getAPIVersion(request);
		String apiIdentifier = apiName + "-" + apiVersion;

		APIThrottleHandler throttleHandler = null;
		ConfigurationContext cc = DataHolder.getServerConfigContext();

		if (cc.getProperty(apiIdentifier) == null) {
			throttleHandler = new APIThrottleHandler();
			/* Add the Throttle handler to ConfigContext against WebApp Identifier */
			cc.setProperty(apiIdentifier, throttleHandler);
		} else {
			throttleHandler = (APIThrottleHandler) cc.getProperty(apiIdentifier);
		}

		if (throttleHandler.doThrottle(request, apiKeyValidationDTO, accessToken)) {
			return true;
		} else {
			throw new APIFaultException(APIManagerErrorConstants.API_THROTTLE_OUT,
			                            "You have exceeded your quota");
		}
	}

	/**
	 * 
	 * @param request -Httpservlet request
	 * @param accessToken
	 * @return
	 * @throws APIFaultException
	 */
	public boolean doThrottle(HttpServletRequest request, String accessToken) throws APIFaultException {

		String apiName = request.getContextPath();
		String apiVersion = APIManagetInterceptorUtils.getAPIVersion(request);
		String apiIdentifier = apiName + "-" + apiVersion;

		APIThrottleHandler throttleHandler = null;
		ConfigurationContext cc = DataHolder.getServerConfigContext();

		if (cc.getProperty(apiIdentifier) == null) {
			throttleHandler = new APIThrottleHandler();
			/* Add the Throttle handler to ConfigContext against WebApp Identifier */
			cc.setProperty(apiIdentifier, throttleHandler);
		} else {
			throttleHandler = (APIThrottleHandler) cc.getProperty(apiIdentifier);
		}

		if (throttleHandler.doThrottle(request, apiKeyValidationDTO, accessToken)) {
			return true;
		} else {
			throw new APIFaultException(APIManagerErrorConstants.API_THROTTLE_OUT,
			                            "You have exceeded your quota");
		}
	}

	/**
	 * Publish the request/response statistics
	 * 
	 * @param request
	 * @param requestTime
	 * @param response
	 *            : boolean
	 * @return
	 * @throws APIFaultException 
	 * @throws APIManagementException 
	 */
	public boolean publishStatistics(HttpServletRequest request, long requestTime, boolean response) throws APIManagementException {	
			
		UsageStatConfiguration statConf= new UsageStatConfiguration();
		APIMgtUsageDataPublisher publisher = statConf.getPublisher() ;
		if (publisher != null) {
			publisher.init();
			APIStatsPublisher statsPublisher =  new APIStatsPublisher(publisher,
			                                                         statConf.getHostName());
			if (response) {
				statsPublisher.publishResponseStatistics(apiKeyValidationDTO,
				                                         request.getRequestURI(),
				                                         request.getContextPath(),
				                                         request.getPathInfo(),
				                                         request.getMethod(), requestTime);
			} else {
				statsPublisher.publishRequestStatistics(apiKeyValidationDTO,
				                                        request.getRequestURI(),
				                                        request.getContextPath(),
				                                        request.getPathInfo(), request.getMethod(),
				                                        requestTime);
			}
			return true;
		}
		return false;
	}
}
