package org.wso2.carbon.identity.application.authenticator.iwa;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.identity.application.authentication.framework.AbstractApplicationAuthenticator;
import org.wso2.carbon.identity.application.authentication.framework.AuthenticatorStatus;
import org.wso2.carbon.identity.application.authentication.framework.context.ApplicationAuthenticationContext;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

/**
 * Username Password based Authenticator
 *
 */
public class IWAAuthenticator extends AbstractApplicationAuthenticator {

	private static Log log = LogFactory.getLog(IWAAuthenticator.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wso2.carbon.identity.application.authentication.framework.
	 * ApplicationAuthenticator#canHandle(javax.servlet.http.HttpServletRequest)
	 */
    @Override
    public boolean canHandle(HttpServletRequest request) {

    	if (log.isTraceEnabled()) {
    		log.trace("Inside canHandle()");
    	}
    	
    	if ("Negotiate".equalsIgnoreCase(request.getAuthType()) && request.getRemoteUser() != null) {
            if (log.isDebugEnabled()) {
                log.debug("IWA request received for url: " + request.getRequestURL());
            }
            return true;
        }
        
        return false;
    }
    
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wso2.carbon.identity.application.authentication.framework.
	 * ApplicationAuthenticator
	 * #authenticate(javax.servlet.http.HttpServletRequest)
	 */
    @Override
    public AuthenticatorStatus authenticate(HttpServletRequest request, HttpServletResponse response, ApplicationAuthenticationContext context) {
    	
    	if (log.isTraceEnabled()) {
    		log.trace("Inside authenticate()");
    	}
    	
    	String username = request.getRemoteUser();
        username = username.substring(username.indexOf("\\") + 1);

        if (log.isDebugEnabled()) {
            log.debug("Authenticate request received : Authtype - " + request.getAuthType() +
                      ", User - " + username);
        }
        
    	 boolean isAuthenticated = false;
    	    
         UserStoreManager userStoreManager = null;
 
         // Check the authentication
         try {
         	userStoreManager = (UserStoreManager) CarbonContext.getThreadLocalCarbonContext().getUserRealm().getUserStoreManager();
 	        isAuthenticated = userStoreManager.isExistingUser(MultitenantUtils.getTenantAwareUsername(username));
         } catch (org.wso2.carbon.user.api.UserStoreException e) {
         	log.error("IWAAuthenticator failed while trying to find user existence", e);
             return AuthenticatorStatus.FAIL;
         }
         
         if (!isAuthenticated) {
             if (log.isDebugEnabled()) {
                 log.debug("user authentication failed");
             }
             
             return AuthenticatorStatus.FAIL;
         }
         
         request.getSession().setAttribute("username", username);
         return AuthenticatorStatus.PASS;
    }
    
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wso2.carbon.identity.application.authentication.framework.
	 * ApplicationAuthenticator#getAuthenticatorName()
	 */
    @Override
    public String getAuthenticatorName() {
    	
    	if (log.isTraceEnabled()) {
    		log.trace("Inside getAuthenticatorName()");
    	}
    	
	    return "IWAAuthenticator";
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wso2.carbon.identity.application.authentication.framework.
	 * ApplicationAuthenticator
	 * #sendInitialRequest(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
    public void sendInitialRequest(HttpServletRequest request, HttpServletResponse response, ApplicationAuthenticationContext context) {
		
		if (log.isTraceEnabled()) {
    		log.trace("Inside sendInitialRequest()");
    	}
		
		throw new UnsupportedOperationException();
    }
	
	public void sendToLoginPage(HttpServletRequest request, HttpServletResponse response, String queryParams) {

		if (log.isTraceEnabled()) {
    		log.trace("Inside sendToLoginPage()");
    	}
		
		throw new UnsupportedOperationException();
	}

    @Override
    public AuthenticatorStatus logout(HttpServletRequest request, HttpServletResponse response, ApplicationAuthenticationContext context) {
    	
    	if (log.isTraceEnabled()) {
    		log.trace("Inside logout()");
    	}
    	
    	// We cannot invalidate the session in case session is used by the calling servlet
        return AuthenticatorStatus.PASS;
    }

	@Override
	public String getAuthenticatedSubject(HttpServletRequest request) {
		
		if (log.isTraceEnabled()) {
    		log.trace("Inside getAuthenticatedSubject()");
    	}
		
		return (String)request.getSession().getAttribute("username");
	}
	
	@Override
	public String getResponseAttributes(HttpServletRequest arg0) {
		return null;
	}

	@Override
	public String getContextIdentifier(HttpServletRequest request) {
		
		if (log.isTraceEnabled()) {
    		log.trace("Inside getContextIdentifier()");
    	}
		
		throw new UnsupportedOperationException();
	}
}
