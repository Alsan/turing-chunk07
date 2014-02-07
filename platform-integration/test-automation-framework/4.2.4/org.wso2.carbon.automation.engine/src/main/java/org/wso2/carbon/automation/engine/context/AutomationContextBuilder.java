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
package org.wso2.carbon.automation.engine.context;

import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.automation.engine.adminclients.AuthenticationAdminClient;
import org.wso2.carbon.automation.engine.context.configurationcontext.ConfigurationContext;
import org.wso2.carbon.automation.engine.context.contextenum.Platforms;
import org.wso2.carbon.automation.engine.context.environmentcontext.EnvironmentContext;
import org.wso2.carbon.automation.engine.context.platformcontext.Instance;
import org.wso2.carbon.automation.engine.context.usercontext.Tenant;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * The builder class for the automation context based on the automation.xml
 */
public class AutomationContextBuilder {
    AutomationContext automationContext;
    AutomationContextFactory automationContextFactory;
    String productGroup = null;
    String instanceName = null;
    String domain = null;
    String userId = null;

    public AutomationContextBuilder(String productGroup, String instanceName) {
        automationContextFactory = new AutomationContextFactory();
        this.productGroup = productGroup;
        this.instanceName = instanceName;
    }

    /**
     * Builds the environment with the given domain with either as tenant admin or tenant user
     *
     * @param domain           The Domain configured in automation.xml
     * @param runAsTenantAdmin Whether the test is running as a super tenant or not.
     */
    public void build(String domain, boolean runAsTenantAdmin)
            throws SAXException, URISyntaxException, IOException,
            XMLStreamException, ConfigurationMismatchException {
        automationContext = automationContextFactory.getAutomationContext();
        this.userId = ContextConstants.TENANT_ADMIN_KEY;
        automationContextFactory.createAutomationContext(productGroup, instanceName,
                domain, ContextConstants.TENANT_ADMIN_KEY);
    }

    /**
     * Run as a specific user defined in a tenant domain
     *
     * @param domain    The Domain configured in automation.xml
     * @param tenantKey The key of the user expect
     */
    public void build(String domain, String tenantKey)
            throws SAXException, URISyntaxException, IOException,
            XMLStreamException, ConfigurationMismatchException {
        this.domain = domain;
        this.userId = tenantKey;
        automationContextFactory.createAutomationContext(productGroup, instanceName,
                domain, tenantKey);
        automationContext = automationContextFactory.getAutomationContext();
    }

    public String getRunningDomain() {
        return domain;
    }

    /**
     * Logs with initiated user and returns the session cookie for the respective session.
     *
     * @return sessionCookie
     */
    public String login() throws RemoteException, LoginAuthenticationExceptionException {
        String sessionCookie;
        AuthenticationAdminClient authenticationAdminClient
                = new AuthenticationAdminClient(automationContext.getEnvironmentContext()
                .getEnvironmentConfigurations().getBackEndUrl());
        Tenant tenant = automationContext.getUserManagerContext().
                getTenantsNode(domain).getTenant(userId);
        sessionCookie = authenticationAdminClient.
                login(domain, tenant.getUserName(), tenant.getPassword(),
                automationContext.getPlatformContext().getProductGroup(productGroup)
                        .getInstanceByName(instanceName).getHost());
        return sessionCookie;
    }

    /**
     * Returns the platform Instance assigned for the test case
     *
     * @return Instance
     */
    private Instance getInstance() {
        return automationContext.getPlatformContext()
                .getProductGroup(productGroup).getInstanceByName(instanceName);
    }

    /**
     * Returns the User Instance assigned for the test case
     *
     * @return User
     */
    private Tenant getTenant() {
        return automationContext.getUserManagerContext().getTenantsNode(domain).getTenant(userId);
    }

    /**
     * Returns Environment Context generated based on the Context
     *
     * @return EnvironmentContext
     */
    private EnvironmentContext getEnvironmentContext() {
        return automationContext.getEnvironmentContext();
    }

    private Tenant getTenant(String domain, String tenantUserKey) {
        return automationContext.getUserManagerContext().
                getTenantsNode(domain).getTenant(tenantUserKey);
    }

    private Instance getInstanceByGroup()
            throws SAXException, URISyntaxException, ConfigurationMismatchException,
            IOException, XMLStreamException {
        AutomationContext basicContext;
        Instance instance;
        basicContext = automationContextFactory.getBasicContext();
        ConfigurationContext configuration = basicContext.getConfigurationContext();
        List<Instance> lbManagerInstanceList = basicContext.getPlatformContext()
                .getProductGroup(productGroup).getAllLBManagerInstances();
        List<Instance> managerInstanceList = basicContext.getPlatformContext()
                .getProductGroup(productGroup).getManagerInstances();
        List<Instance> instanceList = basicContext.getPlatformContext()
                .getProductGroup(productGroup).getAllInstances();
        List<Instance> lbList = basicContext.getPlatformContext()
                .getProductGroup(productGroup).getAllLoadBalanceInstances();
        //If the execution mode is platform it looks for whether instance group is clustered
        if (configuration.getConfiguration().getExecutionEnvironment()
                .equals(Platforms.platform.name())) {
           /*if clustered default instance is manager fronted LB
            if no manger fronted LB is assigned it will go for a lb instance
            otherwise it will take general manager instance
            if manager instance in null it goes for normal instance*/
            if (basicContext.getPlatformContext().getProductGroup(productGroup)
                    .isClusteringEnabled()) {
                if (! lbManagerInstanceList.isEmpty()) {
                    instance = lbManagerInstanceList.get(instanceList.size() - 1);
                } else if (! managerInstanceList.isEmpty()) {
                    instance = managerInstanceList.get(instanceList.size() - 1);
                } else {
                    instance = instanceList.get(instanceList.size() - 1);
                }
            } else {
                /*if not clustered default will go for a lb if lb is null
                * selection would be instance*/
                if (! lbManagerInstanceList.isEmpty()) {
                    instance = lbList.get(instanceList.size() - 1);
                } else {
                    instance = instanceList.get(instanceList.size() - 1);
                }
            }
        } else if (configuration.getConfiguration().getExecutionEnvironment()
                .equals(Platforms.cloud.name())) {
            if (! lbManagerInstanceList.isEmpty()) {
                instance = lbManagerInstanceList.get(instanceList.size() - 1);
            } else if (! managerInstanceList.isEmpty()) {
                instance = managerInstanceList.get(instanceList.size() - 1);
            } else {
                instance = instanceList.get(instanceList.size() - 1);
            }
        } else {
            instance = instanceList.get(instanceList.size() - 1);
        }
        return instance;
    }

    public AutomationContext getAutomationContext() {
        return automationContext;
    }
}
