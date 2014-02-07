/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.user.mgt;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.context.CarbonContext;
import org.wso2.carbon.registry.api.Registry;
import org.wso2.carbon.registry.api.RegistryException;
import org.wso2.carbon.registry.api.Resource;
import org.wso2.carbon.registry.core.Collection;
import org.wso2.carbon.registry.core.RegistryConstants;
import org.wso2.carbon.user.api.Claim;
import org.wso2.carbon.user.api.ClaimMapping;
import org.wso2.carbon.user.api.RealmConfiguration;
import org.wso2.carbon.user.core.AuthorizationManager;
import org.wso2.carbon.user.core.UserCoreConstants;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.common.AbstractUserStoreManager;
import org.wso2.carbon.user.core.ldap.LDAPConstants;
import org.wso2.carbon.user.core.util.UserCoreUtil;
import org.wso2.carbon.user.mgt.bulkimport.BulkImportConfig;
import org.wso2.carbon.user.mgt.bulkimport.CSVUserBulkImport;
import org.wso2.carbon.user.mgt.bulkimport.ExcelUserBulkImport;
import org.wso2.carbon.user.mgt.common.ClaimValue;
import org.wso2.carbon.user.mgt.common.FlaggedName;
import org.wso2.carbon.user.mgt.common.UIPermissionNode;
import org.wso2.carbon.user.mgt.common.UserAdminException;
import org.wso2.carbon.user.mgt.common.UserRealmInfo;
import org.wso2.carbon.user.mgt.common.UserStoreInfo;
import org.wso2.carbon.user.mgt.internal.UserMgtDSComponent;
import org.wso2.carbon.user.mgt.permission.ManagementPermissionUtil;
import org.wso2.carbon.utils.ServerConstants;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

public class UserRealmProxy {

    private static Log log = LogFactory.getLog(UserRealmProxy.class);

    private UserRealm realm = null;

    public UserRealmProxy(UserRealm userRealm) {
        this.realm = userRealm;
    }

    public String[] listUsers(String filter, int maxLimit) throws UserAdminException {
        try {
            return realm.getUserStoreManager().listUsers(filter, maxLimit);
        } catch (UserStoreException e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }
    
    public FlaggedName[] listUsers(ClaimValue claimValue, String filter, int maxLimit) throws UserAdminException {

        try {
            String usersWithClaim[] = null;
            List<FlaggedName> users = new ArrayList<FlaggedName>();

            if(claimValue.getClaimURI() != null && claimValue.getValue() != null){
                usersWithClaim = realm.getUserStoreManager().getUserList(claimValue.getClaimURI(),
                                                                    claimValue.getValue(), null);
            }
            FlaggedName[] allUsers = listAllUsers(filter, maxLimit);

            if(usersWithClaim != null){
                Arrays.sort(usersWithClaim);
                for(FlaggedName name : allUsers){
                    if(Arrays.binarySearch(usersWithClaim, name.getItemName()) > -1){
                        users.add(name);
                    }
                }
                return users.toArray(new FlaggedName[users.size()]);
            } else {
                return allUsers;
            }
        } catch (UserStoreException e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }

    public FlaggedName[] listAllUsers(String filter, int maxLimit) throws UserAdminException{
        FlaggedName[] flaggedNames = null;
        Map<String,Integer> userCount = new HashMap<String,Integer>();
        try {
            UserStoreManager userStoreManager = realm.getUserStoreManager();
            String[] users = userStoreManager.listUsers(filter, maxLimit);
            flaggedNames = new FlaggedName[users.length+1];
            int i = 0;
            for (String user : users) {
                flaggedNames[i] = new FlaggedName();
                //check if display name present
                int index = user.indexOf("|");
                if (index > 0) { //if display name is appended
                    flaggedNames[i].setItemName(user.substring(0, index));
                    flaggedNames[i].setItemDisplayName(user.substring(index + 1));
                } else {
                    //if only user name is present
                    flaggedNames[i].setItemName(user);
                    // set Display name as the item name
                    flaggedNames[i].setItemDisplayName(user);
                }
                int index1 = flaggedNames[i].getItemName() != null
                        ? flaggedNames[i].getItemName().indexOf(CarbonConstants.DOMAIN_SEPARATOR) : -1;
                boolean domainProvided = index1 > 0;
                String domain = domainProvided ? flaggedNames[i].getItemName().substring(0, index1) : null;
                if (domain != null && !UserCoreConstants.INTERNAL_DOMAIN.equalsIgnoreCase(domain)) {
                    UserStoreManager secondaryUM =
                            realm.getUserStoreManager().getSecondaryUserStoreManager(domain); 
                    if (secondaryUM != null && secondaryUM.isReadOnly()) {
                        flaggedNames[i].setEditable(false);
                    } else {
                        flaggedNames[i].setEditable(true);
                    }
                }else{
                    if (realm.getUserStoreManager().isReadOnly()) {
                        flaggedNames[i].setEditable(false);
                    } else {
                        flaggedNames[i].setEditable(true);
                    }
                }
                if(domain != null){
                    if(userCount.containsKey(domain)){
                        userCount.put(domain,userCount.get(domain)+1);
                    }else{
                        userCount.put(domain,1);
                    }
                }else{
                    if(userCount.containsKey(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME)){
                        userCount.put(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME,
                                userCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME)+1);
                    }else{
                        userCount.put(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME, 1);
                    }
                }
                i++;
            }
        } catch (UserStoreException e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
        Arrays.sort(flaggedNames, new Comparator<FlaggedName>() {
            public int compare(FlaggedName o1, FlaggedName o2) {
                if(o1 == null || o2 == null){
                    return 0;
                }
                return o1.getItemName().toLowerCase().compareTo(o2.getItemName().toLowerCase());
            }
        });
        String exceededDomains = "";
        boolean isPrimaryExceeding = false;
        try {
            Map<String,Integer> maxUserListCount = ((AbstractUserStoreManager)realm.getUserStoreManager()).
                    getMaxListCount(UserCoreConstants.RealmConfig.PROPERTY_MAX_USER_LIST);
            String[] domains = userCount.keySet().toArray(new String[userCount.keySet().size()]);
            for(int i=0;i<domains.length;i++){
                if(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME.equalsIgnoreCase(domains[i])){
                    if(userCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME).intValue() ==
                            maxUserListCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME).intValue()){
                        isPrimaryExceeding = true;
                    }
                    continue;
                }
                if(userCount.get(domains[i]).equals(maxUserListCount.get(domains[i].toUpperCase()))){
                    exceededDomains += domains[i];
                    if(i != domains.length - 1){
                        exceededDomains += ":";
                    }
                }
            }
            FlaggedName flaggedName = new FlaggedName();
            if(isPrimaryExceeding){
                flaggedName.setItemName("true");
            }else{
                flaggedName.setItemName("false");
            }
            flaggedName.setItemDisplayName(exceededDomains);
            flaggedNames[flaggedNames.length-1] = flaggedName;
        } catch (UserStoreException e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
        return flaggedNames;
    }

	public FlaggedName[] getAllSharedRoleNames(String filter, int maxLimit)
	                                                                       throws UserAdminException {
		try {

			UserStoreManager userStoreMan = realm.getUserStoreManager();
			// get all roles without hybrid roles
			String[] externalRoles;
			if (userStoreMan instanceof AbstractUserStoreManager) {
				externalRoles =
				                ((AbstractUserStoreManager) userStoreMan).getSharedRoleNames(filter,
				                                                                             maxLimit);
			} else {
				throw new UserAdminException(
				                             "Initialized User Store Manager is not capable of getting the shared roles");
			}

			List<FlaggedName> flaggedNames = new ArrayList<FlaggedName>();
			Map<String, Integer> userCount = new HashMap<String, Integer>();

			for (String externalRole : externalRoles) {
				FlaggedName fName = new FlaggedName();
				mapEntityName(externalRole, fName, userStoreMan);
				fName.setRoleType(UserMgtConstants.EXTERNAL_ROLE);

				// setting read only or writable
				int index = externalRole != null ? externalRole.indexOf("/") : -1;
				boolean domainProvided = index > 0;
				String domain = domainProvided ? externalRole.substring(0, index) : null;
				UserStoreManager secManager =
				                              realm.getUserStoreManager()
				                                   .getSecondaryUserStoreManager(domain);

				if (domain != null && !UserCoreConstants.INTERNAL_DOMAIN.equalsIgnoreCase(domain)) {
					if (secManager != null &&
					    (secManager.isReadOnly() || (secManager.getRealmConfiguration()
                               .getUserStoreProperty(UserCoreConstants.RealmConfig.WRITE_GROUPS_ENABLED) != null &&
                                secManager.getRealmConfiguration().
                                getUserStoreProperty(UserCoreConstants.RealmConfig.WRITE_GROUPS_ENABLED).equals("false")))) {
						fName.setEditable(false);
					} else {
						fName.setEditable(true);
					}
				}
				if (domain != null) {
					if (userCount.containsKey(domain)) {
						userCount.put(domain, userCount.get(domain) + 1);
					} else {
						userCount.put(domain, 1);
					}
				} else {
					if (userCount.containsKey(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME)) {
						userCount.put(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME,
                                userCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME) + 1);
					} else {
						userCount.put(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME, 1);
					}
				}
				flaggedNames.add(fName);
			}

			String exceededDomains = "";
			boolean isPrimaryExceeding = false;
			Map<String, Integer> maxUserListCount =((AbstractUserStoreManager) realm.
                                getUserStoreManager()).getMaxListCount(UserCoreConstants.
                                RealmConfig.PROPERTY_MAX_ROLE_LIST);
			String[] domains = userCount.keySet().toArray(new String[userCount.keySet().size()]);
			for (int i = 0; i < domains.length; i++) {
				if (UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME.equals(domains[i])) {
					if (userCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME).
                        equals(maxUserListCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME))) {
						isPrimaryExceeding = true;
					}
					continue;
				}
				if (userCount.get(domains[i]).equals(maxUserListCount.get(domains[i].toUpperCase()))) {
					exceededDomains += domains[i];
					if (i != domains.length - 1) {
						exceededDomains += ":";
					}
				}
			}
			FlaggedName[] roleNames =
			                          flaggedNames.toArray(new FlaggedName[flaggedNames.size() + 1]);
			Arrays.sort(roleNames, new Comparator<FlaggedName>() {
				public int compare(FlaggedName o1, FlaggedName o2) {
					if (o1 == null || o2 == null) {
						return 0;
					}
					return o1.getItemName().toLowerCase().compareTo(o2.getItemName().toLowerCase());
				}
			});
			FlaggedName flaggedName = new FlaggedName();
			if (isPrimaryExceeding) {
				flaggedName.setItemName("true");
			} else {
				flaggedName.setItemName("false");
			}
			flaggedName.setItemDisplayName(exceededDomains);
			roleNames[roleNames.length - 1] = flaggedName;
			return roleNames;

		} catch (UserStoreException e) {
			// previously logged so logging not needed
			throw new UserAdminException(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new UserAdminException(e.getMessage(), e);
		}
	} 
    
    public FlaggedName[] getAllRolesNames(String filter, int maxLimit) throws UserAdminException {
        try {

            UserStoreManager userStoreMan = realm.getUserStoreManager();
            //get all roles without hybrid roles
            String[] externalRoles ;
            if(userStoreMan instanceof AbstractUserStoreManager){
                externalRoles = ((AbstractUserStoreManager) userStoreMan).getRoleNames(filter,
                        maxLimit, true, true, true);
            } else {
                externalRoles = userStoreMan.getRoleNames();
            }

            List<FlaggedName> flaggedNames = new ArrayList<FlaggedName>();
            Map<String,Integer> userCount = new HashMap<String,Integer>();

            for(String externalRole : externalRoles){
				FlaggedName fName = new FlaggedName();
				mapEntityName(externalRole, fName, userStoreMan);
                fName.setRoleType(UserMgtConstants.EXTERNAL_ROLE);
                
                // setting read only or writable
                int index = externalRole != null ? externalRole.indexOf("/") : -1;
                boolean domainProvided = index > 0;
                String domain = domainProvided ? externalRole.substring(0, index) : null;
            	UserStoreManager secManager = realm.getUserStoreManager().getSecondaryUserStoreManager(domain);

                if (domain != null && !UserCoreConstants.INTERNAL_DOMAIN.equalsIgnoreCase(domain)) {
                    if (secManager!=null && (secManager.isReadOnly() ||
                            ("false".equals(secManager.getRealmConfiguration().
                            getUserStoreProperty(UserCoreConstants.RealmConfig.WRITE_GROUPS_ENABLED))))) {
                        fName.setEditable(false);
                    } else {
                        fName.setEditable(true);
                    }
                } else {
                    if (realm.getUserStoreManager().isReadOnly() ||
                        ("false".equals(realm.getUserStoreManager().getRealmConfiguration().
                            getUserStoreProperty(UserCoreConstants.RealmConfig.WRITE_GROUPS_ENABLED)))) {
                        fName.setEditable(false);
                    } else {
                        fName.setEditable(true);
                    }
                }
                if(domain != null){
                    if(userCount.containsKey(domain)){
                        userCount.put(domain,userCount.get(domain)+1);
                    }else{
                        userCount.put(domain,1);
                    }
                }else{
                    if(userCount.containsKey(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME)){
                        userCount.put(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME,
                                userCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME)+1);
                    }else{
                        userCount.put(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME,1);
                    }
                }
                flaggedNames.add(fName);
            }

            // get hybrid roles
            if(filter.startsWith(UserCoreConstants.INTERNAL_DOMAIN + CarbonConstants.DOMAIN_SEPARATOR)){
                filter = filter.substring(filter.indexOf(CarbonConstants.DOMAIN_SEPARATOR) + 1);
            }
            String[] hybridRoles = ((AbstractUserStoreManager)userStoreMan).getHybridRoles(filter);

            for(String hybridRole : hybridRoles){
                FlaggedName fName = new FlaggedName();
                fName.setItemName(hybridRole);
                fName.setRoleType(UserMgtConstants.INTERNAL_ROLE);
                fName.setEditable(true);
                flaggedNames.add(fName);
            }
            String exceededDomains = "";
            boolean isPrimaryExceeding = false;
            Map<String,Integer> maxUserListCount = ((AbstractUserStoreManager)realm.getUserStoreManager()).getMaxListCount(UserCoreConstants.RealmConfig.PROPERTY_MAX_ROLE_LIST);
            String[] domains = userCount.keySet().toArray(new String[userCount.keySet().size()]);
            for(int i=0;i<domains.length;i++){
                if(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME.equals(domains[i])){
                    if(userCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME).
                            equals(maxUserListCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME))){
                        isPrimaryExceeding = true;
                    }
                    continue;
                }
                if(userCount.get(domains[i]).equals(maxUserListCount.get(domains[i].toUpperCase()))){
                    exceededDomains += domains[i];
                    if(i != domains.length - 1){
                        exceededDomains += ":";
                    }
                }
            }
            FlaggedName[] roleNames = flaggedNames.toArray(new FlaggedName[flaggedNames.size()+1]);
            Arrays.sort(roleNames, new Comparator<FlaggedName>() {
                public int compare(FlaggedName o1, FlaggedName o2) {
                    if(o1 == null || o2 == null){
                        return 0;
                    }
                    return o1.getItemName().toLowerCase().compareTo(o2.getItemName().toLowerCase());
                }
            });
            FlaggedName flaggedName = new FlaggedName();
            if(isPrimaryExceeding){
                flaggedName.setItemName("true");
            }else{
                flaggedName.setItemName("false");
            }
            flaggedName.setItemDisplayName(exceededDomains);
            roleNames[roleNames.length-1] = flaggedName;
            return roleNames;
            
        } catch (UserStoreException e) {
            // previously logged so logging not needed
            throw new UserAdminException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }

    }

    public UserRealmInfo getUserRealmInfo() throws UserAdminException {

        UserRealmInfo userRealmInfo = new UserRealmInfo();

        String userName = CarbonContext.getThreadLocalCarbonContext().getUsername();

        try{

            RealmConfiguration realmConfig = realm.getRealmConfiguration();
            if (realm.getAuthorizationManager().isUserAuthorized(userName,
                    "/permission/admin/configure/security", CarbonConstants.UI_PERMISSION_ACTION) ||
                realm.getAuthorizationManager().isUserAuthorized(userName,
                    "/permission/admin/configure/security/usermgt/users", CarbonConstants.UI_PERMISSION_ACTION) ||
                realm.getAuthorizationManager().isUserAuthorized(userName,
                    "/permission/admin/configure/security/usermgt/passwords", CarbonConstants.UI_PERMISSION_ACTION) ||
                realm.getAuthorizationManager().isUserAuthorized(userName,
                    "/permission/admin/configure/security/usermgt/profiles", CarbonConstants.UI_PERMISSION_ACTION)) {

                userRealmInfo.setAdminRole(realmConfig.getAdminRoleName());
                userRealmInfo.setAdminUser(realmConfig.getAdminUserName());
                userRealmInfo.setEveryOneRole(realmConfig.getEveryOneRoleName());
                ClaimMapping[] defaultClaims = realm.getClaimManager().
                                        getAllClaimMappings(UserCoreConstants.DEFAULT_CARBON_DIALECT);
                List<String> defaultClaimList = new ArrayList<String>();
                List<String> requiredClaimsList = new ArrayList<String>();
                for(ClaimMapping claimMapping : defaultClaims){
                    Claim claim = claimMapping.getClaim();
                    defaultClaimList.add(claim.getClaimUri());
                    if(claim.isRequired()){
                        requiredClaimsList.add(claim.getClaimUri());
                    }
                }
                userRealmInfo.setUserClaims(defaultClaimList.toArray(new String[defaultClaimList.size()]));
                userRealmInfo.setRequiredUserClaims(requiredClaimsList.
                                            toArray(new String[requiredClaimsList.size()]));
            }

            List<UserStoreInfo> storeInfoList = new ArrayList<UserStoreInfo>();
            List<String> domainNames = new ArrayList<String>();
            RealmConfiguration secondaryConfig = realmConfig;
            UserStoreManager secondaryManager = realm.getUserStoreManager();

			while (true) {

				secondaryConfig = secondaryManager.getRealmConfiguration();
				UserStoreInfo userStoreInfo = getUserStoreInfo(secondaryConfig, secondaryManager);
				if (secondaryConfig.isPrimary()) {
					userRealmInfo.setPrimaryUserStoreInfo(userStoreInfo);
				}
				storeInfoList.add(userStoreInfo);
				userRealmInfo.setBulkImportSupported(secondaryManager.isBulkImportSupported());
				String domainName = secondaryConfig
						.getUserStoreProperty(UserCoreConstants.RealmConfig.PROPERTY_DOMAIN_NAME);
				if (domainName != null && domainName.trim().length() > 0) {
					domainNames.add(domainName.toUpperCase());
				}
				secondaryManager = secondaryManager.getSecondaryUserStoreManager();
				if (secondaryManager == null) {
					break;
				}
			}

            if(storeInfoList.size() > 1){
                userRealmInfo.setMultipleUserStore(true);
            }

            userRealmInfo.setUserStoresInfo(storeInfoList.toArray(new UserStoreInfo[storeInfoList.size()]));
            userRealmInfo.setDomainNames(domainNames.toArray(new String[domainNames.size()]));

            String itemsPerPageString = realmConfig.getRealmProperty("MaxItemsPerUserMgtUIPage");
            int itemsPerPage = 15;
            try{
                itemsPerPage = Integer.parseInt(itemsPerPageString);
            } catch (Exception e) {
                // ignore
            }
            userRealmInfo.setMaxItemsPerUIPage(itemsPerPage);

            String maxPageInCacheString = realmConfig.getRealmProperty("MaxUserMgtUIPagesInCache");
            int maxPagesInCache = 6;
            try{
                maxPagesInCache = Integer.parseInt(maxPageInCacheString);
            } catch (Exception e) {
                // ignore
            }
            userRealmInfo.setMaxUIPagesInCache(maxPagesInCache);


            String enableUIPageCacheString = realmConfig.getRealmProperty("EnableUserMgtUIPageCache");
            boolean  enableUIPageCache = true;
            if("false".equals(enableUIPageCacheString)){
                enableUIPageCache = false;
            }
            userRealmInfo.setEnableUIPageCache(enableUIPageCache);

        } catch (Exception e) {
            // previously logged so logging not needed
            throw new UserAdminException(e.getMessage(), e);
        }

        return userRealmInfo;
    }

    private UserStoreInfo getUserStoreInfo(RealmConfiguration realmConfig,
                                            UserStoreManager manager) throws UserAdminException {
        try {

            UserStoreInfo info = new UserStoreInfo();

            info.setReadOnly(manager.isReadOnly());
            info.setPasswordsExternallyManaged(realmConfig.isPasswordsExternallyManaged());
            info.setPasswordRegEx(realmConfig
                    .getUserStoreProperty(UserCoreConstants.RealmConfig.PROPERTY_JS_REG_EX));
            info.setUserNameRegEx(
                realmConfig.getUserStoreProperty(UserCoreConstants.RealmConfig.PROPERTY_USER_NAME_JS_REG_EX));
            
            if (MultitenantUtils.isEmailUserName()) {
                String regEx = null;
                if ((regEx = realmConfig
                        .getUserStoreProperty(UserCoreConstants.RealmConfig.PROPERTY_USER_NAME_WITH_EMAIL_JS_REG_EX)) != null) {
                    info.setUserNameRegEx(regEx);
                }else{
                    info.setUserNameRegEx(UserCoreConstants.RealmConfig.EMAIL_VALIDATION_REGEX);
                }
            }
            
            info.setRoleNameRegEx(
                realmConfig.getUserStoreProperty(UserCoreConstants.RealmConfig.PROPERTY_ROLE_NAME_JS_REG_EX));
            info.setExternalIdP(realmConfig.
                                           getUserStoreProperty(UserCoreConstants.RealmConfig.PROPERTY_EXTERNAL_IDP));

            info.setBulkImportSupported(this.isBulkImportSupported());
            info.setDomainName(realmConfig.getUserStoreProperty(UserCoreConstants.RealmConfig.PROPERTY_DOMAIN_NAME));

            return info;
        } catch (UserStoreException e) {
            // previously logged so logging not needed
            throw new UserAdminException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }


    private boolean isBulkImportSupported() throws UserAdminException {
        try {
            UserStoreManager userStoreManager = this.realm.getUserStoreManager();
            if (userStoreManager != null) {
                return userStoreManager.isBulkImportSupported();
            } else {
                throw new UserAdminException("Unable to retrieve user store manager from realm.");
            }

        } catch (UserStoreException e) {
            throw new UserAdminException("An error occurred while retrieving user store from realm.", e);
        }
    }

    public void addUser(String userName, String password, String[] roles, ClaimValue[] claims,
            String profileName) throws UserAdminException {
        try {
            RealmConfiguration realmConfig = realm.getRealmConfiguration();
            if (realmConfig.
                           getUserStoreProperty(UserCoreConstants.RealmConfig.PROPERTY_EXTERNAL_IDP) != null) {
                throw new UserAdminException(
                                             "Please contact your external Identity Provider to add users");
            }

            // check whether login-in user has privilege to add user with admin privileges
            if (roles != null && roles.length > 0) {
                String loggedInUserName = getLoggedInUser();
                Arrays.sort(roles);
                boolean isRoleHasAdminPermission = false;
                for(String role : roles){
                    isRoleHasAdminPermission = realm.getAuthorizationManager().
                            isRoleAuthorized(role, "/permission", UserMgtConstants.EXECUTE_ACTION);
                    if(!isRoleHasAdminPermission){
                        isRoleHasAdminPermission = realm.getAuthorizationManager().
                            isRoleAuthorized(role, "/permission/admin", UserMgtConstants.EXECUTE_ACTION);
                    }

                    if(isRoleHasAdminPermission){
                        break;
                    }
                }

                if (isRoleHasAdminPermission &&
                                !realmConfig.getAdminUserName().equalsIgnoreCase(loggedInUserName)) {
                    log.warn("An attempt to assign user " + userName + " " +
                            "to Admin permission role by user : " + loggedInUserName);
                    throw new UserStoreException("You have not privilege to assign user to Admin permission role");
                }
            }
            UserStoreManager admin = realm.getUserStoreManager();
            Map<String, String> claimMap = new HashMap<String, String>();
            if (claims != null) {
                for (ClaimValue claimValue : claims) {
                    claimMap.put(claimValue.getClaimURI(), claimValue.getValue());
                }
            }
            admin.addUser(userName, password, roles, claimMap, profileName, false);
        } catch (UserStoreException e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }

    public void changePassword(String userName, String newPassword) throws UserAdminException {
        try {

            String loggedInUserName = getLoggedInUser();
            if(loggedInUserName != null && loggedInUserName.equalsIgnoreCase(userName)){
                log.warn("An attempt to change password with out providing old password : " +
                        loggedInUserName);
                throw new UserStoreException("An attempt to change password with out providing old password");
            }
            RealmConfiguration realmConfig = realm.getRealmConfiguration();
            if(realmConfig.getAdminUserName().equalsIgnoreCase(userName) &&
                                    !realmConfig.getAdminUserName().equalsIgnoreCase(loggedInUserName)){
                log.warn("An attempt to change password of Admin user by user : " + loggedInUserName);
                throw new UserStoreException("You have not privilege to change password of Admin user");
            }

            if(userName != null){
                boolean isUserHadAdminPermission;

                // check whether this user had admin permission
                isUserHadAdminPermission = realm.getAuthorizationManager().
                        isUserAuthorized(userName, "/permission", UserMgtConstants.EXECUTE_ACTION);
                if(!isUserHadAdminPermission){
                    isUserHadAdminPermission = realm.getAuthorizationManager().
                        isUserAuthorized(userName, "/permission/admin", UserMgtConstants.EXECUTE_ACTION);
                }

                if(isUserHadAdminPermission &&
                        !realmConfig.getAdminUserName().equalsIgnoreCase(loggedInUserName)){
                    log.warn("An attempt to change password of user has admin permission by user : " +
                                                                                    loggedInUserName);
                    throw new UserStoreException("You have not privilege to change password of user " +
                            "has admin permission");
                }
            }
            realm.getUserStoreManager().updateCredentialByAdmin(userName, newPassword);
        } catch (UserStoreException e) {
            // previously logged so logging not needed
            throw new UserAdminException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }

    public void deleteUser(String userName, Registry registry) throws UserAdminException {
        try {
            String loggedInUserName = getLoggedInUser();
            RealmConfiguration realmConfig = realm.getRealmConfiguration();
            if(realmConfig.getAdminUserName().equalsIgnoreCase(userName) &&
                            !realmConfig.getAdminUserName().equalsIgnoreCase(loggedInUserName)){
                log.warn("An attempt to delete Admin user by user : " + loggedInUserName);
                throw new UserStoreException("You have not privilege to delete Admin user");
            }

            if(userName != null){

                boolean isUserHadAdminPermission;

                // check whether this user had admin permission
                isUserHadAdminPermission = realm.getAuthorizationManager().
                        isUserAuthorized(userName, "/permission", UserMgtConstants.EXECUTE_ACTION);
                if(!isUserHadAdminPermission){
                    isUserHadAdminPermission = realm.getAuthorizationManager().
                        isUserAuthorized(userName, "/permission/admin", UserMgtConstants.EXECUTE_ACTION);
                }

                if(isUserHadAdminPermission &&
                    !realmConfig.getAdminUserName().equalsIgnoreCase(loggedInUserName)){
                    log.warn("An attempt to delete user user has Admin permission by user : " +
                                                                                    loggedInUserName);
                    throw new UserStoreException("You have not privilege to delete user has Admin permission");
                }
            }

            realm.getUserStoreManager().deleteUser(userName);
            String path = RegistryConstants.PROFILES_PATH + userName;
            if (registry.resourceExists(path)) {
                registry.delete(path);
            }
        } catch (RegistryException e) {
            String msg = "Error deleting user from registry " + e.getMessage();
            log.error(msg, e);
            throw new UserAdminException(msg, e);
        } catch (UserStoreException e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }

	public void addRole(String roleName, String[] userList, String[] permissions,
	                    boolean isSharedRole)            throws UserAdminException {
        try {

            String loggedInUserName = getLoggedInUser();
            if(permissions != null &&
                    !realm.getRealmConfiguration().getAdminUserName().equalsIgnoreCase(loggedInUserName)){
                Arrays.sort(permissions);
                if(Arrays.binarySearch(permissions, "/permission/admin") > -1 ||
                        Arrays.binarySearch(permissions, "/permission/admin/") > -1 ||
                        Arrays.binarySearch(permissions, "/permission") > -1 ||
                        Arrays.binarySearch(permissions, "/permission/") > -1 ||
                        Arrays.binarySearch(permissions, "/permission/protected") > -1 ||
                        Arrays.binarySearch(permissions, "/permission/protected/") > -1){
                    log.warn("An attempt to create role with admin permission by user " + loggedInUserName);
                    throw new UserStoreException("You have not privilege to create a role with Admin permission");
                }
            }

            UserStoreManager usAdmin = realm.getUserStoreManager();
            UserStoreManager secManager = null;

            if(roleName.contains("/")){
                secManager = usAdmin.
                        getSecondaryUserStoreManager(roleName.substring(0, roleName.indexOf("/")));

            } else {
                secManager= usAdmin;
            }

            if(secManager == null){
                throw new UserAdminException("Invalid Domain");           
            }

            if(!secManager.isReadOnly()
                    && ! "false".equals(secManager.getRealmConfiguration().
                    getUserStoreProperty(UserCoreConstants.RealmConfig.WRITE_GROUPS_ENABLED))){
				usAdmin.addRole(roleName,
				                userList,
				                ManagementPermissionUtil.getRoleUIPermissions(roleName, permissions),
				                isSharedRole);
            } else {
                throw new UserAdminException("Read only user store or Role creation is disabled");    
            }
        } catch (UserStoreException e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }

    public void addInternalRole(String roleName, String[] userList, String[] permissions)
            throws UserAdminException {
        try {

            String loggedInUserName = getLoggedInUser();
            if(permissions != null &&
                    !realm.getRealmConfiguration().getAdminUserName().equalsIgnoreCase(loggedInUserName)){
                Arrays.sort(permissions);
                if(Arrays.binarySearch(permissions, "/permission/admin") > -1 ||
                        Arrays.binarySearch(permissions, "/permission/admin/") > -1 ||
                        Arrays.binarySearch(permissions, "/permission") > -1 ||
                        Arrays.binarySearch(permissions, "/permission/") > -1 ||
                        Arrays.binarySearch(permissions, "/permission/protected")  > -1 ||
                        Arrays.binarySearch(permissions, "/permission/protected/")  > -1){
                    log.warn("An attempt to create role with admin permission by user " + loggedInUserName);
                    throw new UserStoreException("You have not privilege to create a role with Admin permission");
                }
            }

			UserStoreManager usAdmin = realm.getUserStoreManager();
			if (usAdmin instanceof AbstractUserStoreManager) {
				((AbstractUserStoreManager) usAdmin).addRole(UserCoreConstants.INTERNAL_DOMAIN
						+ UserCoreConstants.DOMAIN_SEPARATOR + roleName, userList, null, false);
			} else {
				throw new UserStoreException("Internal role can not be created");
			}
            // adding permission with internal domain name
			ManagementPermissionUtil.updateRoleUIPermission(UserCoreConstants.INTERNAL_DOMAIN
					+ UserCoreConstants.DOMAIN_SEPARATOR + roleName, permissions);
        } catch (UserStoreException e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }

    public void updateRoleName(String roleName, String newRoleName)
            throws UserAdminException {
        try {
            
            String loggedInUserName = getLoggedInUser();
            boolean isRoleHasAdminPermission;
            
            String roleWithoutDN = roleName.split(UserCoreConstants.TENANT_DOMAIN_COMBINER)[0];

            // check whether this role had admin permission
            isRoleHasAdminPermission = realm.getAuthorizationManager().
                    isRoleAuthorized(roleWithoutDN, "/permission", UserMgtConstants.EXECUTE_ACTION);
            if(!isRoleHasAdminPermission){
                isRoleHasAdminPermission = realm.getAuthorizationManager().
                    isRoleAuthorized(roleWithoutDN, "/permission/admin", UserMgtConstants.EXECUTE_ACTION);
            } 
            
            if(isRoleHasAdminPermission &&
                    !realm.getRealmConfiguration().getAdminUserName().equalsIgnoreCase(loggedInUserName)){
                log.warn("An attempt to rename role with admin permission by user " + loggedInUserName);
                throw new UserStoreException("You have not privilege to rename a role with Admin permission");
            }
            
            UserStoreManager usAdmin = realm.getUserStoreManager();
            usAdmin.updateRoleName(roleName, newRoleName);
        } catch (UserStoreException e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }

    public void deleteRole(String roleName) throws UserAdminException {
        try {

            String loggedInUserName = getLoggedInUser();
            boolean isRoleHasAdminPermission;

            // check whether this role had admin permission
            isRoleHasAdminPermission = realm.getAuthorizationManager().
                    isRoleAuthorized(roleName, "/permission", UserMgtConstants.EXECUTE_ACTION);
            if(!isRoleHasAdminPermission){
                isRoleHasAdminPermission = realm.getAuthorizationManager().
                    isRoleAuthorized(roleName, "/permission/admin", UserMgtConstants.EXECUTE_ACTION);
            }

            if(isRoleHasAdminPermission &&
                    !realm.getRealmConfiguration().getAdminUserName().equalsIgnoreCase(loggedInUserName)){
                log.warn("An attempt to delete role with admin permission by user " + loggedInUserName);
                throw new UserStoreException("You have not privilege to delete a role with Admin permission");                
            }

            realm.getUserStoreManager().deleteRole(roleName);
        } catch (UserStoreException e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }

    public FlaggedName[] getUsersOfRole(String roleName, String filter, int limit) throws UserAdminException {
        try {
        	
			int index = roleName != null ? roleName.indexOf("/") : -1;
			boolean domainProvided = index > 0;

			String domain = domainProvided ? roleName.substring(0, index) : null;

			if (domain != null) {
                if(filter != null && !filter.toLowerCase().startsWith(domain.toLowerCase()) &&
                                        !UserCoreConstants.INTERNAL_DOMAIN.equalsIgnoreCase(domain)){
				    filter = domain + "/" + filter;
                }
			}
            
            if(domain == null && limit != 0){
                if(filter != null){
                    filter = "/" + filter;
                } else {
                    filter = "/*";
                }
            }

			UserStoreManager usMan = realm.getUserStoreManager();
			String[] usersOfRole = usMan.getUserListOfRole(roleName);
			Arrays.sort(usersOfRole);
            Map<String,Integer> userCount = new HashMap<String,Integer>();
            if(limit == 0){
                filter = filter.replace("*", ".*");
                Pattern pattern = Pattern.compile(filter, Pattern.CASE_INSENSITIVE);
                List<FlaggedName> flaggedNames = new ArrayList<FlaggedName>();
                for (String anUsersOfRole : usersOfRole) {
                    //check if display name is present in the user name
                    int combinerIndex = anUsersOfRole.indexOf("|");
                    Matcher matcher;
                    if(combinerIndex > 0){
                        matcher = pattern.matcher(anUsersOfRole.substring(combinerIndex + 1));
                    } else {
                        matcher = pattern.matcher(anUsersOfRole);
                    }
                    if (!matcher.matches()) {
                        continue;
                    }

                    FlaggedName fName = new FlaggedName();
                    //fName.setItemName(usersOfRole[i]);
                    fName.setSelected(true);
                    if (combinerIndex > 0) { //if display name is appended
                        fName.setItemName(anUsersOfRole.substring(0, combinerIndex));
                        fName.setItemDisplayName(anUsersOfRole.substring(combinerIndex + 1));
                    } else {
                        //if only user name is present
                        fName.setItemName(anUsersOfRole);
                        fName.setItemDisplayName(anUsersOfRole);
                    }
                    if(domain != null && !UserCoreConstants.INTERNAL_DOMAIN.equalsIgnoreCase(domain)){
                        if(usMan.getSecondaryUserStoreManager(domain)!= null &&
                                (usMan.getSecondaryUserStoreManager(domain).isReadOnly() ||
                                    "false".equals(usMan.getSecondaryUserStoreManager(domain).getRealmConfiguration().
                                    getUserStoreProperty(UserCoreConstants.RealmConfig.WRITE_GROUPS_ENABLED)))){
                            fName.setEditable(false);
                        }else{
                            fName.setEditable(true);
                        }
                    } else {
                        if(usMan.isReadOnly() || (usMan.getSecondaryUserStoreManager(domain) != null &&
                                "false".equals(usMan.getRealmConfiguration().
                                    getUserStoreProperty(UserCoreConstants.RealmConfig.WRITE_GROUPS_ENABLED)))){
                            fName.setEditable(false);
                        }else{
                            fName.setEditable(true);
                        }
                    }
                    if(domain != null){
                        if(userCount.containsKey(domain)){
                            userCount.put(domain,userCount.get(domain)+1);
                        }else{
                            userCount.put(domain,1);
                        }
                    }else{
                        if(userCount.containsKey(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME)){
                            userCount.put(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME,
                                    userCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME)+1);
                        }else{
                            userCount.put(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME,1);
                        }
                    }
                    flaggedNames.add(fName);
                }
                String exceededDomains = "";
                boolean isPrimaryExceeding = false;
                Map<String,Integer> maxUserListCount = ((AbstractUserStoreManager)realm.getUserStoreManager()).getMaxListCount(UserCoreConstants.RealmConfig.PROPERTY_MAX_USER_LIST);
                String[] domains = userCount.keySet().toArray(new String[userCount.keySet().size()]);
                for(int i=0;i<domains.length;i++){
                    if(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME.equals(domains[i])){
                        if(userCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME).
                                equals(maxUserListCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME))){
                            isPrimaryExceeding = true;
                        }
                        continue;
                    }
                    if(userCount.get(domains[i]).equals(maxUserListCount.get(domains[i].toUpperCase()))){
                        exceededDomains += domains[i];
                        if(i != domains.length - 1){
                            exceededDomains += ":";
                        }
                    }
                }
                FlaggedName flaggedName = new FlaggedName();
                if(isPrimaryExceeding){
                    flaggedName.setItemName("true");
                }else{
                    flaggedName.setItemName("false");
                }
                flaggedName.setItemDisplayName(exceededDomains);
                flaggedNames.add(flaggedName);
                return flaggedNames.toArray(new FlaggedName[flaggedNames.size()]);
            }

			String[] userNames = usMan.listUsers(filter, limit);
			FlaggedName[] flaggedNames = new FlaggedName[userNames.length+1];
			for (int i = 0; i < userNames.length; i++) {
				FlaggedName fName = new FlaggedName();
				fName.setItemName(userNames[i]);
				if (Arrays.binarySearch(usersOfRole, userNames[i]) > -1) {
					fName.setSelected(true);
                }
                //check if display name is present in the user name
                int combinerIndex = userNames[i].indexOf("|");
                if (combinerIndex > 0) { //if display name is appended
                    fName.setItemName(userNames[i].substring(0, combinerIndex));
                    fName.setItemDisplayName(userNames[i].substring(combinerIndex + 1));
                } else {
                    //if only user name is present
                    fName.setItemName(userNames[i]);
                }
                if(domain != null && !UserCoreConstants.INTERNAL_DOMAIN.equalsIgnoreCase(domain)){
                    if(usMan.getSecondaryUserStoreManager(domain)!= null &&
                            (usMan.getSecondaryUserStoreManager(domain).isReadOnly() ||
                        "false".equals(usMan.getSecondaryUserStoreManager(domain).getRealmConfiguration().
                            getUserStoreProperty(UserCoreConstants.RealmConfig.WRITE_GROUPS_ENABLED)))){
                        fName.setEditable(false);
                    }else{
                        fName.setEditable(true);
                    }
                } else {
                    if(usMan.isReadOnly() || (usMan.getSecondaryUserStoreManager(domain) != null &&
                        "false".equals(usMan.getRealmConfiguration().
                            getUserStoreProperty(UserCoreConstants.RealmConfig.WRITE_GROUPS_ENABLED)))){
                        fName.setEditable(false);
                    }else{
                        fName.setEditable(true);
                    }
                }
                if(domain != null){
                    if(userCount.containsKey(domain)){
                        userCount.put(domain,userCount.get(domain)+1);
                    }else{
                        userCount.put(domain,1);
                    }
                }else{
                    if(userCount.containsKey(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME)){
                        userCount.put(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME,
                                userCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME)+1);
                    }else{
                        userCount.put(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME,1);
                    }
                }
                flaggedNames[i] = fName;
            }
            String exceededDomains = "";
            boolean isPrimaryExceeding = false;
            Map<String,Integer> maxUserListCount = ((AbstractUserStoreManager)realm.getUserStoreManager()).getMaxListCount(UserCoreConstants.RealmConfig.PROPERTY_MAX_USER_LIST);
            String[] domains = userCount.keySet().toArray(new String[userCount.keySet().size()]);
            for(int i=0;i<domains.length;i++){
                if(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME.equals(domains[i])){
                    if(userCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME).
                            equals(maxUserListCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME))){
                        isPrimaryExceeding = true;
                    }
                    continue;
                }
                if(userCount.get(domains[i]).equals(maxUserListCount.get(domains[i].toUpperCase()))){
                    exceededDomains += domains[i];
                    if(i != domains.length - 1){
                        exceededDomains += ":";
                    }
                }
            }
            FlaggedName flaggedName = new FlaggedName();
            if(isPrimaryExceeding){
                flaggedName.setItemName("true");
            }else{
                flaggedName.setItemName("false");
            }
            flaggedName.setItemDisplayName(exceededDomains);
            flaggedNames[flaggedNames.length-1] = flaggedName;
            return flaggedNames;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }

    public FlaggedName[] getRolesOfUser(String userName, String filter, int limit) throws UserAdminException {
        try {

            int index = userName != null ? userName.indexOf("/") : -1;
            boolean domainProvided = index > 0;
            String domain = domainProvided ? userName.substring(0, index) : null;

            if(filter == null){
                 filter = "*";
            }

            UserStoreManager admin = realm.getUserStoreManager();
            String[] userRoles = ((AbstractUserStoreManager)admin).getRoleListOfUser(userName);
            Map<String,Integer> userCount = new HashMap<String,Integer>();

            if(limit == 0){
                
                // want to check whether role is internal of not
                // no limit?
                String modifiedFilter = filter;
                if(filter.contains(CarbonConstants.DOMAIN_SEPARATOR)){
                    modifiedFilter = filter.
                                    substring(filter.indexOf(CarbonConstants.DOMAIN_SEPARATOR) + 1);
                }

                String[] hybridRoles = ((AbstractUserStoreManager) admin).getHybridRoles(modifiedFilter);

                if(hybridRoles != null){
                    Arrays.sort(hybridRoles);
                }

                // filter with regexp
                modifiedFilter = modifiedFilter.replace("*", ".*");

                Pattern pattern = Pattern.compile(modifiedFilter, Pattern.CASE_INSENSITIVE);

                List<FlaggedName> flaggedNames = new ArrayList<FlaggedName>();

                for (String role : userRoles) {
                    String matchingRole = role;
                    String roleDomain = null;
                    if(matchingRole.contains(CarbonConstants.DOMAIN_SEPARATOR)){
                        matchingRole = matchingRole.
                                substring(matchingRole.indexOf(CarbonConstants.DOMAIN_SEPARATOR) + 1);
                        if(filter.contains(CarbonConstants.DOMAIN_SEPARATOR)){
                            roleDomain = role.
                                    substring(0, role.indexOf(CarbonConstants.DOMAIN_SEPARATOR) + 1);
                        }
                    }
                    if (hybridRoles != null && Arrays.binarySearch(hybridRoles, role) > -1) {
                        Matcher matcher = pattern.matcher(matchingRole);
                        if (!(matcher.matches() && (roleDomain == null ||
                            filter.toLowerCase().startsWith(roleDomain.toLowerCase())))) {
                            continue;
                        }
                    } else {
                        Matcher matcher = pattern.matcher(matchingRole);
                        if (!(matcher.matches() && (roleDomain== null ||
                            filter.toLowerCase().startsWith(roleDomain.toLowerCase())))) {
                            continue;
                        }
                    }

                    FlaggedName fName = new FlaggedName();
                    mapEntityName(role, fName, admin);
                    fName.setSelected(true);
                    if (domain != null && !UserCoreConstants.INTERNAL_DOMAIN.equalsIgnoreCase(domain)) {
                        if ((admin.getSecondaryUserStoreManager(domain).isReadOnly() ||
                            (admin.getSecondaryUserStoreManager(domain).getRealmConfiguration().
                            getUserStoreProperty(UserCoreConstants.RealmConfig.WRITE_GROUPS_ENABLED) != null &&
                            admin.getSecondaryUserStoreManager(domain).getRealmConfiguration().
                            getUserStoreProperty(UserCoreConstants.RealmConfig.WRITE_GROUPS_ENABLED).equals("false"))) &&
                            hybridRoles != null && Arrays.binarySearch(hybridRoles, role) < 0) {

                            fName.setEditable(false);
                        } else {
                            fName.setEditable(true);
                        }
                    } else {
                        if ((admin.isReadOnly() || (admin.getRealmConfiguration().
                                getUserStoreProperty(UserCoreConstants.RealmConfig.WRITE_GROUPS_ENABLED) != null &&
                            admin.getRealmConfiguration().
                                getUserStoreProperty(UserCoreConstants.RealmConfig.WRITE_GROUPS_ENABLED).equals("false"))) &&
                            hybridRoles != null && Arrays.binarySearch(hybridRoles, role) < 0) {
                            fName.setEditable(false);
                        } else {
                            fName.setEditable(true);
                        }
                    }
                    if(domain != null){
                        if(userCount.containsKey(domain)){
                            userCount.put(domain,userCount.get(domain)+1);
                        }else{
                            userCount.put(domain,1);
                        }
                    }else{
                        if(userCount.containsKey(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME)){
                            userCount.put(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME,
                                    userCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME)+1);
                        }else{
                            userCount.put(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME,1);
                        }
                    }
                    flaggedNames.add(fName);
                }
                String exceededDomains = "";
                boolean isPrimaryExceeding = false;
                Map<String,Integer> maxUserListCount = ((AbstractUserStoreManager)realm.getUserStoreManager()).getMaxListCount(UserCoreConstants.RealmConfig.PROPERTY_MAX_ROLE_LIST);
                String[] domains = userCount.keySet().toArray(new String[userCount.keySet().size()]);
                for(int i=0;i<domains.length;i++){
                    if(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME.equals(domains[i])){
                        if(userCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME).
                                equals(maxUserListCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME))){
                            isPrimaryExceeding = true;
                        }
                        continue;
                    }
                    if(userCount.get(domains[i]).equals(maxUserListCount.get(domains[i].toUpperCase()))){
                        exceededDomains += domains[i];
                        if(i != domains.length - 1){
                            exceededDomains += ":";
                        }
                    }
                }
                FlaggedName flaggedName = new FlaggedName();
                if(isPrimaryExceeding){
                    flaggedName.setItemName("true");
                }else{
                    flaggedName.setItemName("false");
                }
                flaggedName.setItemDisplayName(exceededDomains);
                flaggedNames.add(flaggedName);
                return flaggedNames.toArray(new FlaggedName[flaggedNames.size()]);
            }

            String[] internalRoles = null;
            String[] externalRoles = null;

            // only internal roles are retrieved.
            if(filter.toLowerCase().startsWith(UserCoreConstants.INTERNAL_DOMAIN.toLowerCase())){
                if(admin instanceof AbstractUserStoreManager){
                    filter = filter.substring(filter.indexOf(CarbonConstants.DOMAIN_SEPARATOR) + 1);
                    internalRoles = ((AbstractUserStoreManager) admin).getHybridRoles(filter);
                } else {
                    internalRoles = admin.getHybridRoles();
                }
            } else {    
                // filter has a domain value
                if(domain != null && filter.toLowerCase().startsWith(domain.toLowerCase() +
                                                                CarbonConstants.DOMAIN_SEPARATOR)){
                    if(admin instanceof AbstractUserStoreManager){
                        externalRoles = ((AbstractUserStoreManager) admin).getRoleNames(filter, limit,
                                true, true, true);
                    } else {
                        externalRoles = admin.getRoleNames();
                    }
                } else {

                    if(admin instanceof AbstractUserStoreManager){
                        internalRoles = ((AbstractUserStoreManager) admin).getHybridRoles(filter);
                    } else {
                        internalRoles = admin.getHybridRoles();
                    }

                    if(domain == null){
                        filter = CarbonConstants.DOMAIN_SEPARATOR + filter;
                    } else {
                        filter = domain + CarbonConstants.DOMAIN_SEPARATOR + filter;
                    }

                    if(admin instanceof AbstractUserStoreManager){
                        externalRoles = ((AbstractUserStoreManager) admin).getRoleNames(filter, limit,
                                true, true, true);
                    } else {
                        externalRoles = admin.getRoleNames();
                    }
                }
            }

            List<FlaggedName> flaggedNames = new ArrayList<FlaggedName>();

            Arrays.sort(userRoles);
            if(externalRoles != null){
                for (String externalRole : externalRoles) {
                    FlaggedName fname = new FlaggedName();
                    
    				mapEntityName(externalRole, fname, admin);
                    fname.setDomainName(domain);
                    if (Arrays.binarySearch(userRoles, externalRole) > -1) {
                        fname.setSelected(true);
                    }
                    if(domain != null){
                        UserStoreManager secManager = admin.getSecondaryUserStoreManager(domain);
                        if(secManager.isReadOnly() ||
                                "false".equals(secManager.getRealmConfiguration().
                                getUserStoreProperty(UserCoreConstants.RealmConfig.WRITE_GROUPS_ENABLED))){
                            fname.setEditable(false);
                        } else {
                            fname.setEditable(true);
                        }
                    } else {
                        if(admin.isReadOnly() || "false".equals(admin.getRealmConfiguration().
                                getUserStoreProperty(UserCoreConstants.RealmConfig.WRITE_GROUPS_ENABLED))){
                            fname.setEditable(false);
                        } else {
                            fname.setEditable(true);
                        }
                    }
                    if(domain != null){
                        if(userCount.containsKey(domain)){
                            userCount.put(domain,userCount.get(domain)+1);
                        }else{
                            userCount.put(domain,1);
                        }
                    }else{
                        if(userCount.containsKey(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME)){
                            userCount.put(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME,
                                    userCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME)+1);
                        }else{
                            userCount.put(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME,1);
                        }
                    }
                    flaggedNames.add(fname);
                }
            }

            if(internalRoles != null){
                for (String internalRole : internalRoles) {
                    FlaggedName fname = new FlaggedName();
                    fname.setItemName(internalRole);
                    fname.setDomainName(UserCoreConstants.INTERNAL_DOMAIN);
                    if (Arrays.binarySearch(userRoles, internalRole) > -1) {
                        fname.setSelected(true);
                    }
                    fname.setEditable(true);
                    flaggedNames.add(fname);
                }
            }
            String exceededDomains = "";
            boolean isPrimaryExceeding = false;
            Map<String,Integer> maxUserListCount = ((AbstractUserStoreManager)realm.
                    getUserStoreManager()).getMaxListCount(UserCoreConstants.RealmConfig.PROPERTY_MAX_ROLE_LIST);
            String[] domains = userCount.keySet().toArray(new String[userCount.keySet().size()]);
            for(int i=0;i<domains.length;i++){
                if(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME.equals(domains[i])){
                    if(userCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME).
                            equals(maxUserListCount.get(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME))){
                        isPrimaryExceeding = true;
                    }
                    continue;
                }
                if(userCount.get(domains[i]).equals(maxUserListCount.get(domains[i].toUpperCase()))){
                    exceededDomains += domains[i];
                    if(i != domains.length - 1){
                        exceededDomains += ":";
                    }
                }
            }
            FlaggedName flaggedName = new FlaggedName();
            if(isPrimaryExceeding){
                flaggedName.setItemName("true");
            }else{
                flaggedName.setItemName("false");
            }
            flaggedName.setItemDisplayName(exceededDomains);
            flaggedNames.add(flaggedName);
            return flaggedNames.toArray(new FlaggedName[flaggedNames.size()]);
        } catch (Exception e) {
            log.error(e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }

    public void updateUsersOfRole(String roleName, FlaggedName[] userList)
            throws UserAdminException {

        try {

            if (CarbonConstants.REGISTRY_ANONNYMOUS_ROLE_NAME.equalsIgnoreCase(roleName)) {
                log.error("Security Alert! Carbon anonymous role is being manipulated");
                throw new UserStoreException("Invalid data");// obscure error
                                                             // message
            }

            if (realm.getRealmConfiguration().getEveryOneRoleName().equalsIgnoreCase(roleName)) {
                log.error("Security Alert! Carbon Everyone role is being manipulated");
                throw new UserStoreException("Invalid data");// obscure error
                                                             // message
            }

            UserStoreManager admin = realm.getUserStoreManager();
            String[] oldUserList = admin.getUserListOfRole(roleName);
            List<String> list = new ArrayList<String>();
            if(oldUserList != null){
                for(String value : oldUserList){
                    int combinerIndex = value.indexOf("|");
                    if (combinerIndex > 0) {
                        list.add(value.substring(0, combinerIndex));
                    } else {
                        list.add(value);
                    }
                }
                oldUserList = list.toArray(new String[list.size()]);
            }

            Arrays.sort(oldUserList);

            List<String> delUsers = new ArrayList<String>();
            List<String> addUsers = new ArrayList<String>();

            for (FlaggedName fName : userList) {
                boolean isSelected = fName.isSelected();
                String userName = fName.getItemName();
                if (CarbonConstants.REGISTRY_ANONNYMOUS_USERNAME.equalsIgnoreCase(userName)) {
                    log.error("Security Alert! Carbon anonymous user is being manipulated");
                    return;
                }
                int oldindex = Arrays.binarySearch(oldUserList, userName);
                if (oldindex > -1 && !isSelected) {
                    // deleted
                    delUsers.add(userName);
                } else if (oldindex < 0 && isSelected) {
                    // added
                    addUsers.add(userName);
                }
            }

            String loggedInUserName = getLoggedInUser();
            RealmConfiguration realmConfig = realm.getRealmConfiguration();

            boolean isRoleHasAdminPermission = realm.getAuthorizationManager().
                    isRoleAuthorized(roleName, "/permission/", UserMgtConstants.EXECUTE_ACTION);
            if(!isRoleHasAdminPermission){
                isRoleHasAdminPermission = realm.getAuthorizationManager().
                        isRoleAuthorized(roleName, "/permission/admin/", UserMgtConstants.EXECUTE_ACTION);
            }

            if ((realmConfig.getAdminRoleName().equalsIgnoreCase(roleName) || isRoleHasAdminPermission) &&
                                !realmConfig.getAdminUserName().equalsIgnoreCase(loggedInUserName)) {
                log.warn("An attempt to add or remove users from Admin role by user : "
                                                                                + loggedInUserName);
                throw new UserStoreException("Can not add or remove user from Admin permission role");
            }

            String[] delUsersArray = null;
            String[] addUsersArray = null;

            String[] users = realm.getUserStoreManager().getUserListOfRole(roleName);

            if(users == null){
                Arrays.sort(users);
            }
            if(delUsers != null && users != null){
                delUsersArray = delUsers.toArray(new String[delUsers.size()]);
                Arrays.sort(delUsersArray);
                if (Arrays.binarySearch(delUsersArray, loggedInUserName) > -1
                        && Arrays.binarySearch(users, loggedInUserName) > -1
                        && !realmConfig.getAdminUserName().equalsIgnoreCase(loggedInUserName)) {
                    log.warn("An attempt to remove from role : " + roleName + " by user :" + loggedInUserName);
                    throw new UserStoreException("Can not remove yourself from role : " + roleName);
                }
            }

            if(addUsers != null){
                addUsersArray = addUsers.toArray(new String[addUsers.size()]);
            }
            admin.updateUserListOfRole(roleName, delUsersArray, addUsersArray);
        } catch (UserStoreException e) {
            // previously logged so logging not needed
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }

    public void updateRolesOfUser(String userName, String[] roleList) throws UserAdminException {
        try {

            if (CarbonConstants.REGISTRY_ANONNYMOUS_USERNAME.equalsIgnoreCase(userName)) {
                log.error("Security Alert! Carbon anonymous user is being manipulated");
                throw new UserAdminException("Invalid data");// obscure error
                                                             // message
            }

            if(roleList != null){
                String loggedInUserName = getLoggedInUser();
                RealmConfiguration realmConfig = realm.getRealmConfiguration();
                Arrays.sort(roleList);
                String[] roles = realm.getUserStoreManager().getRoleListOfUser(userName);
                if(roles != null){
                    Arrays.sort(roles);
                }

                boolean isUserHasAdminPermission = false;
                String adminPermissionRole = null;
                for(String role : roles){
                    isUserHasAdminPermission = realm.getAuthorizationManager().
                            isRoleAuthorized(role, "/permission", UserMgtConstants.EXECUTE_ACTION);
                    if(!isUserHasAdminPermission){
                        isUserHasAdminPermission = realm.getAuthorizationManager().
                            isRoleAuthorized(role, "/permission/admin", UserMgtConstants.EXECUTE_ACTION);
                    }

                    if(isUserHasAdminPermission){
                        break;
                    }
                }

                boolean isRoleHasAdminPermission;
                for(String roleName : roleList){
                    isRoleHasAdminPermission = realm.getAuthorizationManager().
                        isRoleAuthorized(roleName, "/permission", UserMgtConstants.EXECUTE_ACTION);
                    if(!isRoleHasAdminPermission){
                        isRoleHasAdminPermission = realm.getAuthorizationManager().
                        isRoleAuthorized(roleName, "/permission/admin", UserMgtConstants.EXECUTE_ACTION);
                    }

                    if(isRoleHasAdminPermission){
                        adminPermissionRole = roleName;
                        break;
                    }
                }

                if(roles == null || Arrays.binarySearch(roles, realmConfig.getAdminRoleName()) < 0){
                    if ((Arrays.binarySearch(roleList, realmConfig.getAdminRoleName()) > -1 ||
                            (!isUserHasAdminPermission && adminPermissionRole != null)) &&
                                !realmConfig.getAdminUserName().equalsIgnoreCase(loggedInUserName)){
                        log.warn("An attempt to add users to Admin permission role by user : " +
                                                                                loggedInUserName);
                        throw new UserStoreException("Can not add users to Admin permission role");
                    }
                } else {
                    if (Arrays.binarySearch(roleList, realmConfig.getAdminRoleName()) < 0 &&
                            !realmConfig.getAdminUserName().equalsIgnoreCase(loggedInUserName)) {
                        log.warn("An attempt to remove users from Admin role by user : " +
                                                                                    loggedInUserName);
                        throw new UserStoreException("Can not remove users from Admin role");
                    }
                }
            }

            UserStoreManager admin = realm.getUserStoreManager();
            String[] oldRoleList = admin.getRoleListOfUser(userName);
            Arrays.sort(roleList);
            Arrays.sort(oldRoleList);

            List<String> delRoles = new ArrayList<String>();
            List<String> addRoles = new ArrayList<String>();

            for (String name : roleList) {
                int oldindex = Arrays.binarySearch(oldRoleList, name);
                if (oldindex < 0) {
                    addRoles.add(name);
                }
            }

            for (String name : oldRoleList) {
                int newindex = Arrays.binarySearch(roleList, name);
                if (newindex > 0) {
                    if (realm.getRealmConfiguration().getEveryOneRoleName().equalsIgnoreCase(name)) {
                        log.error("Security Alert! Carbon everyone role is being manipulated");
                        throw new UserAdminException("Invalid data");// obscure
                                                                     // error
                                                                     // message
                    }
                    delRoles.add(name);
                }
            }

            admin.updateRoleListOfUser(userName, delRoles.toArray(new String[delRoles.size()]),
                    addRoles.toArray(new String[addRoles.size()]));
        } catch (UserStoreException e) {
            // previously logged so logging not needed
            throw new UserAdminException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }

    public void updateUsersOfRole(String roleName, String[] newUsers, String[] deleteUsers)
            throws UserAdminException {

        try {

            String loggedInUserName = getLoggedInUser();

            if (CarbonConstants.REGISTRY_ANONNYMOUS_ROLE_NAME.equalsIgnoreCase(roleName)) {
                log.error("Security Alert! Carbon anonymous role is being manipulated by user " + loggedInUserName);
                throw new UserStoreException("Invalid data");
            }

            if (realm.getRealmConfiguration().getEveryOneRoleName().equalsIgnoreCase(roleName)) {
                log.error("Security Alert! Carbon Everyone role is being manipulated by user " + loggedInUserName);
                throw new UserStoreException("Invalid data");
            }

            boolean isRoleHasAdminPermission = realm.getAuthorizationManager().
                    isRoleAuthorized(roleName, "/permission/", UserMgtConstants.EXECUTE_ACTION);
            if(!isRoleHasAdminPermission){
                isRoleHasAdminPermission = realm.getAuthorizationManager().
                        isRoleAuthorized(roleName, "/permission/admin/", UserMgtConstants.EXECUTE_ACTION);
            }

            RealmConfiguration realmConfig = realm.getRealmConfiguration();
            if ((realmConfig.getAdminRoleName().equalsIgnoreCase(roleName) ||
                    isRoleHasAdminPermission) &&
                                    !realmConfig.getAdminUserName().equalsIgnoreCase(loggedInUserName)) {
                log.warn("An attempt to add or remove users from Admin role by user : "
                                                                                + loggedInUserName);
                throw new UserStoreException("You have not privilege to add or remove user " +
                        "from Admin permission role");
            }

            if(deleteUsers != null){
                Arrays.sort(deleteUsers);
                if(realmConfig.getAdminRoleName().equalsIgnoreCase(roleName) &&
                        Arrays.binarySearch(deleteUsers, realmConfig.getAdminUserName()) > -1){
                    log.warn("An attempt to remove Admin user from Admin role by user : "
                                                                                    + loggedInUserName);
                    throw new UserStoreException("Can not remove Admin user " +
                            "from Admin role");                    
                }
            }

            UserStoreManager admin = realm.getUserStoreManager();
            String[] oldUserList = admin.getUserListOfRole(roleName);
            List<String> list = new ArrayList<String>();
            if(oldUserList != null){
                for(String value : oldUserList){
                    int combinerIndex = value.indexOf("|");
                    if (combinerIndex > 0) {
                        list.add(value.substring(0, combinerIndex));
                    } else {
                        list.add(value);
                    }
                }
                oldUserList = list.toArray(new String[list.size()]);
                Arrays.sort(oldUserList);
            }


            List<String> delUser = new ArrayList<String>();
            List<String> addUsers = new ArrayList<String>();

            if(oldUserList != null){
                if(newUsers != null){
                    for(String name : newUsers){
                        if (Arrays.binarySearch(oldUserList, name) < 0) {
                            addUsers.add(name);
                        }
                    }
                    newUsers =  addUsers.toArray(new String[addUsers.size()]);
                }

                if(deleteUsers != null){
                    for(String name : deleteUsers){
                        if (Arrays.binarySearch(oldUserList, name) > -1) {
                            delUser.add(name);
                        }
                    }
                    deleteUsers = delUser.toArray(new String[delUser.size()]);
                }
            } else {
                deleteUsers = null;
            }


           admin.updateUserListOfRole(roleName, deleteUsers, newUsers);

        } catch (UserStoreException e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }

    public void updateRolesOfUser(String userName, String[] newRoles, String[] deletedRoles) throws UserAdminException {
        try {

            String loggedInUserName = getLoggedInUser();

            if (CarbonConstants.REGISTRY_ANONNYMOUS_USERNAME.equalsIgnoreCase(userName)) {
                log.error("Security Alert! Carbon anonymous user is being manipulated by user "
                        + loggedInUserName);
                throw new UserAdminException("Invalid data");
            }

            if(deletedRoles != null){
                for (String name : deletedRoles) {
                    if (realm.getRealmConfiguration().getEveryOneRoleName().equalsIgnoreCase(name)) {
                        log.error("Security Alert! Carbon everyone role is being manipulated by user "
                                + loggedInUserName);
                        throw new UserAdminException("Invalid data");
                    }
                    if (realm.getRealmConfiguration().getAdminRoleName().equalsIgnoreCase(name) &&
                        realm.getRealmConfiguration().getAdminUserName().equalsIgnoreCase(userName)) {
                        log.error("Can not remove admin user from admin role "
                                + loggedInUserName);
                        throw new UserAdminException("Can not remove admin user from admin role");
                    }
                }
            }

            RealmConfiguration realmConfig = realm.getRealmConfiguration();

            if(!realmConfig.getAdminUserName().equalsIgnoreCase(loggedInUserName)){

                boolean isUserHadAdminPermission;

                // check whether this user had admin permission
                isUserHadAdminPermission = realm.getAuthorizationManager().
                        isUserAuthorized(userName, "/permission", UserMgtConstants.EXECUTE_ACTION);
                if(!isUserHadAdminPermission){
                    isUserHadAdminPermission = realm.getAuthorizationManager().
                        isUserAuthorized(userName, "/permission/admin", UserMgtConstants.EXECUTE_ACTION);
                }

                if(newRoles != null){
                    boolean isRoleHasAdminPermission = false;
                    // check whether new roles has admin permission
                    for(String roleName : newRoles){
                        
                        if(roleName.equalsIgnoreCase(realmConfig.getAdminRoleName())){
                            log.warn("An attempt to add users to Admin permission role by user : " +
                                    loggedInUserName);
                            throw new UserStoreException("Can not add users to Admin permission role");
                        }
                        
                        isRoleHasAdminPermission = realm.getAuthorizationManager().
                            isRoleAuthorized(roleName, "/permission", UserMgtConstants.EXECUTE_ACTION);
                        if(!isRoleHasAdminPermission){
                            isRoleHasAdminPermission = realm.getAuthorizationManager().
                            isRoleAuthorized(roleName, "/permission/admin", UserMgtConstants.EXECUTE_ACTION);
                        }

                        if(isRoleHasAdminPermission){
                            break;
                        }
                    }

                    if(!isUserHadAdminPermission && isRoleHasAdminPermission){
                        log.warn("An attempt to add users to Admin permission role by user : " +
                                                                                loggedInUserName);
                        throw new UserStoreException("Can not add users to Admin permission role");
                    }
                }

                if(deletedRoles != null){

                    boolean isRemoveRoleHasAdminPermission = false;
                    // check whether delete roles has admin permission
                    for(String roleName : deletedRoles){
                        isRemoveRoleHasAdminPermission = realm.getAuthorizationManager().
                            isRoleAuthorized(roleName, "/permission", UserMgtConstants.EXECUTE_ACTION);
                        if(!isRemoveRoleHasAdminPermission){
                            isRemoveRoleHasAdminPermission = realm.getAuthorizationManager().
                            isRoleAuthorized(roleName, "/permission/admin", UserMgtConstants.EXECUTE_ACTION);
                        }

                        if(isRemoveRoleHasAdminPermission){
                            break;
                        }
                    }

                    if(isUserHadAdminPermission && isRemoveRoleHasAdminPermission){
                        log.warn("An attempt to remove users from Admin role by user : " +
                                                                                    loggedInUserName);
                        throw new UserStoreException("Can not remove users from Admin role");
                    }
                }
            }

            realm.getUserStoreManager().updateRoleListOfUser(userName, deletedRoles , newRoles);

        } catch (UserStoreException e) {
            // previously logged so loggin
            // g not needed
            throw new UserAdminException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }

    public UIPermissionNode getAllUIPermissions(int tenantId)
            throws UserAdminException {

        UIPermissionNode nodeRoot;
        Collection regRoot;
        try {
            Registry registry = UserMgtDSComponent.getRegistryService().getGovernanceSystemRegistry();           
            if (tenantId == MultitenantConstants.SUPER_TENANT_ID) {
                if (CarbonContext.getThreadLocalCarbonContext().getTenantId() != MultitenantConstants.SUPER_TENANT_ID) {
                    log.error("Illegal access attempt");
                    throw new UserStoreException("Illegal access attempt");
                }
                regRoot = (Collection) registry.get(UserMgtConstants.UI_PERMISSION_ROOT);
                String displayName = regRoot.getProperty(UserMgtConstants.DISPLAY_NAME);
                nodeRoot = new UIPermissionNode(UserMgtConstants.UI_PERMISSION_ROOT, displayName);
            } else {
                regRoot = (Collection) registry.get(UserMgtConstants.UI_ADMIN_PERMISSION_ROOT);
                String displayName = regRoot.getProperty(UserMgtConstants.DISPLAY_NAME);
                nodeRoot = new UIPermissionNode(UserMgtConstants.UI_ADMIN_PERMISSION_ROOT,
                        displayName);
            }
            buildUIPermissionNode(regRoot, nodeRoot, registry, null, null, null);
            return nodeRoot;
        } catch (UserStoreException e) {
            // previously logged so logging not needed
            throw new UserAdminException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }

    public UIPermissionNode getRolePermissions(String roleName, int tenantId)
            throws UserAdminException {
        UIPermissionNode nodeRoot;
        Collection regRoot;
        try {
            Registry registry = UserMgtDSComponent.getRegistryService().getGovernanceSystemRegistry();
            if (tenantId == MultitenantConstants.SUPER_TENANT_ID) {
                regRoot = (Collection) registry.get(UserMgtConstants.UI_PERMISSION_ROOT);
                String displayName = regRoot.getProperty(UserMgtConstants.DISPLAY_NAME);
                nodeRoot = new UIPermissionNode(UserMgtConstants.UI_PERMISSION_ROOT, displayName);
            } else {
                regRoot = (Collection) registry.get(UserMgtConstants.UI_ADMIN_PERMISSION_ROOT);
                String displayName = regRoot.getProperty(UserMgtConstants.DISPLAY_NAME);
                nodeRoot = new UIPermissionNode(UserMgtConstants.UI_ADMIN_PERMISSION_ROOT,
                        displayName);
            }
            buildUIPermissionNode(regRoot, nodeRoot, registry, realm.getAuthorizationManager(),
                    roleName, null);
            return nodeRoot;
        } catch (UserStoreException e) {
            // previously logged so logging not needed
            throw new UserAdminException(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }

    public void setRoleUIPermission(String roleName, String[] rawResources)
            throws UserAdminException {
        try {
			if (((AbstractUserStoreManager) realm.getUserStoreManager()).isOthersSharedRole(roleName)) {
				throw new UserAdminException("Logged in user is not authorized to assign " +
                        "permissions to a role belong to another tenant");
			}
            if (realm.getRealmConfiguration().getAdminRoleName().equalsIgnoreCase(roleName)) {
                String msg = "UI permissions of Admin is not allowed to change";
                log.error(msg);
                throw new UserAdminException(msg);
            }

            String loggedInUserName = getLoggedInUser();
            if(rawResources != null &&
                    !realm.getRealmConfiguration().getAdminUserName().equalsIgnoreCase(loggedInUserName)){
                Arrays.sort(rawResources);
                if(Arrays.binarySearch(rawResources, "/permission/admin") > -1 ||
                        Arrays.binarySearch(rawResources, "/permission/protected")  > -1 ||
                                Arrays.binarySearch(rawResources, "/permission") > -1){
                    log.warn("An attempt to Assign admin permission for role by user : " +
                                                                                loggedInUserName);
                    throw new UserStoreException("Can not assign Admin for permission role");
                }
            }

            String[] optimizedList = UserCoreUtil.optimizePermissions(rawResources);
            AuthorizationManager authMan = realm.getAuthorizationManager();
            authMan.clearRoleActionOnAllResources(roleName, UserMgtConstants.EXECUTE_ACTION);
            for (String path : optimizedList) {
                authMan.authorizeRole(roleName, path, UserMgtConstants.EXECUTE_ACTION);
            }
        } catch (UserStoreException e) {
            log.error(e.getMessage(), e);
            throw new UserAdminException(e.getMessage(), e);
        }
    }

    public void bulkImportUsers(String fileName, InputStream inStream, String defaultPassword)
            throws UserAdminException {
        try {
            BulkImportConfig config = new BulkImportConfig(inStream, fileName);
            if (defaultPassword != null && defaultPassword.trim().length() > 0) {
                config.setDefaultPassword(defaultPassword.trim());
            }
            UserStoreManager userStore = this.realm.getUserStoreManager();
            if (fileName.endsWith("csv")) {
                CSVUserBulkImport csvAdder = new CSVUserBulkImport(config);
                csvAdder.addUserList(userStore);
            } else if (fileName.endsWith("xls") || fileName.endsWith("xlsx")) {
                ExcelUserBulkImport excelAdder = new ExcelUserBulkImport(config);
                excelAdder.addUserList(userStore);
            } else {
                throw new UserAdminException("Unsupported format");
            }
        } catch (UserStoreException e) {
            // previously logged so logging not needed
            throw new UserAdminException(e.getMessage(), e);
        }

    }

    public void changePasswordByUser(String oldPassword, String newPassword)
            throws UserAdminException {
        try {
            UserStoreManager userStore = this.realm.getUserStoreManager();
            HttpServletRequest request = (HttpServletRequest) MessageContext
                    .getCurrentMessageContext().getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
            HttpSession httpSession = request.getSession(false);
            String userName = (String) httpSession.getAttribute(ServerConstants.USER_LOGGED_IN);

            int indexOne;
			indexOne = userName.indexOf("/");
            if (indexOne < 0) {
                /*if domain is not provided, this can be the scenario where user from a secondary user store
                logs in without domain name and tries to change his own password*/
                String domainName = (String) httpSession.getAttribute("logged_in_domain");
                
                if (domainName != null) {
                    userName = domainName + "/" + userName;
                }
            }
            userStore.updateCredential(userName, newPassword, oldPassword);
        } catch (UserStoreException e) {
            // previously logged so logging not needed
            throw new UserAdminException(e.getMessage(), e);
        }
    }


    public boolean hasMultipleUserStores() throws UserAdminException {
        try {
            return realm.getUserStoreManager().getSecondaryUserStoreManager() != null;
        } catch (UserStoreException e) {
            log.error(e);
            throw new UserAdminException("Unable to check for multiple user stores");
        }
    }

    private void buildUIPermissionNode(Collection parent, UIPermissionNode parentNode,
            Registry registry, AuthorizationManager authMan, String roleName, String userName)
            throws RegistryException, UserStoreException {

        boolean isSelected = false;
        if (roleName != null) {
            isSelected = authMan.isRoleAuthorized(roleName, parentNode.getResourcePath(),
                    UserMgtConstants.EXECUTE_ACTION);
        } else if (userName != null) {
            isSelected = authMan.isUserAuthorized(userName, parentNode.getResourcePath(),
                    UserMgtConstants.EXECUTE_ACTION);
        }
        if(isSelected){
            buildUIPermissionNodeAllSelected(parent, parentNode, registry);
            parentNode.setSelected(true);
        }  else {
             buildUIPermissionNodeNotAllSelected(parent, parentNode,registry,
                     authMan, roleName, userName);
        }
    }

    private void buildUIPermissionNodeAllSelected (Collection parent, UIPermissionNode parentNode,
            Registry registry)
            throws RegistryException, UserStoreException {

        String [] children = parent.getChildren();
        UIPermissionNode[] childNodes = new UIPermissionNode[children.length];
        for (int i = 0 ; i < children.length; i++) {
            String child = children[i];
            Resource resource = registry.get(child);

            childNodes[i] = getUIPermissionNode(resource, registry, true);
            if (resource instanceof Collection) {
                buildUIPermissionNodeAllSelected((Collection) resource, childNodes[i], registry);
            }
        }
        parentNode.setNodeList(childNodes);
    }

    private void buildUIPermissionNodeNotAllSelected(Collection parent, UIPermissionNode parentNode,
            Registry registry, AuthorizationManager authMan, String roleName, String userName)
            throws RegistryException, UserStoreException {

        String [] children = parent.getChildren();
        UIPermissionNode[] childNodes = new UIPermissionNode[children.length];

        for (int i = 0 ; i < children.length; i++) {
            String child = children[i];
            Resource resource = registry.get(child);
            boolean isSelected = false;
            if (roleName != null) {
                isSelected = authMan.isRoleAuthorized(roleName, child,
                        UserMgtConstants.EXECUTE_ACTION);
            } else if (userName != null) {
                isSelected = authMan.isUserAuthorized(userName, child,
                        UserMgtConstants.EXECUTE_ACTION);
            }
            childNodes[i] = getUIPermissionNode(resource, registry, isSelected);
            if (resource instanceof Collection) {
                buildUIPermissionNodeNotAllSelected((Collection) resource, childNodes[i],
                        registry, authMan,roleName, userName);
            }
        }
        parentNode.setNodeList(childNodes);
    }

    private UIPermissionNode getUIPermissionNode(Resource resource, Registry registry,
            boolean isSelected) throws RegistryException {
        String displayName = resource.getProperty(UserMgtConstants.DISPLAY_NAME);
        return new UIPermissionNode(resource.getPath(), displayName, isSelected);
    }

    /**
     * Gets logged in user of the server
     *
     * @return  user name
     */
    private String getLoggedInUser(){

        return CarbonContext.getThreadLocalCarbonContext().getUsername();
    }
    
	private void mapEntityName(String entityName, FlaggedName fName,
	                           UserStoreManager userStoreManager) {
		if (entityName.contains(UserCoreConstants.TENANT_DOMAIN_COMBINER)) {
			String[] nameAndDn = entityName.split(UserCoreConstants.TENANT_DOMAIN_COMBINER);
			fName.setItemName(nameAndDn[0]);
			fName.setDn(nameAndDn[1]);

			// TODO remove abstract user store
			fName.setShared(((AbstractUserStoreManager) userStoreManager).isOthersSharedRole(entityName));
			if (fName.isShared()) {
				fName.setItemDisplayName(UserCoreConstants.SHARED_ROLE_TENANT_SEPERATOR +
				                         fName.getItemName());
			}

		} else {
			fName.setItemName(entityName);
		}

	}

	public boolean isSharedRolesEnabled() throws UserAdminException {
		UserStoreManager userManager;
		try {
			userManager = realm.getUserStoreManager();   // TODO remove abstract user store
			return ((AbstractUserStoreManager)userManager).isSharedGroupEnabled();
		} catch (UserStoreException e) {
			log.error(e);
			throw new UserAdminException("Unable to check shared role enabled", e);
		}
	}
}
