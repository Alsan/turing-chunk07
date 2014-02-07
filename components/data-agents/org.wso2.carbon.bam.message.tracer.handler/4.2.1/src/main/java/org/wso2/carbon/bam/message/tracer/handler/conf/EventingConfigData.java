/**
 * Copyright (c) 2005 - 2013, WSO2 Inc. (http://www.wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.bam.message.tracer.handler.conf;


public class EventingConfigData {

    private boolean messageTracingEnable;
    private String url;
    private String userName;
    private String password;
    private boolean dumpBodyEnable;
    private boolean loggingEnable;
    private boolean publishToBAMEnable;

    public void setMessageTracingEnable(boolean isPublishingEnabled) {
        messageTracingEnable = isPublishingEnabled;
    }

    public boolean isMessageTracingEnable() {
        return messageTracingEnable;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDumpBodyEnable() {
        return dumpBodyEnable;
    }

    public void setDumpBodyEnable(boolean dumpBodyEnable) {
        this.dumpBodyEnable = dumpBodyEnable;
    }

    public void setLoggingEnable(boolean loggingEnable) {
        this.loggingEnable = loggingEnable;
    }

    public boolean isLoggingEnable() {
        return loggingEnable;
    }

    public void setPublishToBAMEnable(boolean publishToBAMEnable) {
        this.publishToBAMEnable = publishToBAMEnable;
    }

    public boolean isPublishToBAMEnable() {
        return publishToBAMEnable;
    }

}
