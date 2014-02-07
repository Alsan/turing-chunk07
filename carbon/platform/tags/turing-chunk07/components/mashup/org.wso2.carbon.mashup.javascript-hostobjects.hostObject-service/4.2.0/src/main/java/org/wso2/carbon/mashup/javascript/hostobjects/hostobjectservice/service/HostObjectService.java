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
package org.wso2.carbon.mashup.javascript.hostobjects.hostobjectservice.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class HostObjectService {

    private List<String> hostObjectClasses = new ArrayList<String>();
    private List<String> hostObjectsThatNeedServices = new ArrayList<String>();
    private Map<String,String> globalObjects = new HashMap<String, String>();

    private HostObjectService() {
        
    }

    static private HostObjectService _instance = null;

    static public HostObjectService instance() {
          if(null == _instance) {
             _instance = new HostObjectService();
          }
          return _instance;
       }

    public List<String> getHostObjectClasses() {
        return hostObjectClasses;
    }

    public Map<String,String> getGlobalObjects() {
        return globalObjects;
    }

    public void addHostObjectThatNeedServices(String className) {
        hostObjectsThatNeedServices.add(className);
    }

    public List<String> getHostObjectsThatNeedServices() {
        return hostObjectsThatNeedServices;
    }

    public void addHostObjectClass(String className) {
        hostObjectClasses.add(className);
    }

    public void addGlobalObject(String hostObjectName, String globalObjectName) {
        globalObjects.put(hostObjectName,  globalObjectName);
    }

    public void removeHostObject(String className, String hostObjectName) {
        hostObjectClasses.remove(className);
        globalObjects.remove(hostObjectName);
    }
}
