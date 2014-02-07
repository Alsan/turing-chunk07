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
import java.util.LinkedHashMap;
import java.util.List;

public class PlatformContext {
    private LinkedHashMap<String, ProductGroup> productGroups = new LinkedHashMap<String, ProductGroup>();

    public List<ProductGroup> getAllProductGroups() {
        return new ArrayList<ProductGroup>(productGroups.values());
    }

    public ProductGroup getProductGroup(String instanceGroupName) {
        return productGroups.get(instanceGroupName);
    }

    public void setProductGroups(LinkedHashMap<String, ProductGroup> productGroups) {
        this.productGroups = productGroups;
    }

    // this method to add new instance group to the instance group list
    public void addProductGroup(ProductGroup productGroup) {
        productGroups.put(productGroup.getGroupName(), productGroup);
    }

    public ProductGroup getFirstProductGroup() {
        List<ProductGroup> productGroupList = new ArrayList<ProductGroup>(productGroups.values());
        return productGroupList.get(0);
    }
}
