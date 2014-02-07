/*
*  Copyright (c) 2005-2011, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.appmgt.api;

import org.wso2.carbon.appmgt.api.model.APIIdentifier;
import org.wso2.carbon.appmgt.api.model.SubscribedAPI;

import java.util.Collections;
import java.util.Set;

/**
 * This class represent WebApp Subscriptions
 */
public class APISubscriptions {
    private APIIdentifier apiId;
    private Set<SubscribedAPI> subscriptions;

    public APISubscriptions(APIIdentifier apiId, Set<SubscribedAPI> subscriptions) {
        this.apiId = apiId;
        this.subscriptions = subscriptions;
    }

    public APIIdentifier getApiId() {
        return apiId;
    }

    public Set<SubscribedAPI> getSubscriptions() {
        return Collections.unmodifiableSet(subscriptions);
    }
}
