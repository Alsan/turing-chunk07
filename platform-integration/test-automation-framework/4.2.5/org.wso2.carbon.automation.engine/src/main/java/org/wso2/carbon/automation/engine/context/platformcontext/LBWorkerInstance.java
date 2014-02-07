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
package org.wso2.carbon.automation.engine.context.platformcontext;

/**
 * This class to represent Load Balance Worker instances
 */
public class LBWorkerInstance extends Instance {
    private String workerHost = null;

    /**
     * @param instance general instance node is fed and create specific type LBWorker instance
     */
    public LBWorkerInstance(Instance instance) {
        this.setHost(instance.getHost());
        this.setServicePortType(instance.getServicePortType());
        this.setHttpPort(instance.getHttpPort());
        this.setHttpsPort(instance.getHttpsPort());
        this.setNhttpPort(instance.getNhttpPort());
        this.setNhttpsPort(instance.getNhttpsPort());
        this.setName(instance.getName());
        this.setType(instance.getType());
        this.setWebContext(instance.getWebContext());
        this.setProperties(instance.getAllProperties());
    }

    public String getWorkerHost() {
        return workerHost;
    }

    public void setWorkerHost(String workerHost) {
        this.workerHost = workerHost;
    }
}
