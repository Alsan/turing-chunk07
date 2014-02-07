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
package org.wso2.carbon.andes.admin.util;

import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.andes.core.QueueManagerService;

/**
 * this class is used to get the QueueMangerInterface service. it is used to send the
 * requests received from the Admin service to real cep engine
 * @scr.component name="AndesQueueManagerAdmin.component" immediate="true"
 * @scr.reference name="QueueManagerService.component"
 * interface="org.wso2.carbon.andes.core.QueueManagerService" cardinality="1..1"
 * policy="dynamic" bind="setQueueManagerService" unbind="unSetQueueManagerService"
 */

public class AndesQueueManagerAdminServiceDS {

       protected void activate(ComponentContext context) {

       }

       protected void setQueueManagerService(QueueManagerService cepService) {
           AndesQueueManagerAdminServiceDSHolder.getInstance().registerQueueManagerService(cepService);
       }

       protected void unSetQueueManagerService(QueueManagerService cepService) {
           AndesQueueManagerAdminServiceDSHolder.getInstance().unRegisterQueueManagerService(cepService);
       }
}
