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
package org.wso2.carbon.automation.engine.context.featuremgtcontext;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class FeatureManagementContext {
    private LinkedHashMap<String, P2Repositories> p2Repositories = new LinkedHashMap<String, P2Repositories>();

    private LinkedHashMap<String, P2Repositories> getP2RepositoriesMap() {
        return p2Repositories;
    }

    public List<P2Repositories> getAllP2Repositories() {
        List<P2Repositories> repoList = new LinkedList<P2Repositories>();
        for (P2Repositories p2Repositories1 : p2Repositories.values()) {
            repoList.add(p2Repositories1);
        }
        return repoList;
    }

    public P2Repositories getP2Repository(String name) {
        return p2Repositories.get(name);
    }

    public void setP2Repositories(LinkedHashMap<String, P2Repositories> p2Repositories) {
        this.p2Repositories = p2Repositories;
    }
}
