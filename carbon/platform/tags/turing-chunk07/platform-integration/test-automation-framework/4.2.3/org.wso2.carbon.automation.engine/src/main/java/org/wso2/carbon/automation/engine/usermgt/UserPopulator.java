/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.automation.engine.usermgt;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.automation.engine.FrameworkConstants;
import org.wso2.carbon.automation.engine.adminclients.AuthenticationAdminClient;
import org.wso2.carbon.automation.engine.adminclients.TenantManagementServiceClient;
import org.wso2.carbon.automation.engine.adminclients.UserManagementAdminServiceClient;
import org.wso2.carbon.automation.engine.context.ContextConstants;
import org.wso2.carbon.automation.engine.context.InitialContextPublisher;
import org.wso2.carbon.automation.engine.context.usercontext.Tenant;
import org.wso2.carbon.automation.engine.context.usercontext.Tenants;
import org.wso2.carbon.automation.engine.context.usercontext.UserManagerContext;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserPopulator {
    private static final Log log = LogFactory.getLog(UserPopulator.class);
    Boolean isMultiTenantMode;
    String sessionCookie = null;
    String backendURL = null;
    List<Tenants> tenantsList;
    UserManagerContext userManagerContext;
    TenantManagementServiceClient tenantStub;

    /**
     * @param sessionCookie   session cookie of the tenant domain session
     * @param backendURL      backend url of the service
     * @param multiTenantMode true/false values for whether multi tenant mode enabled or disabled
     */
    public UserPopulator(String sessionCookie, String backendURL, Boolean multiTenantMode)
            throws AxisFault {
        this.sessionCookie = sessionCookie;
        this.backendURL = backendURL;
        this.isMultiTenantMode = multiTenantMode;
        tenantStub = new TenantManagementServiceClient(backendURL, sessionCookie);
        userManagerContext = InitialContextPublisher.getContext().getUserManagerContext();
        //get the tenant list
        if (! isMultiTenantMode) {
            tenantsList = new ArrayList<Tenants>();
            tenantsList.add(userManagerContext.getTenantsNode(FrameworkConstants.
                    SUPER_TENANT_DOMAIN_NAME));
        } else {
            tenantsList = new ArrayList<Tenants>(userManagerContext.getAllTenantsNodes().values());
        }
    }

    public void populateUsers(String productName, String instanceName) throws Exception {
        String tenantAdminSession;
        UserManagementAdminServiceClient userManagementClient;
        for (Tenants tenants : tenantsList) {
            if (! tenants.getDomain().equals(FrameworkConstants.SUPER_TENANT_DOMAIN_NAME)) {
                tenantStub.addTenant(tenants.getDomain(), tenants.getAdminTenant().getPassword(),
                        tenants.getAdminTenant().getUserName(),
                        FrameworkConstants.TENANT_USAGE_PLAN_DEMO);
            }
            Thread.sleep(2000);
            log.info("Start populating users for " + tenants.getDomain());
            tenantAdminSession = login(tenants.getAdminTenant().getUserName(), tenants.getDomain(),
                    tenants.getAdminTenant().getPassword(), backendURL,
                    InitialContextPublisher.getContext().
                    getPlatformContext().getProductGroup(productName).
                            getInstanceByName(instanceName).getHost());
            List<Tenant> userList = tenants.getAllTenants();
            Iterator<Tenant> userIterator = userList.iterator();
            userManagementClient = new UserManagementAdminServiceClient
                    (backendURL, tenantAdminSession);
            while (userIterator.hasNext()) {
                Tenant tenant = userIterator.next();
                if (! userManagementClient.getUserList().contains(tenant.getUserName())) {
                    userManagementClient.addUser(tenant.getUserName(), tenant.getPassword(),
                            new String[]{FrameworkConstants.ADMIN_ROLE}, null);
                    log.info("Populated " + tenant.getUserName());
                } else {
                    if (! tenant.getKey().equals(ContextConstants.USER_MANAGEMENT_ADMIN_TENANT)) {
                        log.info(tenant.getUserName() + " is already in " + tenants.getDomain());
                    }
                }
            }
        }
        Thread.sleep(2000);
    }
//    public void deleteUsers(String productName, String instanceName) throws Exception {
//        String tenantAdminSession;
//        UserManagementAdminServiceClient userManagementClient;
//        for (Tenants tenant : tenantsList) {
//            tenantAdminSession = login(tenant.getTenantAdmin().getUserName(), tenant.getDomain(),
//                    tenant.getTenantAdmin().getPassword(), backendURL,
// InitialContextPublisher.getContext().
//                    getPlatformContext().getProductGroup(productName).
// getInstanceByName(instanceName).getHost());
//            userManagementClient = new UserManagementAdminServiceClient(backendURL,
// tenantAdminSession);
//            List<User> userList = tenant.getAllUsers();
//            Iterator<User> userIterator = userList.iterator();
//            while ((userIterator.hasNext())) {
//                User tenantUser = userIterator.next();
//                if(userManagementClient.getUserList().contains(tenantUser.getUserName())) {
//                userManagementClient.deleteUser(tenantUser.getUserName());
//                    log.info(tenantUser.getUserName()+" user deleted successfully");
//                }
//            }
////            if (! tenant.getDomain().equals(FrameworkConstants.SUPER_TENANT_DOMAIN_NAME)) {
////                tenantStub.deleteTenant(tenant.getDomain());
////            }
//        }
//    }

    protected static String login(String userName, String domain, String password, String backendUrl,
                                  String hostName)
            throws RemoteException, LoginAuthenticationExceptionException {
        AuthenticationAdminClient loginClient = new AuthenticationAdminClient(backendUrl);
        return loginClient.login(domain, userName, password, hostName);
    }
}
