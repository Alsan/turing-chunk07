/*
 *  Copyright (c) 2008, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.qpid.authorization.service.qpid;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.apache.qpid.server.configuration.plugins.ConfigurationPlugin;
import org.apache.qpid.server.security.AbstractPlugin;
import org.apache.qpid.server.security.Result;
import org.apache.qpid.server.security.SecurityManager;
import org.apache.qpid.server.security.SecurityPluginFactory;
import org.apache.qpid.server.security.access.ObjectProperties;
import org.apache.qpid.server.security.access.ObjectType;
import org.apache.qpid.server.security.access.Operation;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.qpid.authorization.internal.AuthorizationServiceDataHolder;
import org.wso2.carbon.qpid.authorization.qpid.QpidAuthorizationHandler;
import org.wso2.carbon.user.api.UserRealm;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.service.RealmService;

import java.security.Principal;

/**
 * Qpid access control class based on Carbon Authorization Manager
 */
public class QpidAuthorizationPlugin extends AbstractPlugin {

    private static final Logger logger = Logger.getLogger(QpidAuthorizationPlugin.class);

    private static final String DOMAIN_NAME_SEPARATOR = "!";

    /**
        * Factory method for QpidAuthorizationPlugin
        */
    public static final SecurityPluginFactory<QpidAuthorizationPlugin>
            FACTORY = new SecurityPluginFactory<QpidAuthorizationPlugin>()
    {
        public QpidAuthorizationPlugin newInstance(ConfigurationPlugin config)
                throws ConfigurationException {
            QpidAuthorizationPlugin plugin = new QpidAuthorizationPlugin();
            return plugin;
        }
        
        public String getPluginName() {
            return QpidAuthorizationPlugin.class.getName();
        }

        public Class<QpidAuthorizationPlugin> getPluginClass() {
            return QpidAuthorizationPlugin.class;
        }
    };

    /**
        * Authorize access to broker
        *
        * @param objectType
        *               We only control access to virtual host 
        * @param instance
        * @return
        *               Authorization result
        */
    public Result access(ObjectType objectType, Object instance) {
        try {
            Principal principal = SecurityManager.getThreadPrincipal();
            if (principal == null) { // No user associated with the thread
                return getDefault();
            }

            // Allow access to virtual host for all logged in users. Authorization happens only if a user is authenticated.
            // So, at this point, the user is logged in.
            if (objectType == ObjectType.VIRTUALHOST) {
                return Result.ALLOWED;
            }
        } catch (Exception e) {
            // Do nothing
        }

        return Result.DENIED;
    }

    /**
        * Authorize operations inside broker
        * 
        * @param operation
        *               Operation on broker object (CONSUME, PUBLISH, etc)
        * @param objectType
        *               Type of object (EXCHANGE, QUEUE, etc)
        * @param properties
        *               Properties attached to the operation 
        * @return
        *               ALLOWED/DENIED 
        */
    public Result authorise(Operation operation, ObjectType objectType, ObjectProperties properties)
    {
        try {

            // Get username from tenant username
            PrivilegedCarbonContext.startTenantFlow();
            switch (operation) { // These operations do not need users associated with them
                case UNBIND:
                    return QpidAuthorizationHandler.handleUnbindQueue(properties);
                case DELETE:
                    if (ObjectType.EXCHANGE == objectType) {
                        return Result.ALLOWED;
                    } else if (ObjectType.QUEUE == objectType) {
                        return QpidAuthorizationHandler.handleDeleteQueue(properties);
                    }
            }

            Principal principal = SecurityManager.getThreadPrincipal();
            if (principal == null) { // No user associated with the thread
                return getDefault();
            }

            String username = principal.getName();

            // Get User Realm
            UserRealm userRealm = getUserRealm(username);

            if (username.indexOf(DOMAIN_NAME_SEPARATOR) > -1){
                String tenantDomain = username.substring(username.indexOf(DOMAIN_NAME_SEPARATOR) + 1);
                PrivilegedCarbonContext.getCurrentContext().setTenantDomain(tenantDomain);
                PrivilegedCarbonContext.getCurrentContext().getTenantId(true);
            } else {
                PrivilegedCarbonContext.getCurrentContext().setTenantId(0);
            }

            int domainNameSeparatorIndex = username.indexOf(DOMAIN_NAME_SEPARATOR);
            if (-1 != domainNameSeparatorIndex) {
                username = username.substring(0, domainNameSeparatorIndex);
            }
            switch (operation) {
                case CREATE:
                    if (ObjectType.EXCHANGE == objectType) {
                        return Result.ALLOWED;
                    } else if (ObjectType.QUEUE == objectType) {
                        return QpidAuthorizationHandler.handleCreateQueue(
                                username, userRealm, properties);
                    }
                case BIND:
                    return QpidAuthorizationHandler.handleBindQueue(
                            username, userRealm, properties);
                case PUBLISH:
                    return QpidAuthorizationHandler.handlePublishToExchange(
                            username, userRealm, properties);
                case CONSUME:
                    return QpidAuthorizationHandler.handleConsumeQueue(
                            username, userRealm, properties);
            }
        } catch (Exception e) {
            logger.error("Error while invoking QpidAuthorizationHandler", e);
        } finally {
            PrivilegedCarbonContext.endTenantFlow();
        }
        
        return Result.DENIED;
    }

    private String getRawQueueName(String queueName) {
        return queueName.substring(queueName.indexOf(":") + 1, queueName.length());
    }

    private static UserRealm getUserRealm(String username) {
        UserRealm userRealm = null;

        RealmService realmService = AuthorizationServiceDataHolder.getInstance().getRealmService();
        if (null != realmService) {
            try {
                // Get tenant ID
                int tenantID = 0;
                int domainNameSeparatorIndex = username.indexOf(DOMAIN_NAME_SEPARATOR);
                if (-1 != domainNameSeparatorIndex) { // Service case
                    String domainName = username.substring(domainNameSeparatorIndex + 1);
                    tenantID = realmService.getTenantManager().getTenantId(domainName);
                }

                // Get Realm
                userRealm = realmService.getTenantUserRealm(tenantID);
            } catch (UserStoreException e) {
                logger.warn("Error while getting tenant user realm for user " + username);
            } catch (NullPointerException e) {
                logger.error("Error while accessing the realm service : " + e.getMessage());
            }
        }

        return userRealm;
    }
}
