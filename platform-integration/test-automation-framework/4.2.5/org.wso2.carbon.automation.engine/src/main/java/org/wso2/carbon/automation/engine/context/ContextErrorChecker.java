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
import org.wso2.carbon.automation.engine.context.contextenum.Platforms;
import org.wso2.carbon.automation.engine.context.platformcontext.ProductGroup;
import org.wso2.carbon.automation.engine.context.usercontext.Tenants;
import org.wso2.carbon.automation.engine.context.usercontext.UserManagerContext;

import java.util.ArrayList;
import java.util.List;

public class ContextErrorChecker {
    private static final Log log = LogFactory.getLog(ContextErrorChecker.class);
    AutomationContext context;

    public ContextErrorChecker(AutomationContext automationContext) {
        context = automationContext;
    }

    public void checkErrors() throws ConfigurationMismatchException {
        checkUserContextErrors();
        checkPlatformErrors();
    }

    private void checkPlatformErrors() throws ConfigurationMismatchException {
        String executionEnv = context.getConfigurationContext().getConfiguration().
                getExecutionEnvironment();
        int productGroupCount = context.getPlatformContext().getAllProductGroups().size();
        int lbManagerNodeCount;
        int standaloneNodeCount;
        List<ProductGroup> productGroupList = context.getPlatformContext().getAllProductGroups();
        if (executionEnv.equals(Platforms.product.name())) {
            if (productGroupCount > 1) {
                log.error("Product execution mode cannot have multiple product groups");
                throw new ConfigurationMismatchException("PlatformContext",
                        "Product execution mode cannot have multiple product groups");
            }
            standaloneNodeCount = context.getPlatformContext().getFirstProductGroup()
                    .getAllStandaloneInstances().size();
            if (standaloneNodeCount == 0) {
                log.error("Product execution mode should have at least one standalone instance");
                throw new ConfigurationMismatchException
                        ("PlatformContext", "Product execution mode should have" +
                                " at least one standalone instance");
            }
        } else if (executionEnv.equals(Platforms.platform.name())) {
            for (ProductGroup productGroup : productGroupList) {
                lbManagerNodeCount = productGroup.getAllLBManagerInstances().size();
                if (lbManagerNodeCount == 0) {
                    log.error("Platform execution mode should have one lb_manager instance");
                    throw new ConfigurationMismatchException
                            ("PlatformContext", "Platform execution mode " +
                                    "should have one lb_manager instance");
                }
            }
        }
    }

    private void checkUserContextErrors() throws ConfigurationMismatchException {
        UserManagerContext userManagerContext = context.getUserManagerContext();
        List<Tenants> tenantsNodes = new ArrayList<Tenants>(userManagerContext.
                getAllTenantsNodes().values());
        for (Tenants tenants : tenantsNodes) {
            if (tenants.getAdminTenant() == null) {
                throw new ConfigurationMismatchException
                        ("UserContext ", tenants.getDomain() + " tenants should have admin tenant");
            }
        }
    }
}
