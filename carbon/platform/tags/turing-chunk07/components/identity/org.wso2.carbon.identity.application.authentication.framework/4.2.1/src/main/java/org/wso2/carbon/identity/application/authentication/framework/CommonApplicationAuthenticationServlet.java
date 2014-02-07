/*
*  Copyright (c) 2005-2013, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.application.authentication.framework;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authentication.framework.internal.ApplicationAuthenticationFrameworkServiceComponent;
import org.wso2.carbon.registry.core.utils.UUIDGenerator;

@SuppressWarnings("serial")
public class CommonApplicationAuthenticationServlet extends HttpServlet {
	
	private static Log log = LogFactory.getLog(CommonApplicationAuthenticationServlet.class);
	
	private static final String REQUEST_CAN_BE_HANDLED = "requestCanBeHandled";
	
	public ApplicationAuthenticator[] authenticators;
	private final boolean isSingleFactor = ApplicationAuthenticatorsConfiguration.getInstance().isSingleFactor();
	
	@Override
	public void init(){
		authenticators = ApplicationAuthenticationFrameworkServiceComponent.authenticators;
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
														throws ServletException, IOException {
		doPost(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
														throws ServletException, IOException {
		
		/* Check whether this is the start of the authentication flow. 'type' parameter should 
		 * be present if so. This parameter contains the request type (e.g. samlsso) set by the 
		 * calling servlet.
		 */
		if(request.getParameter("type") != null){
			
			String callerSessionDataKey = request.getParameter("sessionDataKey");
			String callerPath = URLDecoder.decode(request.getParameter("commonAuthCallerPath"), "UTF-8");
			String requestType = request.getParameter("type");
			
			boolean forceAuthenticate = request.getParameter("forceAuthenticate") != null 
					? Boolean.valueOf(request.getParameter("forceAuthenticate")) : false;
			boolean checkAuthentication = request.getParameter("checkAuthentication") != null 
					? Boolean.valueOf(request.getParameter("checkAuthentication")) : false;
			
			String authenticatedUser = (String)request.getSession().getAttribute("username");
			
			//send already authenticated or not
			if (checkAuthentication) {
				boolean isAuthenticated = false;
				
				if (authenticatedUser != null) {
					isAuthenticated = true;
				} 
				
				sendResponseToCaller(request, response, isAuthenticated, callerSessionDataKey, callerPath, requestType);
				return;
			}
			
			//skip authentication flow if already logged in
			if (authenticatedUser != null && !forceAuthenticate) {
				sendResponseToCaller(request, response, Boolean.TRUE, callerSessionDataKey, callerPath, requestType);
				return;
			}
			
			//remove the session variables left from a previous authentication flow
			cleanUpSession(request);
			
			//Store the request data sent by the caller in a session DTO
			ApplicationAuthenticationSessionDTO sessionDTO = new ApplicationAuthenticationSessionDTO();
			sessionDTO.setRequestType(requestType);
			sessionDTO.setCallerPath(callerPath);
			sessionDTO.setCallerSessionKey(callerSessionDataKey);
			
			//generate a new session key to hold the session DTO
			String sessionDataKey = UUIDGenerator.generateUUID();
			
			if (log.isDebugEnabled()) {
				log.debug("CommonApplicationAuthenticationServlet sessionDataKey: " + sessionDataKey);
			}
			
			String queryParams = request.getQueryString();
			
			if (log.isDebugEnabled()) {
				log.debug("The query-string sent by the calling servlet is: " + queryParams);
			}

			/* Upto now, query-string contained a 'sessionDataKey' of the calling servlet.
			   At here. we replace it with the key generated for this commonauth servlet. 
			*/	
			queryParams = queryParams.replace(callerSessionDataKey, sessionDataKey);
			sessionDTO.setQueryParams("?" + queryParams);
			
			//TODO: Remove commonAuthCallerPath from the queryString.
			
			request.getSession().setAttribute("sessionDataKey", sessionDataKey);
			request.getSession().setAttribute(sessionDataKey, sessionDTO);
			request.setAttribute("commonAuthQueryParams", "?" + queryParams);
		}
		
		if(request.getParameter("deny") != null) {
		    sendResponseToCaller(request, response, Boolean.FALSE);
		    return;
		}
		
		for (ApplicationAuthenticator authenticator : authenticators) {
			
			if (!authenticator.isDisabled()) {
				int status = authenticator.getStatus(request);
				
				//Authenticator is called if it's not already AUTHENTICATED and if its status is not CONNOT HANDLE.
				if (status != ApplicationAuthenticatorConstants.STATUS_AUTHENTICATION_PASS 
						|| status != ApplicationAuthenticatorConstants.STATUS_AUTHENTICATION_CANNOT_HANDLE) {
					
					//"canHandle" session attribute is set to indicate atleast one Authenticator 
					//can handle the request.
					if (request.getSession().getAttribute(REQUEST_CAN_BE_HANDLED) == null) {
						request.getSession().setAttribute(REQUEST_CAN_BE_HANDLED, Boolean.TRUE);
					}
					
					status = authenticator.doAuthentication(request, response);
					
					//Authenticator setting a custom status means, its job is not completed yet.
					if (status != ApplicationAuthenticatorConstants.STATUS_AUTHENTICATION_PASS
							&& status != ApplicationAuthenticatorConstants.STATUS_AUTHENTICATION_FAIL
							&& status != ApplicationAuthenticatorConstants.STATUS_AUTHENTICATION_CANNOT_HANDLE) {
						
						if (log.isDebugEnabled()) {
							log.debug(authenticator.getAuthenticatorName() + 
							          " has set custom status code: " + String.valueOf(status));
						}
						
						return;
					}
					
					/* In single or multi factor modes, one Authenticator failing means whole authentication 
					   chain is failed. */
					if (status == ApplicationAuthenticatorConstants.STATUS_AUTHENTICATION_FAIL) {
						
						if (log.isDebugEnabled()) {
							log.debug("Authentication chain failed due to " + authenticator.getAuthenticatorName() 
							          + "failure");
						}
						
			            sendResponseToCaller(request, response, Boolean.FALSE);
			            return;
					}
					
					//If in single-factor mode, no need to check the other Authenticators. Send the response back.
					if (status == ApplicationAuthenticatorConstants.STATUS_AUTHENTICATION_PASS && isSingleFactor) {
						
						if (log.isDebugEnabled()) {
							log.debug("Authenticaticated by " + authenticator.getAuthenticatorName() 
							          + " in single-factor mode");
						}
						
						sendResponseToCaller(request, response, Boolean.TRUE);
						return;
					}
				} 
			}
		}
		
		//If all the Authenticators failed to handle the request
		if (request.getSession().getAttribute(REQUEST_CAN_BE_HANDLED) == null) {
			
			if (log.isDebugEnabled()) {
				log.debug("No Authenticator can handle the request");
			}
			
			sendResponseToCaller(request, response, Boolean.FALSE);
		} 
		//Otherwise, authentication has PASSED in multi-factor mode
		else { 
			
			if (log.isDebugEnabled()) {
				log.debug("Authenticared passed in multi-factor mode");
			}
			
			sendResponseToCaller(request, response, Boolean.TRUE);
		}
	}
	
	private void sendResponseToCaller(HttpServletRequest request, 
	                                          HttpServletResponse response, 
	                                          Boolean isAuthenticated) 
	                                        		  throws ServletException, IOException {
		
		String sessionDataKey = request.getParameter(ApplicationAuthenticatorConstants.SESSION_DATA_KEY);
		ApplicationAuthenticationSessionDTO sessionDTO = 
				(ApplicationAuthenticationSessionDTO)request.getSession().getAttribute(
						             (String)request.getSession().getAttribute("sessionDataKey"));
		
		sendResponseToCaller(request, response, isAuthenticated, sessionDTO.getCallerSessionKey(), 
		                     sessionDTO.getCallerPath(), sessionDTO.getRequestType());
    }
	
	private void sendResponseToCaller(HttpServletRequest request, HttpServletResponse response,
	                                  Boolean isAuthenticated, String callerSessionKey, 
	                                  String callerPath, String requestType) throws ServletException, IOException {

		// Set values to be returned to the calling servlet as request
		// attributes
		request.setAttribute(ApplicationAuthenticatorConstants.AUTHENTICATED, isAuthenticated);
		request.setAttribute(ApplicationAuthenticatorConstants.AUTHENTICATED_USER, (String) request.getSession().getAttribute("username"));
		request.setAttribute(ApplicationAuthenticatorConstants.SESSION_DATA_KEY, callerSessionKey);

		if (log.isDebugEnabled()) {
			log.debug("Sending response back to: " + callerPath);
		}

		if (requestType.equals("oauth2")) {
			request.getServletContext().getContext("/oauth2").getRequestDispatcher("/authorize/")
			       .forward(request, response);
		} else {
			RequestDispatcher dispatcher = request.getRequestDispatcher(callerPath);
			dispatcher.forward(request, response);
		}
	}
	
	private void cleanUpSession(HttpServletRequest request) {
		request.getSession().removeAttribute(REQUEST_CAN_BE_HANDLED);
		request.getSession().removeAttribute(ApplicationAuthenticatorConstants.DO_AUTHENTICATION);
		
		//Reset the status of all the authenticators before catering the new request
		for(ApplicationAuthenticator authenticator : authenticators){
			authenticator.resetStatus(request);
		}
	}
}