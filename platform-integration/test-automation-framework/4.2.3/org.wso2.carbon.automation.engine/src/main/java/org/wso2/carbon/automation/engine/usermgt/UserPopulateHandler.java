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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.automation.engine.FrameworkConstants;
import org.wso2.carbon.automation.engine.FrameworkPathUtil;
import org.wso2.carbon.automation.engine.context.InitialContextPublisher;
import org.wso2.carbon.automation.engine.context.contextenum.Platforms;
import org.wso2.carbon.automation.engine.context.platformcontext.Instance;
import org.wso2.carbon.automation.engine.context.platformcontext.LBWorkerManagerInstance;
import org.wso2.carbon.automation.engine.context.platformcontext.ProductGroup;
import org.wso2.carbon.automation.engine.context.securitycontext.KeyStore;
import org.wso2.carbon.automation.engine.frameworkutils.FrameworkUtil;

import java.io.File;
import java.util.List;

public class UserPopulateHandler {
    private static final Log log = LogFactory.getLog(UserPopulateHandler.class);
    private static String executionEnvironment = InitialContextPublisher.getContext().
            getConfigurationContext().getConfiguration().getExecutionEnvironment();
    private static UserPopulator userPopulator;
    private static String productGroupName;
    private static String sessionCookie;
    private static String instanceName;
    private static String backendURL;
    private static boolean multiTenantEnabled = InitialContextPublisher.
            getContext().getConfigurationContext()
            .getConfiguration().isMultiTenantModeEnabled();

    /**
     * This class handles the user population for  different execution modes
     *
     * @throws Exception
     */
    public static void populateUsers() throws Exception {
        FrameworkUtil.setKeyStoreProperties();
        if (executionEnvironment.equals(Platforms.product.name())) {
            productGroupName = InitialContextPublisher.getContext().getPlatformContext().
                    getFirstProductGroup().getGroupName();
            List<Instance> instanceList = InitialContextPublisher.getContext().getPlatformContext().
                    getFirstProductGroup().getAllStandaloneInstances();
            for (Instance instance : instanceList) {
                instanceName = instance.getName();
                sessionCookie = InitialContextPublisher.
                        getAdminUserSessionCookie(productGroupName, instanceName);
                backendURL = InitialContextPublisher.
                        getSuperTenantEnvironmentContext(productGroupName, instanceName).
                        getEnvironmentConfigurations().getBackEndUrl();
                userPopulator = new UserPopulator(sessionCookie, backendURL, multiTenantEnabled);
                log.info("Populating users for " + productGroupName + " product group: " +
                        instanceName + " instance");
                userPopulator.populateUsers(productGroupName, instanceName);
            }
        }
        //here we go through every product group and populate users for those
        else if (executionEnvironment.equals(Platforms.platform.name())) {
            List<ProductGroup> productGroupList = InitialContextPublisher.getContext().
                    getPlatformContext().getAllProductGroups();
            for (ProductGroup productGroup : productGroupList) {
                productGroupName = productGroup.getGroupName();
                if (! productGroup.isClusteringEnabled()) {
                    instanceName = productGroup.getAllStandaloneInstances().get(0).getName();
                } else {
                    if (productGroup.getAllLBWorkerManagerInstances().size() > 0) {
                        LBWorkerManagerInstance lbWorkerManagerInstance = productGroup.
                                getAllLBWorkerManagerInstances().get(0);
                        lbWorkerManagerInstance.setHost(lbWorkerManagerInstance.getManagerHost());
                        instanceName = lbWorkerManagerInstance.getName();
                    } else if (productGroup.getAllLBManagerInstances().size() > 0) {
                        instanceName = productGroup.getAllLBManagerInstances().get(0).getName();
                    } else if (productGroup.getAllManagerInstances().size() > 0) {
                        instanceName = productGroup.getAllManagerInstances().get(0).getName();
                    }
                }
                sessionCookie = InitialContextPublisher.
                        getAdminUserSessionCookie(productGroupName, instanceName);
                backendURL = InitialContextPublisher.
                        getSuperTenantEnvironmentContext(productGroupName, instanceName).
                        getEnvironmentConfigurations().getBackEndUrl();
                userPopulator = new UserPopulator(sessionCookie, backendURL, multiTenantEnabled);
                log.info("Populating users for " + productGroupName + " product group: " +
                        instanceName + " instance");
                userPopulator.populateUsers(productGroupName, instanceName);
            }
        }
    }

    public static void deleteUsers() throws Exception {
        if (executionEnvironment.equals(Platforms.product.name())) {
            productGroupName = InitialContextPublisher.getContext().getPlatformContext().
                    getFirstProductGroup().getGroupName();
            List<Instance> instanceList = InitialContextPublisher.getContext().getPlatformContext().
                    getFirstProductGroup().getAllStandaloneInstances();
            for (Instance instance : instanceList) {
                instanceName = instance.getName();
                sessionCookie = InitialContextPublisher.
                        getAdminUserSessionCookie(productGroupName, instanceName);
                backendURL = InitialContextPublisher.
                        getSuperTenantEnvironmentContext(productGroupName, instanceName).
                        getEnvironmentConfigurations().getBackEndUrl();
                userPopulator = new UserPopulator(sessionCookie, backendURL, multiTenantEnabled);
                log.info("Populating users for " + productGroupName + " product group: " +
                        instanceName + " instance");
                // userPopulator.deleteUsers(productGroupName, instanceName);
            }
        }
        //here we go through every product group and populate users for those
        else if (executionEnvironment.equals(Platforms.platform.name())) {
            List<ProductGroup> productGroupList = InitialContextPublisher.getContext().
                    getPlatformContext().getAllProductGroups();
            for (ProductGroup productGroup : productGroupList) {
                productGroupName = productGroup.getGroupName();
                instanceName = productGroup.getAllLBManagerInstances().get(0).getName();
                sessionCookie = InitialContextPublisher.
                        getAdminUserSessionCookie(productGroupName, instanceName);
                backendURL = InitialContextPublisher.
                        getSuperTenantEnvironmentContext(productGroupName, instanceName).
                        getEnvironmentConfigurations().getBackEndUrl();
                userPopulator = new UserPopulator(sessionCookie, backendURL, multiTenantEnabled);
                log.info("Populating users for " + productGroupName + " product group: " +
                        instanceName + " instance");
                //userPopulator.deleteUsers(productGroupName, instanceName);
            }
        }
    }

    public static void setKeyStoreProperties() {
        KeyStore keyStore = InitialContextPublisher.getContext().getSecurityContext().
                getKeyStore(FrameworkConstants.DEFAULT_KEY_STORE);
        String keyStoreFileName = keyStore.getFileName();
        File keyStoreFile = new File(String.format("%s%s", FrameworkPathUtil.
                getSystemResourceLocation(), keyStoreFileName));
        System.setProperty("javax.net.ssl.trustStore", keyStoreFile.getAbsolutePath());
        System.setProperty("javax.net.ssl.trustStorePassword", keyStore.getPassword());
        System.setProperty("javax.net.ssl.trustStoreType", keyStore.getType());
    }
}
