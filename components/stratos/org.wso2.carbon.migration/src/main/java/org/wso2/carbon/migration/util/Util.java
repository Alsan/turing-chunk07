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
package org.wso2.carbon.migration.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.ServiceRegistration;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.usage.agent.api.SystemMeteringAgent;

public class Util {

    private static final Log log = LogFactory.getLog(Util.class);
    private static int JOB_INTERVAL = 15; // in mins
    private static String TASK_ID = "taskId";

    private static RegistryService registryService;
    private static RealmService realmService;
    private static ServiceRegistration redirectorServiceRegistration = null;

    public static synchronized void setRegistryService(RegistryService service) {
        if (registryService == null) {
            registryService = service;
        }
    }

    public static synchronized void setRealmService(RealmService service) {
        if (realmService == null) {
            realmService = service;
        }
    }

    public static RealmService getRealmService() {
        return realmService;
    }

    public static RegistryService getRegistryService() {
        return registryService;
    }

    public static UserRealm getUserRealm(int tenantId) throws RegistryException {
        return registryService.getUserRealm(tenantId);
    }

    public static UserRegistry getSuperTenantGovernanceSystemRegistry() throws RegistryException {
        return registryService.getGovernanceSystemRegistry();
    }

    public static SystemMeteringAgent getSystemMeteringAgent() throws RegistryException {
        return new SystemMeteringAgent(getSuperTenantGovernanceSystemRegistry());
    }
}
