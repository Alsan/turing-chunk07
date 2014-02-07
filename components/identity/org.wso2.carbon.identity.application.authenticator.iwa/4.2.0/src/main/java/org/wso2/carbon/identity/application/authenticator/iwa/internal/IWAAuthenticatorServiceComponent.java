package org.wso2.carbon.identity.application.authenticator.iwa.internal;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.identity.application.authentication.framework.ApplicationAuthenticator;
import org.wso2.carbon.identity.application.authenticator.iwa.IWAAuthenticator;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * @scr.component name="identity.application.authenticator.basicauth.component" immediate="true"
 */
public class IWAAuthenticatorServiceComponent {

    private static Log log = LogFactory.getLog(IWAAuthenticatorServiceComponent.class);
    
    protected void activate(ComponentContext ctxt) {
    	
    	IWAAuthenticator iwaAuth = new IWAAuthenticator();
    	Hashtable<String, String> props = new Hashtable<String, String>();
    	
        ctxt.getBundleContext().registerService(ApplicationAuthenticator.class.getName(), iwaAuth, props);
        
        if (log.isDebugEnabled()) {
            log.info("IWAAuthenticator bundle is activated");
        }
    }

    protected void deactivate(ComponentContext ctxt) {
        if (log.isDebugEnabled()) {
            log.info("IWAAuthenticator bundle is deactivated");
        }
    }
}
