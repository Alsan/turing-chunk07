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
package org.wso2.carbon.application.mgt.bpel.internal;

import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.application.deployer.service.ApplicationManagerService;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * @scr.component name="application.mgt.bpel.dscomponent" immediate="true"
 * @scr.reference name="application.manager"
 * interface="org.wso2.carbon.application.deployer.service.ApplicationManagerService"
 * cardinality="1..1" policy="dynamic" bind="setAppManager" unbind="unsetAppManager"
 */
public class BPELAppMgtServiceComponent {

    private static Log log = LogFactory.getLog(BPELAppMgtServiceComponent.class);

    private static ApplicationManagerService applicationManager;

    protected void activate(ComponentContext ctxt) {
        if (log.isDebugEnabled()) {
            log.debug("Activated BPELAppMgtServiceComponent");
        }
    }

    protected void deactivate(ComponentContext ctxt) {
        if (log.isDebugEnabled()) {
            log.debug("Deactivated BPELAppMgtServiceComponent");
        }
    }

    protected void setAppManager(ApplicationManagerService appManager) {
        applicationManager = appManager;
    }

    protected void unsetAppManager(ApplicationManagerService appManager) {
        applicationManager = null;
    }

    public static ApplicationManagerService getAppManager() {
        if (applicationManager == null) {
            String msg = "Before activating BPEL App management service bundle, an instance of "
                    + "ApplicationManager should be in existance";
            log.error(msg);
        }
        return applicationManager;
    }

}
