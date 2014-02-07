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
package org.wso2.carbon.registry.webdav;

import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.webdav.utils.Utils;

/**
 * @scr.component name="org.wso2.carbon.registry.webdav" immediate="true"
 * @scr.reference name="registry.service"
 *                interface="org.wso2.carbon.registry.core.service.RegistryService"
 *                cardinality="1..1" policy="dynamic" bind="setRegistryService"
 *                unbind="unsetRegistryService"
 */

public class WebdavServiceComponet {


	protected void activate(ComponentContext context) {
	}

	protected void deactivate(ComponentContext context) {
	}

	protected void setRegistryService(RegistryService registryService) {
		Utils.setRegistryService(registryService);
	}

	protected void unsetRegistryService(RegistryService registryService) {
		if (registryService != null && registryService.equals(Utils.getRegistryService())){
			Utils.setRegistryService(null);
		}
	}
	
	public static Registry getRegistryInstance(String userName, String password) throws RegistryException{
        if (userName != null && password != null) {
            return Utils.getRegistryService().getUserRegistry(userName, password);
        } else if (userName != null) {
            return Utils.getRegistryService().getUserRegistry(userName);
        } else {
            return Utils.getRegistryService().getUserRegistry();
        }
	}

}
