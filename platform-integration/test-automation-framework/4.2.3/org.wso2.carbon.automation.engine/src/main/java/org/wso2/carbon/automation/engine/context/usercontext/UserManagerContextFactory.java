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
package org.wso2.carbon.automation.engine.context.usercontext;

import org.apache.axiom.om.OMElement;
import org.wso2.carbon.automation.engine.context.ContextConstants;

import javax.xml.namespace.QName;
import java.util.*;

public class UserManagerContextFactory {
    UserManagerContext userManagerContext;

    public UserManagerContextFactory() {
        userManagerContext = new UserManagerContext();
    }

    /*
      this method is get the list of the tenant level objects from the configuration
     */

    protected List<OMElement> listTenantsElements(OMElement endPointElem) {
        List<OMElement> tenantsList = new ArrayList<OMElement>();
        OMElement node;
        Iterator children = endPointElem.getChildElements();
        while (children.hasNext()) {
            node = (OMElement) children.next();
            tenantsList.add(node);
        }
        return tenantsList;
    }

    /*
      List all the Tenants(Admin tenant and other tenants)
     */
    protected List<OMElement> listTenants(OMElement node) {
        List<OMElement> tenantList = new ArrayList<OMElement>();
        Iterator environmentNodeItr = node.getChildElements();
        while (environmentNodeItr.hasNext()) {
            tenantList.add((OMElement) environmentNodeItr.next());
        }
        return tenantList;
    }

    /*
      List all the tenants
     */
    protected List<OMElement> listTenant(OMElement node) {
        List<OMElement> tenantList = new ArrayList<OMElement>();
        Iterator tenantIterator = node.getChildElements();
        while (tenantIterator.hasNext()) {
            tenantList.add((OMElement) tenantIterator.next());
        }
        return tenantList;
    }

    public UserManagerContext getUserManagementContext() {
        return userManagerContext;
    }

    protected Tenant createTenant(OMElement tenantNode) {
        Tenant tenant = new Tenant();
        tenant.setKey(tenantNode.getAttributeValue(QName.valueOf
                (ContextConstants.USER_MANAGEMENT_CONTEXT_TENANT_KEY)));
        Iterator tenantProperties = tenantNode.getChildElements();
        while (tenantProperties.hasNext()) {
            OMElement tenantProperty = (OMElement) tenantProperties.next();
            String attributeName = tenantProperty.getLocalName();
            String attributeValue = tenantProperty.getText();
            if (attributeName.equals(ContextConstants.USER_MANAGEMENT_CONTEXT_TENANT_USERNAME)) {
                tenant.setUsername(attributeValue);
            } else if (attributeName.equals(ContextConstants.
                    USER_MANAGEMENT_CONTEXT_TENANT_PASSWORD)) {
                tenant.setPassword(attributeValue);
            }
        }
        return tenant;
    }

    /**
     * @param nodeElement OMElement input from the xml reader
     */
    public void createUserManagementContext(OMElement nodeElement) {
        LinkedHashMap<String, Tenants> tenantsMap = new LinkedHashMap<String, Tenants>();
        for (OMElement tenantsNode : listTenantsElements(nodeElement)) {
            Tenants tenantBunch = new Tenants();
            String tenantsDomain = tenantsNode.getAttributeValue(QName.valueOf
                    (ContextConstants.USER_MANAGEMENT_CONTEXT_TENANT_DOMAIN));
            tenantBunch.setDomain(tenantsDomain);
            HashMap<String, Tenant> tenantList = new HashMap<String, Tenant>();
            for (OMElement tenantNode : listTenants(tenantsNode)) {
                Tenant tenant = createTenant(tenantNode);
                if (tenant.getKey().equals(ContextConstants.USER_MANAGEMENT_ADMIN_TENANT)) {
                    tenantBunch.setAdminTenant(tenant);
                }
                tenantList.put(tenant.getKey(), tenant);
            }
            tenantBunch.setTenants(tenantList);
            tenantsMap.put(tenantsDomain, tenantBunch);
        }
        userManagerContext.setTenants(tenantsMap);
    }
}
