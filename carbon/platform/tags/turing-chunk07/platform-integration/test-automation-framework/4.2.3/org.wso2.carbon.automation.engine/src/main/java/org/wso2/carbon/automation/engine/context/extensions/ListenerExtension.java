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
package org.wso2.carbon.automation.engine.context.extensions;

import java.util.LinkedHashMap;

public class ListenerExtension {
    private String name;
    private LinkedHashMap<String, Listener> listenerMap = null;

    public LinkedHashMap<String, Listener> getAllListeners() {
        return listenerMap;
    }

    public void setListeners(LinkedHashMap<String, Listener> listenerMap) {
        this.listenerMap = listenerMap;
    }

    public Listener getListener(String key) {
        return listenerMap.get(key);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
