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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ProductGroup {
    private String groupName;
    private Boolean clusteringEnabled;
    //separate instance lists  based on the type of the instances
    protected HashMap<String, Instance> instances = new HashMap<String, Instance>();
    private HashMap<String, Instance> lbInstances = new HashMap<String, Instance>();
    private HashMap<String, Instance> managerInstances = new HashMap<String, Instance>();
    private HashMap<String, Instance> workerInstances = new HashMap<String, Instance>();
    private HashMap<String, LBWorkerInstance> lbWorkerInstances = new HashMap<String, LBWorkerInstance>();
    private HashMap<String, LBManagerInstance> lbManagerInstances = new HashMap<String, LBManagerInstance>();
    private HashMap<String, Instance> standaloneInstances = new HashMap<String, Instance>();
    private HashMap<String, LBWorkerManagerInstance> lbWorkerManagerInstances = new HashMap<String, LBWorkerManagerInstance>();
    //this list holds the complete list of instances
    protected HashMap<String, Instance> instanceList = new HashMap<String, Instance>();

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Boolean isClusteringEnabled() {
        return clusteringEnabled;
    }

    public void setClusteringEnabled(Boolean clusteringEnabled) {
        this.clusteringEnabled = clusteringEnabled;
    }

    /**
     * @param instance        instance to be added
     * @param instanceTypeMap specific instance type map (manager,loadBalanceManager
     */
    public void addInstanceToCategory(Instance instance, HashMap instanceTypeMap) {
        instanceTypeMap.put(instance.getName(), instance);
    }

    public void addInstance(Instance instance) {
        instances.put(instance.getName(), instance);
        this.instanceList.put(instance.getName(), instance);
    }

    public List<Instance> getAllInstances() {
        return new ArrayList<Instance>(instanceList.values());
    }

    public List<Instance> getAllManagerInstances() {
        return new ArrayList<Instance>(managerInstances.values());
    }

    /**
     * @param instanceName name of the instance
     * @return instance
     */
    public Instance getInstanceByName(String instanceName) {
        return instanceList.get(instanceName);
    }

    public void setInstances(HashMap<String, Instance> instances) {
        this.instances = instances;
    }

    public List<Instance> getAllLoadBalanceInstances() {
        List<Instance> instanceList = new LinkedList<Instance>();
        for (Instance instance : lbInstances.values()) {
            instanceList.add(instance);
        }
        return instanceList;
    }

    public List<Instance> getAllStandaloneInstances() {
        List<Instance> instanceList = new LinkedList<Instance>();
        for (Instance instance : standaloneInstances.values()) {
            instanceList.add(instance);
        }
        return instanceList;
    }

    public Instance getLBInstanceByName(String instanceName) {
        return lbInstances.get(instanceName);
    }

    public void setLbInstances(HashMap<String, Instance> loadBalanceInstances) {
        this.lbInstances = loadBalanceInstances;
    }

    public void addLBInstance(Instance instance) {
        this.lbInstances.put(instance.getName(), instance);
        this.instanceList.put(instance.getName(), instance);
    }

    public void addLBWorkerManagerInstance(LBWorkerManagerInstance instance) {
        this.lbWorkerManagerInstances.put(instance.getName(), instance);
        this.instanceList.put(instance.getName(), instance);
    }

    public List<Instance> getManagerInstances() {
        List<Instance> instanceList = new LinkedList<Instance>();
        for (Instance instance : managerInstances.values()) {
            instanceList.add(instance);
        }
        return instanceList;
    }

    public Instance getManagerInstanceByName(String instanceName) {
        return managerInstances.get(instanceName);
    }

    public void setManagerInstances(HashMap<String, Instance> managerInstances) {
        this.managerInstances = managerInstances;
    }

    public void addManagerInstance(Instance instance) {
        this.managerInstances.put(instance.getName(), instance);
        this.instanceList.put(instance.getName(), instance);
    }

    public List<Instance> getAllWorkerInstances() {
        List<Instance> instanceList = new LinkedList<Instance>();
        for (Instance instance : workerInstances.values()) {
            instanceList.add(instance);
        }
        return instanceList;
    }

    public Instance getWorkerInstanceByName(String instanceName) {
        return workerInstances.get(instanceName);
    }

    public void setWorkerInstances(HashMap<String, Instance> workerInstances) {
        this.workerInstances = workerInstances;
    }

    public void addWorkerInstance(Instance instance) {
        this.workerInstances.put(instance.getName(), instance);
        this.instanceList.put(instance.getName(), instance);
    }

    public List<Instance> getAllLBWorkerInstances() {
        List<Instance> instanceList = new LinkedList<Instance>();
        for (Instance instance : lbWorkerInstances.values()) {
            instanceList.add(instance);
        }
        return instanceList;
    }

    public List<LBWorkerManagerInstance> getAllLBWorkerManagerInstances() {
        return new LinkedList<LBWorkerManagerInstance>(lbWorkerManagerInstances.values());
    }

    public Instance getLBWorkerInstancesByName(String instanceName) {
        return lbWorkerInstances.get(instanceName);
    }

    public void setLBWorkerInstances(HashMap<String, LBWorkerInstance> loadBalanceWorkerInstances) {
        this.lbWorkerInstances = loadBalanceWorkerInstances;
    }

    public void addLBWorkerInstance(LBWorkerInstance instance) {
        this.lbWorkerInstances.put(instance.getName(), instance);
        this.instanceList.put(instance.getName(), instance);
    }

    public void addStandaloneInstance(Instance instance) {
        this.standaloneInstances.put(instance.getName(), instance);
        this.instanceList.put(instance.getName(), instance);
    }

    public List<Instance> getAllLBManagerInstances() {
        List<Instance> instanceList = new LinkedList<Instance>();
        for (Instance instance : lbManagerInstances.values()) {
            instanceList.add(instance);
        }
        return instanceList;
    }

    public Instance getLBManagerInstanceByName(String instanceName) {
        return lbManagerInstances.get(instanceName);
    }

    public void setLBManagerInstances(HashMap<String, LBManagerInstance>
                                              loadBalanceManagerInstances) {
        this.lbManagerInstances = loadBalanceManagerInstances;
    }

    public void addLBManagerInstance(LBManagerInstance instance) {
        this.lbManagerInstances.put(instance.getName(), instance);
        this.instanceList.put(instance.getName(), instance);
    }

    public HashMap<String, LBWorkerManagerInstance> getLbWorkerManagerInstances() {
        return lbWorkerManagerInstances;
    }

    public void setLBWorkerManagerInstances(HashMap<String, LBWorkerManagerInstance>
                                                    loadBalanceWkrMngInstances) {
        this.lbWorkerManagerInstances = loadBalanceWkrMngInstances;
    }
}
