/**
 *  Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.mediation.templates.internal;

import org.apache.axis2.AxisFault;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.config.SynapseConfiguration;
import org.wso2.carbon.mediation.dependency.mgt.services.DependencyManagementService;
import org.wso2.carbon.mediation.initializer.services.SynapseEnvironmentService;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the required bits and pieces for the admin service
 * {@link org.wso2.carbon.mediation.templates.services.TemplateEditorAdmin}
 */
public class ConfigHolder {

    private static ConfigHolder instance;
    private static final Log log = LogFactory.getLog(ConfigHolder.class);

    private SynapseConfiguration synapseConfiguration;
    private AxisConfiguration axisConfiguration;
    private DependencyManagementService dependencyManager;

    private Map<Integer, SynapseEnvironmentService> synapseEnvironmentServices =
            new HashMap<Integer, SynapseEnvironmentService>();

    private ConfigHolder() {
    }

    public static ConfigHolder getInstance() {
        if (instance == null) {
            instance = new ConfigHolder();
        }
        return instance;
    }

    public SynapseConfiguration getSynapseConfiguration() throws AxisFault {
        assertNull("SynapseConfiguration", synapseConfiguration);
        return synapseConfiguration;
    }

    public void setSynapseConfiguration(SynapseConfiguration synapseConfiguration) {
        this.synapseConfiguration = synapseConfiguration;
    }

    public AxisConfiguration getAxisConfiguration() throws AxisFault {
        assertNull("AxisConfiguration", axisConfiguration);
        return axisConfiguration;
    }

    public void setAxisConfiguration(AxisConfiguration axisConfiguration) {
        this.axisConfiguration = axisConfiguration;
    }

    public DependencyManagementService getDependencyManager() {
        return dependencyManager;
    }

    public void setDependencyManager(DependencyManagementService dependencyManager) {
        this.dependencyManager = dependencyManager;
    }

    private void assertNull(String name, Object object) throws AxisFault {
        if (object == null) {
            String message = name + " reference in the proxy admin config holder is null";
            log.error(message);
            throw new AxisFault(message);
        }
    }

    public SynapseEnvironmentService getSynapseEnvironmentService(int id) {
        return synapseEnvironmentServices.get(id);
    }

    public void addSynapseEnvironmentService(int id,
                                             SynapseEnvironmentService synapseEnvironmentService) {
        synapseEnvironmentServices.put(id, synapseEnvironmentService);
    }

    public void removeSynapseEnvironmentService(int id) {
        synapseEnvironmentServices.remove(id);
    }

    public Map<Integer, SynapseEnvironmentService> getSynapseEnvironmentServices() {
        return synapseEnvironmentServices;
    }

}
