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

import org.apache.axiom.om.OMElement;
import org.wso2.carbon.automation.engine.context.ContextConstants;
import org.wso2.carbon.automation.engine.context.contextenum.InstanceTypes;

import javax.xml.namespace.QName;
import java.util.Iterator;

public class PlatformContextFactory {
    private PlatformContext platformContext;

    public PlatformContextFactory() {
        platformContext = new PlatformContext();
    }

    /**
     * this method creates the platform object
     *
     * @param nodeElement OMElement input from the xml reader
     */
    public void createPlatformContext(OMElement nodeElement) {
        Iterator instanceGroupIterator = nodeElement.getChildElements();
        OMElement instanceGroupNode;
        // Walk through the instance group list
        while (instanceGroupIterator.hasNext()) {
            ProductGroup productGroup = new ProductGroup();
            instanceGroupNode = (OMElement) instanceGroupIterator.next();
            //set the attributes of the current instance group
            String groupName = instanceGroupNode.getAttributeValue
                    (QName.valueOf(ContextConstants.PLATFORM_CONTEXT_INSTANCE_GROUP_NAME));
            Boolean clusteringEnabled = Boolean.parseBoolean(instanceGroupNode.getAttributeValue
                    (QName.valueOf(ContextConstants.
                            PLATFORM_CONTEXT_INSTANCE_GROUP_CLUSTERING_ENABLED)));
            productGroup.setGroupName(groupName);
            productGroup.setClusteringEnabled(clusteringEnabled);
            // walk through the instances in the instance group
            Iterator instances = instanceGroupNode.getChildElements();
            OMElement instanceNode;
            while (instances.hasNext()) {
                //adding the instance to the instance group
                instanceNode = (OMElement) instances.next();
                createInstance(instanceNode, productGroup);
                //addInstanceToTypeList(currentInstance, productGroup);
            }
            platformContext.addProductGroup(productGroup);
        }
    }

    /*
     this method create and returns the instance object
      */
    protected void createInstance(OMElement instanceNode, ProductGroup productGroup) {
        Instance instance = new Instance();
        String instanceName = instanceNode.getAttributeValue
                (QName.valueOf(ContextConstants.PLATFORM_CONTEXT_INSTANCE_NAME));
        String instanceType = instanceNode.getAttributeValue
                (QName.valueOf(ContextConstants.PLATFORM_CONTEXT_INSTANCE_TYPE));
        String instancePortType = instanceNode.getAttributeValue
                (QName.valueOf(ContextConstants.PLATFORM_CONTEXT_INSTANCE_PORT_TYPE));
        instance.setName(instanceName);
        instance.setType(instanceType);
        instance.setServicePortType(instancePortType);
        Iterator instancePropertiesIterator = instanceNode.getChildElements();
        OMElement instancePropertyNode;
        while (instancePropertiesIterator.hasNext()) {
            instancePropertyNode = (OMElement) instancePropertiesIterator.next();
            //set the attribute values of the current instance
            String attribute = instancePropertyNode.getLocalName();
            String attributeValue = instancePropertyNode.getText();
            //set the property values of the current instance
            if (attribute.equals(ContextConstants.PLATFORM_CONTEXT_INSTANCE_HOST)) {
                instance.setHost(attributeValue);
            } else if (attribute.equals(ContextConstants.PLATFORM_CONTEXT_INSTANCE_HTTP_PORT)) {
                instance.setHttpPort(attributeValue);
            } else if (attribute.equals(ContextConstants.PLATFORM_CONTEXT_INSTANCE_HTTPS_PORT)) {
                instance.setHttpsPort(attributeValue);
            } else if (attribute.equals(ContextConstants.PLATFORM_CONTEXT_INSTANCE_WEB_CONTEXT)) {
                instance.setWebContext(attributeValue);
            } else if (attribute.equals(ContextConstants.PLATFORM_CONTEXT_INSTANCE_NHTTP_PORT)) {
                instance.setNhttpPort(attributeValue);
            } else if (attribute.equals(ContextConstants.PLATFORM_CONTEXT_INSTANCE_NHTTPS_PORT)) {
                instance.setNhttpsPort(attributeValue);
            }
            //add all the properties to the Map structure
            //this handles the unknown properties add by the user
            instance.addProperty(attribute, attributeValue);
        }
        if (instance.getType().equals(InstanceTypes.lb_worker_manager.name())) {
            LBWorkerManagerInstance lbWorkerManagerInstance = new LBWorkerManagerInstance(instance);
            lbWorkerManagerInstance.setWorkerHost(instance.
                    getPropertyByKey(ContextConstants.PLATFORM_CONTEXT_INSTANCE_WORKER_HOST));
            lbWorkerManagerInstance.setManagerHost(instance.
                    getPropertyByKey(ContextConstants.PLATFORM_CONTEXT_INSTANCE_MANAGER_HOST));
            productGroup.addLBWorkerManagerInstance(lbWorkerManagerInstance);
        } else if (instance.getType().equals(InstanceTypes.lb_worker.name())) {
            LBWorkerInstance lbWorkerInstance = new LBWorkerInstance(instance);
            lbWorkerInstance.setWorkerHost(instance.
                    getPropertyByKey(ContextConstants.PLATFORM_CONTEXT_INSTANCE_WORKER_HOST));
            productGroup.addLBWorkerInstance(lbWorkerInstance);
        } else if (instance.getType().equals(InstanceTypes.lb_manager.name())) {
            LBManagerInstance lbManagerInstance = new LBManagerInstance(instance);
            lbManagerInstance.setManagerHost(instance.
                    getPropertyByKey(ContextConstants.PLATFORM_CONTEXT_INSTANCE_MANAGER_HOST));
            productGroup.addLBManagerInstance(lbManagerInstance);
        } else if (instance.getType().equals(InstanceTypes.standalone.name())) {
            productGroup.addStandaloneInstance(instance);
        } else if (instance.getType().equals(InstanceTypes.manager.name())) {
            productGroup.addManagerInstance(instance);
        } else if (instance.getType().equals(InstanceTypes.worker.name())) {
            productGroup.addWorkerInstance(instance);
        } else if (instance.getType().equals(InstanceTypes.lb.name())) {
            productGroup.addLBInstance(instance);
        }
    }

    /*
    this returns the platform context
     */
    public PlatformContext getPlatformContext() {
        return platformContext;
    }
}
