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

package org.wso2.carbon.identity.entitlement.pip;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.core.util.AdminServicesUtil;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.entitlement.internal.EntitlementServiceComponent;
import org.wso2.carbon.user.api.ClaimManager;
import org.wso2.carbon.user.api.ClaimMapping;
import org.wso2.carbon.user.core.UserCoreConstants;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

/**
 * DefaultAttributeFinder talks to the underlying user store to read user attributes.
 * DefaultAttributeFinder is by default registered for all the claims defined under
 * http://wso2.org/claims dialect.
 */
public class DefaultAttributeFinder extends AbstractPIPAttributeFinder {

	private Set<String> supportedAttrs = new HashSet<String>();
	private static Log log = LogFactory.getLog(DefaultAttributeFinder.class);

	/**
	 * Loads all the claims defined under http://wso2.org/claims dialect.
	 * 
	 * @throws Exception
	 */
	public void init(Properties properties) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("DefaultAttributeFinder is initialized successfully");
		}
	}

    @Override
    public String getModuleName() {
        return "Default Attribute Finder";
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.wso2.carbon.identity.entitlement.pip.PIPAttributeFinder#getAttributeValues(java.lang.
	 * String, java.lang.String, java.lang.String)
	 */
	public Set<String> getAttributeValues(String subjectId, String resourceId, String actionId,
                                          String environmentId, String attributeId, String issuer) throws Exception {
		Set<String> values = new HashSet<String>();

        subjectId = MultitenantUtils.getTenantAwareUsername(subjectId);
		if (UserCoreConstants.ClaimTypeURIs.ROLE.equals(attributeId)) {
			if (log.isDebugEnabled()) {
				log.debug("Looking for roles via DefaultAttributeFinder");
			}
			String[] roles = CarbonContext.getThreadLocalCarbonContext().getUserRealm().getUserStoreManager()
					.getRoleListOfUser(subjectId);
			if (roles != null && roles.length > 0) {
                for (String role : roles) {
                    if (log.isDebugEnabled()) {
                        log.debug(String.format("User %1$s belongs to the Role %2$s", subjectId,
                                role));
                    }
                    values.add(role);
                }
			}
		} else {
			String claimValue = CarbonContext.getThreadLocalCarbonContext().getUserRealm().
                    getUserStoreManager().getUserClaimValue(subjectId, attributeId, null);
			if (claimValue == null && log.isDebugEnabled()) {
				log.debug(String.format("Request attribute %1$s not found", attributeId));
			}
			values.add(claimValue);
		}
		return values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.wso2.carbon.identity.entitlement.pip.PIPAttributeFinder#getSupportedAttributes()
	 */
	public Set<String> getSupportedAttributes() {
        try{
            ClaimManager claimManager = CarbonContext.getThreadLocalCarbonContext().getUserRealm().getClaimManager();
            ClaimMapping[] claims = claimManager
                                    .getAllClaimMappings(UserCoreConstants.DEFAULT_CARBON_DIALECT);
            for (ClaimMapping claim : claims) {
                supportedAttrs.add(claim.getClaim().getClaimUri());
            }
        } catch (Exception e){
            //ignore
        }
        return supportedAttrs;
	}
}
