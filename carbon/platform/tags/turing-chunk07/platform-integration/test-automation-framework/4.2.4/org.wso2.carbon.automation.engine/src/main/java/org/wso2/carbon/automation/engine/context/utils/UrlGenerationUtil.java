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
package org.wso2.carbon.automation.engine.context.utils;

import org.wso2.carbon.automation.engine.FrameworkConstants;
import org.wso2.carbon.automation.engine.context.AutomationContext;
import org.wso2.carbon.automation.engine.context.ContextConstants;
import org.wso2.carbon.automation.engine.context.contextenum.InstanceTypes;
import org.wso2.carbon.automation.engine.context.platformcontext.Instance;

public class UrlGenerationUtil {
    static AutomationContext automationContext;
    static String nhttpsPort;
    static String nhttpPort;
    static String httpsPort;
    static String httpPort;
    static String urlSuffix;
    static String hostName;
    static String webContextRoot;
    static String tenantDomain;
    static String servicePortType;
    static boolean webContextEnabled = true;
    static boolean isRunningOnSuperAdmin = false;
    static Instance automationInstance;

    public static void setEnvironment(AutomationContext context, String instanceGroupName,
                                      String instanceName,String domain, String tenantUserId) {
        automationContext = context;
        automationInstance = automationContext.getPlatformContext()
                .getProductGroup(instanceGroupName).getInstanceByName(instanceName);
        nhttpsPort = automationInstance.getNhttpsPort();
        nhttpPort = automationInstance.getNhttpPort();
        httpsPort = automationInstance.getHttpsPort();
        httpPort = automationInstance.getHttpPort();
        hostName = automationInstance.getHost();
        servicePortType = automationInstance.getServicePortType();
        webContextRoot = automationInstance.getWebContext();
        tenantDomain = domain;
        automationContext.getUserManagerContext().getTenantsNode(domain).getTenant(tenantUserId);
        if (tenantDomain.equals(FrameworkConstants.SUPER_TENANT_DOMAIN_NAME)) {
            isRunningOnSuperAdmin = true;
            urlSuffix = "";
        } else {
            isRunningOnSuperAdmin = false;
            urlSuffix = "/t/" + tenantDomain;
        }
        tenantDomain = domain;
        if (webContextRoot.length() == 1) {
            webContextRoot = "";
            webContextEnabled = false;
        }
    }

    public static String getHttpServiceURL() {
        return getServiceURL("http");
    }

    public static String getHttpsServiceURL() {
        return getServiceURL("https");
    }

    /**
     * user can user this method to generate Secure service URL for a given port type
     *
     * @param port port value
     * @return the secure service URL
     */
    public static String getSecureServiceUrl(String port) {
        return getServiceURL("https", port);
    }

    /**
     * user can user this method to generate service URL for a given port type
     *
     * @param port port value
     * @return the service URL
     */
    public static String getServiceUrl(String port) {
        return getServiceURL("http", port);
    }

    public static String getServiceURL(String protocol) {
        String serviceURL;
        String port = automationInstance.getPropertyByKey(automationInstance.getServicePortType());
        String type = automationInstance.getType();
        if (type.equals(InstanceTypes.lb_worker_manager.name()) ||
                type.equals(InstanceTypes.lb_worker.name())) {
            hostName = automationInstance.
                    getPropertyByKey(ContextConstants.PLATFORM_CONTEXT_INSTANCE_WORKER_HOST);
        } else if (type.equals(InstanceTypes.worker.name()) ||
                type.equals(InstanceTypes.manager.name())) {
            hostName = automationInstance.
                    getPropertyByKey(ContextConstants.PLATFORM_CONTEXT_INSTANCE_HOST);
        }
        serviceURL = protocol + "://" + hostName + ":" + port + webContextRoot
                + "/" + "services" + urlSuffix;
        return serviceURL;
    }

    public static String getServiceURL(String protocol, String portName) {
        String serviceURL;
        String port = automationInstance.getPropertyByKey(portName);
        serviceURL = protocol + "://" + hostName + ":" + port + webContextRoot + "/"
                + "services" + urlSuffix;
        return serviceURL;
    }

    /**
     * Generated the backend URL for the given instance
     *
     * @return backend URL of the instance
     */
    public static String getBackendURL() {
        String backendURL;
        // Use the nhttps port if the instance is LB manager or LB worker manager
        if (automationInstance.getType().equals(InstanceTypes.lb_manager.name()) ||
                automationInstance.getType().equals(InstanceTypes.lb_worker_manager.name())) {
            backendURL = "https://" + hostName + ":" +
                    nhttpsPort + webContextRoot + "/" + "services/";
        } else {
            backendURL = "https://" + hostName + ":" +
                    httpsPort + webContextRoot + "/" + "services/";
        }
        return backendURL;
    }

    public static String getWebAppURL() {
        String webAppURL;
        if (isRunningOnSuperAdmin) {
            if (httpPort != null) {
                webAppURL = "http://" + hostName + ":" +
                        httpPort + "/t/" + tenantDomain + "/webapps";
            } else {
                webAppURL = "http://" + hostName + "/t/" + tenantDomain + "/webapps";
            }
        } else {
            if (httpPort != null) {
                webAppURL = "http://" + hostName + ":" + httpPort;
            } else {
                webAppURL = "http://" + hostName;
            }
        }
        return webAppURL;
    }
}
