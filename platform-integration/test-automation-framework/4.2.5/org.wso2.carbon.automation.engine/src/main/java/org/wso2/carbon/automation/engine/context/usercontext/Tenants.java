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

import java.util.*;

public class Tenants {
    private String domain;
    private Map<String, Tenant> tenants = new HashMap<String, Tenant>();
    private Tenant adminTenant = null;

    public void setDomain(String value) {
        this.domain = value;
    }

    public void setTenants(Map<String, Tenant> tenants) {
        this.tenants = tenants;
    }

    public String getDomain() {
        return domain;
    }

    public Map<String, Tenant> getTenants() {
        return tenants;
    }

    public List<Tenant> getAllTenants() {
        List<Tenant> tenantList = new LinkedList<Tenant>();
        for (Tenant tenant : tenants.values()) {
            tenantList.add(tenant);
        }
        return tenantList;
    }

    public Tenant getTenant(String tenantId) {
        return tenants.get(tenantId);
    }

    public void addTenant(Tenant tenant) {
        tenants.put(tenant.getKey(), tenant);
    }

    public Tenant getAdminTenant() {
        return adminTenant;
    }

    public void setAdminTenant(Tenant adminTenant) {
        this.adminTenant = adminTenant;
    }
}
