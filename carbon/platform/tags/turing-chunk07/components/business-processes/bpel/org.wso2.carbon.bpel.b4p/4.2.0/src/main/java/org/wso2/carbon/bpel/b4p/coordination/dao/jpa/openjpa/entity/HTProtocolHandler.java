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

package org.wso2.carbon.bpel.b4p.coordination.dao.jpa.openjpa.entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.bpel.b4p.coordination.dao.HTProtocolHandlerDAO;

import javax.persistence.*;

@Entity
@Table(name = "HT_COORDINATION_DATA")
public class HTProtocolHandler implements HTProtocolHandlerDAO {

    private static Log log = LogFactory.getLog(HTProtocolHandler.class);

    /**
     * Used to specify Unique message ID (UUID) generated by B4P component.
     */
    @Id
    @Column(name = "MESSAGE_ID", nullable = false, unique = true)
    private String messageID;

    /**
     * Used to specify Task parent, in this case BPEL Process instance ID.
     */
    @Column(name = "PROCESS_INSTANCE_ID", nullable = true)
    private String processInstanceID;

    /**
     * Used to specify TaskID. A process instance can have multiple task IDs.
     *
     */
    @Column(name = "TASK_ID", nullable = true)
    private String taskID;

    /**
     * Used to specify Protocol Handler URL for a Task.
     */
    @Column(name = "PROTOCOL_HANDlER_URL", nullable = false)
    private String protocolHandlerURL;

    public HTProtocolHandler() {
    }

    @Override
    public String getMessageID() {
        return messageID;
    }

    @Override
    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    @Override
    public String getProcessInstanceID() {
        return processInstanceID;
    }

    @Override
    public void setProcessInstanceID(String processInstanceID) {
        this.processInstanceID = processInstanceID;
    }

    @Override
    public String getTaskID() {
        return taskID;
    }

    @Override
    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    @Override
    public String getHumanTaskProtocolHandlerURL() {
        return protocolHandlerURL;
    }

    @Override
    public void setHumanTaskProtocolHandlerURL(String protocolHandlerURL) {
        this.protocolHandlerURL = protocolHandlerURL;
    }
}
