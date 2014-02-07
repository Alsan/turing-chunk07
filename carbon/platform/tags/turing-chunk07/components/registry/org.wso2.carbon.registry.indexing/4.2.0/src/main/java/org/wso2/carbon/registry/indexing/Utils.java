/*
 *  Copyright (c) 2005-2008, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.registry.indexing;

import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.RegistryConstants;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.utils.CarbonUtils;
import org.wso2.carbon.utils.WaitBeforeShutdownObserver;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

public class Utils {
    private static final Log log = LogFactory.getLog(Utils.class);

    private static RegistryService registryService;
    private static List<WaitBeforeShutdownObserver> waitBeforeShutdownObserver =
            new LinkedList<WaitBeforeShutdownObserver>();

    private static String defaultEventingServiceURL;

    private static String remoteTopicHeaderName;
    
    private static String remoteTopicHeaderNS;

    private static String remoteSubscriptionStoreContext;

    public static void setRegistryService(RegistryService service) {
        registryService = service;
    }

    public static RegistryService getRegistryService() {
        return registryService;
    }

    public static WaitBeforeShutdownObserver[] getWaitBeforeShutdownObservers() {
        return waitBeforeShutdownObserver.toArray(
                new WaitBeforeShutdownObserver[waitBeforeShutdownObserver.size()]);
    }

    public static void setWaitBeforeShutdownObserver(WaitBeforeShutdownObserver service) {
        CarbonUtils.checkSecurity();
        waitBeforeShutdownObserver.add(service);
    }

    public static void clearWaitBeforeShutdownObserver() {
        CarbonUtils.checkSecurity();
        waitBeforeShutdownObserver = new LinkedList<WaitBeforeShutdownObserver>();
    }

    public static Registry getRegistry() throws RegistryException {

        MessageContext messageContext = MessageContext.getCurrentMessageContext();
        if (messageContext == null) {
            String msg = "Could not get the user's Registry session. Message context not found.";
            log.error(msg);
            throw new RegistryException(msg);
        }

        HttpServletRequest request =
                (HttpServletRequest) messageContext.getProperty("transport.http.servletRequest");

        Registry registry =
                (Registry) request.getSession().getAttribute(RegistryConstants.USER_REGISTRY);

        if (registry == null) {
            String msg = "User's Registry instance is not found. " +
                    "Creating a anonymous Registry instance for the user.";
            if(log.isDebugEnabled()) {
                log.debug(msg);
            }
            if (registryService == null) {
                msg = "Unable to create anonymous Registry instance for user. " +
                        "Registry Service was not found.";
                log.error(msg);
                throw new RegistryException(msg);
            }
            registry = registryService.getUserRegistry();
            request.getSession().setAttribute(RegistryConstants.USER_REGISTRY, registry);
        }

        return registry;
    }
    public static String getDefaultEventingServiceURL() {
        return defaultEventingServiceURL;
    }

    public static void setDefaultEventingServiceURL(String defaultEventingServiceURL) {
        Utils.defaultEventingServiceURL = defaultEventingServiceURL;
    }

    public static String getRemoteTopicHeaderName() {
        return remoteTopicHeaderName;
    }

    public static void setRemoteTopicHeaderName(String remoteTopicHeaderName) {
        Utils.remoteTopicHeaderName = remoteTopicHeaderName;
    }

    public static String getRemoteTopicHeaderNS() {
        return remoteTopicHeaderNS;
    }

    public static void setRemoteTopicHeaderNS(String remoteTopicHeaderNS) {
        Utils.remoteTopicHeaderNS = remoteTopicHeaderNS;
    }

    public static String getRemoteSubscriptionStoreContext() {
        return remoteSubscriptionStoreContext;
    }

    public static void setRemoteSubscriptionStoreContext(String remoteSubscriptionStoreContext) {
        Utils.remoteSubscriptionStoreContext = remoteSubscriptionStoreContext;
    }
}
