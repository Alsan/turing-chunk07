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
package org.wso2.carbon.user.core.tenant;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.user.api.TenantMgtConfiguration;
import org.wso2.carbon.user.core.UserCoreConstants;
import org.wso2.carbon.user.core.UserStoreException;
import org.wso2.carbon.user.api.RealmConfiguration;
import org.wso2.carbon.user.core.ldap.LDAPConnectionContext;
import org.wso2.carbon.user.core.ldap.LDAPConstants;
import org.wso2.carbon.user.core.util.UserCoreUtil;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.sql.DataSource;
import java.util.Map;

/**
 * This class is the tenant manager for any external LDAP and based on the "ou" partitioning
 * per tenant under one DIT.
 */
public class CommonHybridLDAPTenantManager extends JDBCTenantManager {
    private static Log logger = LogFactory.getLog(CommonHybridLDAPTenantManager.class);
    private LDAPConnectionContext ldapConnectionSource;
    //TODO move the following configurations and constants to relevant files.

    private TenantMgtConfiguration tenantMgtConfig = null;
    private RealmConfiguration realmConfig = null;
    //constants
    private static final String USER_PASSWORD_ATTRIBUTE_NAME = "userPassword";
    private static final String EMAIL_ATTRIBUTE_NAME = "mail";
    private static final String SN_ATTRIBUTE_NAME = "sn";
    private static final String CN_ATTRIBUTE_NAME = "cn";

    public CommonHybridLDAPTenantManager(OMElement omElement, Map<String, Object> properties)
            throws Exception {
        super(omElement, properties);

        tenantMgtConfig = (TenantMgtConfiguration) properties.get(
                UserCoreConstants.TENANT_MGT_CONFIGURATION);

        realmConfig = (RealmConfiguration) properties.get(UserCoreConstants.REALM_CONFIGURATION);
        if (realmConfig == null) {
            throw new UserStoreException("Tenant Manager can not function without a bootstrap realm config");
        }
        
        if (ldapConnectionSource == null) {
        	ldapConnectionSource = new LDAPConnectionContext(realmConfig);
        }

    }

    public CommonHybridLDAPTenantManager(DataSource dataSource, String superTenantDomain) {
        super(dataSource, superTenantDomain);
    }

    /**
     * Do necessary things in LDAP when adding a tenant.
     *
     * @param tenant Tenant to be added.
     * @return The tenant id.
     * @throws UserStoreException If an error occurred while creating the tenant.
     */
    @Override
    public int addTenant(org.wso2.carbon.user.api.Tenant tenant) throws UserStoreException {
        int tenantID = super.addTenant(tenant);
        tenant.setId(tenantID);

        DirContext initialDirContext = null;
        try {
            initialDirContext = this.ldapConnectionSource.getContext();
            //create per tenant context and its user store and group store with admin related entries.
            createOrganizationalUnit(tenant.getDomain(), (Tenant) tenant, initialDirContext);
        } finally {
            closeContext(initialDirContext);
        }


        return tenantID;
    }

    /**
     * Create a space for tenant in LDAP.
     *
     * @param orgName           Organization name.
     * @param tenant            The tenant
     * @param initialDirContext The directory connection.
     * @throws UserStoreException If an error occurred while creating.
     */
    protected void createOrganizationalUnit(String orgName, Tenant tenant,
                                            DirContext initialDirContext)
            throws UserStoreException {
        //e.g: ou=wso2.com
        String partitionDN = tenantMgtConfig.getTenantStoreProperties().get(
                UserCoreConstants.TenantMgtConfig.PROPERTY_ROOT_PARTITION);
        createOrganizationalContext(partitionDN, orgName, initialDirContext);

        //create user store
        String organizationNameAttribute = tenantMgtConfig.getTenantStoreProperties().get(
                UserCoreConstants.TenantMgtConfig.PROPERTY_ORG_SUB_CONTEXT_ATTRIBUTE);
        //eg:o=cse.org,dc=wso2,dc=com
        String dnOfOrganizationalContext = organizationNameAttribute + "=" + orgName + "," +
                                           partitionDN;
        createOrganizationalSubContext(dnOfOrganizationalContext,
                                       LDAPConstants.USER_CONTEXT_NAME, initialDirContext);

        //create group store
        createOrganizationalSubContext(dnOfOrganizationalContext,
                                       LDAPConstants.GROUP_CONTEXT_NAME, initialDirContext);

        //create admin entry
        String orgSubContextAttribute = tenantMgtConfig.getTenantStoreProperties().get(
                UserCoreConstants.TenantMgtConfig.PROPERTY_ORG_SUB_CONTEXT_ATTRIBUTE);
        //eg: ou=users,o=cse.org,dc=wso2,dc=com
        String dnOfUserContext = orgSubContextAttribute + "=" + LDAPConstants.USER_CONTEXT_NAME
                                 + "," + dnOfOrganizationalContext;
        String dnOfUserEntry = createAdminEntry(dnOfUserContext, tenant, initialDirContext);

        //create admin group if write ldap group is enabled
        if (("true").equals(realmConfig.getUserStoreProperty(
                LDAPConstants.WRITE_EXTERNAL_ROLES))) {
            //construct dn of group context: eg:ou=groups,o=cse.org,dc=wso2,dc=com
            String dnOfGroupContext = orgSubContextAttribute + "=" +
                                      LDAPConstants.GROUP_CONTEXT_NAME + "," +
                                      dnOfOrganizationalContext;
            createAdminGroup(dnOfGroupContext, dnOfUserEntry, initialDirContext);
        }
    }

    /**
     * Create main context corresponding to tenant.
     *
     * @param rootDN            Root domain name.
     * @param orgName           Organization name
     * @param initialDirContext The directory connection.
     * @throws UserStoreException If an error occurred while creating context.
     */
    protected void createOrganizationalContext(String rootDN, String orgName,
                                               DirContext initialDirContext)
            throws UserStoreException {

        DirContext subContext = null;
        DirContext organizationalContext = null;
        try {

            //get the connection context for rootDN
            subContext = (DirContext) initialDirContext.lookup(rootDN);

            Attributes contextAttributes = new BasicAttributes(true);
            //create organizational object class attribute
            Attribute objectClass = new BasicAttribute(LDAPConstants.OBJECT_CLASS_NAME);
            objectClass.add(
                    tenantMgtConfig.getTenantStoreProperties().get(
                            UserCoreConstants.TenantMgtConfig.PROPERTY_ORGANIZATIONAL_OBJECT_CLASS));
            contextAttributes.put(objectClass);
            //create organizational name attribute
            String organizationalNameAttribute = tenantMgtConfig.getTenantStoreProperties().get(
                    UserCoreConstants.TenantMgtConfig.PROPERTY_ORGANIZATIONAL_ATTRIBUTE);
            Attribute organization =
                    new BasicAttribute(organizationalNameAttribute);
            organization.add(orgName);
            contextAttributes.put(organization);
            //construct organization rdn.
            String rdnOfOrganizationalContext = organizationalNameAttribute + "=" + orgName;
            if (logger.isDebugEnabled()) {
                logger.debug("Adding sub context: " + rdnOfOrganizationalContext + " under " +
                             rootDN + " ...");
            }
            //create organization sub context
            organizationalContext = subContext.createSubcontext(rdnOfOrganizationalContext, contextAttributes);
            if (logger.isDebugEnabled()) {
                logger.debug("Sub context: " + rdnOfOrganizationalContext + " was added under "
                             + rootDN + " successfully.");
            }

        } catch (NamingException e) {
            String errorMsg = "Error occurred while adding the organizational unit " +
                              "sub context.";
            logger.error(errorMsg, e);
            throw new UserStoreException(errorMsg, e);
        } finally {
            closeContext(organizationalContext);
            closeContext(subContext);
        }
    }

    protected void closeContext(DirContext ldapContext) {
        if (ldapContext != null) {
            try {
                ldapContext.close();
            } catch (NamingException e) {
                logger.error("Error closing sub context.", e);
            }
        }
    }

    /**
     * Create sub contexts under the tenant's main context.
     *
     * @param dnOfParentContext    domain name of the parent context.
     * @param nameOfCurrentContext name of the current context.
     * @param initialDirContext    The directory connection.
     * @throws UserStoreException if an error occurs while creating context.
     */
    protected void createOrganizationalSubContext(String dnOfParentContext,
                                                  String nameOfCurrentContext,
                                                  DirContext initialDirContext)
            throws UserStoreException {

        DirContext subContext = null;
        DirContext organizationalContext = null;

        try {
            //get the connection for tenant's main context
            subContext = (DirContext) initialDirContext.lookup(dnOfParentContext);

            Attributes contextAttributes = new BasicAttributes(true);
            //create sub unit object class attribute
            Attribute objectClass = new BasicAttribute(LDAPConstants.OBJECT_CLASS_NAME);
            objectClass.add(tenantMgtConfig.getTenantStoreProperties().get(
                    UserCoreConstants.TenantMgtConfig.PROPERTY_ORG_SUB_CONTEXT_OBJ_CLASS));
            contextAttributes.put(objectClass);

            //create org sub unit name attribute
            String orgSubUnitAttributeName = tenantMgtConfig.getTenantStoreProperties().get(
                    UserCoreConstants.TenantMgtConfig.PROPERTY_ORG_SUB_CONTEXT_ATTRIBUTE);
            Attribute organizationSubUnit = new BasicAttribute(orgSubUnitAttributeName);
            organizationSubUnit.add(nameOfCurrentContext);
            contextAttributes.put(organizationSubUnit);

            //construct the rdn of org sub context
            String rdnOfOrganizationalContext = orgSubUnitAttributeName + "=" +
                                                nameOfCurrentContext;
            if (logger.isDebugEnabled()) {
                logger.debug("Adding sub context: " + rdnOfOrganizationalContext + " under " +
                             dnOfParentContext + " ...");
            }
            //create sub context
            organizationalContext = subContext.createSubcontext(rdnOfOrganizationalContext, contextAttributes);
            if (logger.isDebugEnabled()) {
                logger.debug("Sub context: " + rdnOfOrganizationalContext + " was added under "
                             + dnOfParentContext + " successfully.");
            }

        } catch (NamingException e) {
            String errorMsg = "Error occurred while adding the organizational unit " +
                              "sub context.";
            logger.error(errorMsg, e);
            throw new UserStoreException(errorMsg, e);
        } finally {
            closeContext(organizationalContext);
            closeContext(subContext);
        }
    }

    protected String createAdminEntry(String dnOfUserContext, Tenant tenant,
                                      DirContext initialDirContext)
            throws UserStoreException {
        String userDN = null;
        DirContext organizationalUsersContext = null;
        try {
            //get connection to tenant's user context
            organizationalUsersContext = (DirContext) initialDirContext.lookup(
                    dnOfUserContext);
            Attributes userAttributes = new BasicAttributes(true);

            //create person object class attribute
            Attribute objClass = new BasicAttribute(LDAPConstants.OBJECT_CLASS_NAME);
            objClass.add(realmConfig.getUserStoreProperty(LDAPConstants.USER_ENTRY_OBJECT_CLASS));
            userAttributes.put(objClass);

            //create user password attribute
            Attribute password = new BasicAttribute(USER_PASSWORD_ATTRIBUTE_NAME);
            String passwordToStore = UserCoreUtil.getPasswordToStore(
                    tenant.getAdminPassword(),
                    realmConfig.getUserStoreProperties().get(LDAPConstants.PASSWORD_HASH_METHOD),
                    isKDCEnabled());
            password.add(passwordToStore);
            userAttributes.put(password);

            //create mail attribute
            Attribute adminEmail = new BasicAttribute(EMAIL_ATTRIBUTE_NAME);
            adminEmail.add(tenant.getEmail());
            userAttributes.put(adminEmail);

            //create compulsory attribute: sn-last name
            Attribute lastName = new BasicAttribute(SN_ATTRIBUTE_NAME);
            lastName.add(tenant.getAdminLastName());
            userAttributes.put(lastName);

            //read user name attribute in user-mgt.xml
            String userNameAttribute = realmConfig.getUserStoreProperty(
                    LDAPConstants.USER_NAME_ATTRIBUTE);

            //if user name attribute is not cn, add it to attribute list
            if (!(CN_ATTRIBUTE_NAME.equals(userNameAttribute))) {
                Attribute firstName = new BasicAttribute(CN_ATTRIBUTE_NAME);
                firstName.add(tenant.getAdminFirstName());
                userAttributes.put(firstName);
            }
            String userRDN = userNameAttribute + "=" + tenant.getAdminName();
            organizationalUsersContext.bind(userRDN, null, userAttributes);
            userDN = userRDN + "," + dnOfUserContext;
            //return (userRDN + dnOfUserContext);
        } catch (NamingException e) {
            String errorMsg = "Error occurred while creating Admin entry";
            logger.error(errorMsg, e);
            throw new UserStoreException(errorMsg, e);
        } finally {
            closeContext(organizationalUsersContext);
        }

        return userDN;
    }

    protected void createAdminGroup(String dnOfGroupContext, String adminUserDN,
                                    DirContext initialDirContext)
            throws UserStoreException {
        //create set of attributes required to create admin group
        Attributes adminGroupAttributes = new BasicAttributes(true);
        //admin entry object class
        Attribute objectClassAttribute = new BasicAttribute(LDAPConstants.OBJECT_CLASS_NAME);
        objectClassAttribute.add(realmConfig.getUserStoreProperty(
                LDAPConstants.GROUP_ENTRY_OBJECT_CLASS));
        adminGroupAttributes.put(objectClassAttribute);

        //group name attribute
        String groupNameAttributeName = realmConfig.getUserStoreProperty(
                LDAPConstants.GROUP_NAME_ATTRIBUTE);
        Attribute groupNameAttribute = new BasicAttribute(groupNameAttributeName);
        String adminRoleName = realmConfig.getAdminRoleName();
        groupNameAttribute.add(UserCoreUtil.removeDomainFromName(adminRoleName));
        adminGroupAttributes.put(groupNameAttribute);

        //membership attribute
        Attribute membershipAttribute = new BasicAttribute(realmConfig.getUserStoreProperty(
                LDAPConstants.MEMBERSHIP_ATTRIBUTE));
        membershipAttribute.add(adminUserDN);
        adminGroupAttributes.put(membershipAttribute);

        DirContext groupContext = null;
        try {
            groupContext = (DirContext) initialDirContext.lookup(dnOfGroupContext);
            String rdnOfAdminGroup = groupNameAttributeName + "=" + UserCoreUtil.removeDomainFromName(adminRoleName);
            groupContext.bind(rdnOfAdminGroup, null, adminGroupAttributes);

        } catch (NamingException e) {
            String errorMessage = "Error occurred while creating the admin group.";
            logger.error(errorMessage);
            throw new UserStoreException(errorMessage, e);
        } finally {
            closeContext(groupContext);
        }
    }

    private boolean isKDCEnabled() {
        return UserCoreUtil.isKdcEnabled(realmConfig);
    }
}
