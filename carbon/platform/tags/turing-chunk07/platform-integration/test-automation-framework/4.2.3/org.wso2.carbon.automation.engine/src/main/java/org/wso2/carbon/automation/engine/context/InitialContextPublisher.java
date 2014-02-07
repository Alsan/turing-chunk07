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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.automation.engine.FrameworkConstants;
import org.wso2.carbon.automation.engine.adminclients.AuthenticationAdminClient;
import org.wso2.carbon.automation.engine.context.configurationcontext.Configuration;
import org.wso2.carbon.automation.engine.context.contextenum.Platforms;
import org.wso2.carbon.automation.engine.context.environmentcontext.EnvironmentContext;
import org.wso2.carbon.automation.engine.context.environmentcontext.EnvironmentContextFactory;
import org.wso2.carbon.automation.engine.context.platformcontext.Instance;
import org.wso2.carbon.automation.engine.context.platformcontext.ProductGroup;
import org.wso2.carbon.automation.engine.context.toolcontext.ToolContext;
import org.wso2.carbon.automation.engine.context.usercontext.Tenant;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.RemoteException;

public class InitialContextPublisher {
    private final static Log log = LogFactory.getLog(InitialContextPublisher.class);
    private static AutomationContext context;

    static {
        AutomationContextFactory contextFactory = new AutomationContextFactory();
        try {
            InitialContextPublisher.context = contextFactory.getBasicContext();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public InitialContextPublisher() throws SAXException, URISyntaxException,
            ConfigurationMismatchException, IOException, XMLStreamException {
    }

    public static AutomationContext getContext() {
        return context;
    }

    public static Configuration getConfiguration() {
        Configuration configuration;
        new Configuration();
        configuration = getContext().getConfigurationContext().getConfiguration();
        return configuration;
    }

    public static ToolContext getToolConfiguration() {
        ToolContext configuration;
        new ToolContext();
        configuration = getContext().getToolContext();
        return configuration;
    }

    public static boolean isBuilderEnabled() {
        boolean builderEnabled = false;
        String environment = getContext().getConfigurationContext()
                .getConfiguration().getExecutionEnvironment();
        if (environment.equals(Platforms.product.name())) {
            builderEnabled = true;
        }
        return builderEnabled;
    }

    /**
     * @return give the first product group's first instance as the default instance
     */
    public static Instance getDefaultInstance() {
        //when the execution mode is product there is only one product group
        //when the execution mode is platform there are multiple product groups
        // but this take the first product group
        ProductGroup productGroup = getContext().getPlatformContext().getAllProductGroups().get(0);
        //get the fist instance of the product group
        return productGroup.getInstanceByName(ContextConstants.PRODUCT_GROUP_WORKER_INSTANCE);
    }

    /**
     * this method is used except product execution environment
     *
     * @param productGroupName product Group name
     * @return Environment context related to the given product group
     */
    public static EnvironmentContext getSuperTenantEnvironmentContext(String productGroupName,
                                                                      String instanceName) {
        Tenant admin = getContext().getUserManagerContext()
                .getTenantsNode(FrameworkConstants.SUPER_TENANT_DOMAIN_NAME).getAdminTenant();
        EnvironmentContextFactory environmentContextFactory = new EnvironmentContextFactory();
        environmentContextFactory.createEnvironmentContext(getContext(), productGroupName
                , instanceName, FrameworkConstants.SUPER_TENANT_DOMAIN_NAME, admin.getKey());
        return environmentContextFactory.getEnvironmentContext();
    }

    /**
     * this method is used the execution modes except product
     *
     * @param productGroup name of the product group
     * @return session cookie return by the admin login session
     */
    public static String getAdminUserSessionCookie(String productGroup, String instanceName)
            throws RemoteException, LoginAuthenticationExceptionException {
        Tenant admin = getContext().getUserManagerContext().
                getTenantsNode(FrameworkConstants.SUPER_TENANT_DOMAIN_NAME)
                .getAdminTenant();
        AuthenticationAdminClient authenticationAdminClient;
        String backend = getSuperTenantEnvironmentContext(productGroup, instanceName).
                getEnvironmentConfigurations().getBackEndUrl();
        authenticationAdminClient = new AuthenticationAdminClient(backend);
        return authenticationAdminClient.login(FrameworkConstants.
                SUPER_TENANT_DOMAIN_NAME, admin.getUserName(),
                admin.getPassword(), getContext().getPlatformContext().getProductGroup(productGroup).
                getInstanceByName(instanceName).getHost());
    }
}
