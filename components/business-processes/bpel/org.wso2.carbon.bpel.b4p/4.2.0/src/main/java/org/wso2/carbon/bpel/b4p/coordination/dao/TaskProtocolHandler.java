/*
 * Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.bpel.b4p.coordination.dao;

public class TaskProtocolHandler {

    private String taskID;
    private String protocolHandlerURL;

    public TaskProtocolHandler(String protocolHandlerURL,String taskID) {
        this.taskID = taskID;
        this.protocolHandlerURL = protocolHandlerURL;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getProtocolHandlerURL() {
        return protocolHandlerURL;
    }

    public void setProtocolHandlerURL(String protocolHandlerURL) {
        this.protocolHandlerURL = protocolHandlerURL;
    }
}

