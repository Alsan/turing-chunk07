/*                                                                             
 * Copyright 2004,2005 The Apache Software Foundation.                         
 *                                                                             
 * Licensed under the Apache License, Version 2.0 (the "License");             
 * you may not use this file except in compliance with the License.            
 * You may obtain a copy of the License at                                     
 *                                                                             
 *      http://www.apache.org/licenses/LICENSE-2.0                             
 *                                                                             
 * Unless required by applicable law or agreed to in writing, software         
 * distributed under the License is distributed on an "AS IS" BASIS,           
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    
 * See the License for the specific language governing permissions and         
 * limitations under the License.                                              
 */
package org.wso2.carbon.eventing.impl.internal;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

/**
 * @scr.component name="org.wso2.carbon.eventing.impl.internal.EventingServiceComponent" immediate="true"
 * @scr.reference name="registry.service" interface="org.wso2.carbon.registry.core.service.RegistryService"
 * cardinality="1..1" policy="dynamic"  bind="setRegistryService" unbind="unsetRegistryService"
 */

public class EventingServiceComponent {

    private static RegistryService registryService;
    private ServiceRegistration eventingServiceComponentRegistration;

    public static Registry getSystemRegistry() throws RegistryException {
        if (registryService != null) {
            return registryService.getConfigSystemRegistry();
        }
        return null;
    }

    protected void activate(ComponentContext context) {
        eventingServiceComponentRegistration = context.getBundleContext().registerService(
                EventingServiceComponent.class.getName(), this, null);
    }

    protected void deactivate(ComponentContext context) {
        if (eventingServiceComponentRegistration != null) {
            eventingServiceComponentRegistration.unregister();
            eventingServiceComponentRegistration = null;
        }
    }

    protected void setRegistryService(RegistryService registryService){
        EventingServiceComponent.registryService = registryService;
    }

    protected void unsetRegistryService(RegistryService registryService){
         EventingServiceComponent.registryService = null;
    }
}
