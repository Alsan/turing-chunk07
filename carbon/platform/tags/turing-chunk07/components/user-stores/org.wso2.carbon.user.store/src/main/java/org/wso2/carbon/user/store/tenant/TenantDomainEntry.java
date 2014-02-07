package org.wso2.carbon.user.core.tenant;

//import org.wso2.carbon.user.core.CacheEntry;
import org.wso2.carbon.caching.core.CacheEntry;

/*
 * Copyright 2004,2005 The Apache Software Foundation.
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

/**
 * Date: Oct 1, 2010 Time: 3:48:04 PM
 */

/**
 * Cache entry class for tenant cache.
 */
class TenantDomainEntry extends CacheEntry {

    private static final long serialVersionUID = -973366456167638275L;

    public TenantDomainEntry(String tenantDomainName) {
        this.tenantDomainName = tenantDomainName;
    }

    public String getTenantDomainName() {
        return tenantDomainName;
    }

    private String tenantDomainName;
}
