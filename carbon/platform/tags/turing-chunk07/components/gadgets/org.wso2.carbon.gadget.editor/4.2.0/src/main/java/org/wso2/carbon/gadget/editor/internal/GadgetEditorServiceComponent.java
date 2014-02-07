/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.carbon.gadget.editor.internal;

import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.gadget.editor.util.Util;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.core.session.UserRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.user.mgt.UserMgtConstants;

/**
 * @scr.component name="org.wso2.carbon.editor.editor" immediate="true"
 * @scr.reference name="registry.service"
 *                interface="org.wso2.carbon.registry.core.service.RegistryService"
 *                cardinality="1..1" policy="dynamic" bind="setRegistryService"
 *                unbind="unsetRegistryService"
 */
public class GadgetEditorServiceComponent {

    private static Log log = LogFactory.getLog(GadgetEditorServiceComponent.class);
   
    protected void activate(ComponentContext context) {
        try {
            log.debug("******* Gadget Editor Management Service bundle is activated ******* ");
        } catch (Exception e) {
            log.debug("******* Failed to activate Gadget Editor Management Service bundle ******* ");
        }

    }

    protected void deactivate(ComponentContext context) {
        log.debug("******* Gadget Editor Management Service bundle is deactivated ******* ");
    }

    protected void setRegistryService(RegistryService registryService) {
        Util.setRegistryService(registryService);
    }

    protected void unsetRegistryService(RegistryService registryService) {
        Util.setRegistryService(null);
    }

}
