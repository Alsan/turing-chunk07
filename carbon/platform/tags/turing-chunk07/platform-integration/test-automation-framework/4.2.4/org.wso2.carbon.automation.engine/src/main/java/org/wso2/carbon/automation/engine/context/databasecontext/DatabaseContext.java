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
package org.wso2.carbon.automation.engine.context.databasecontext;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * has the methods : operations for database contexts
 */
public class DatabaseContext {
    private LinkedHashMap<String, Database> databaseConfigurationMap;

    public DatabaseContext() {
        databaseConfigurationMap = new LinkedHashMap<String, Database>();
    }

    protected void setDatabaseConfigurationMap(LinkedHashMap<String, Database> config) {
        this.databaseConfigurationMap = config;
    }

    protected LinkedHashMap<String, Database> getDatabaseConfigurationMap() {
        return databaseConfigurationMap;
    }

    public Database getDatabase(String databaseName) {
        return databaseConfigurationMap.get(databaseName);
    }

    public void addDatabase(Database database) {
        databaseConfigurationMap.put(database.getName(), database);
    }

    /**
     * @return List of all database configurations
     */
    public List<Database> getAllDataBaseConfigurations() {
        List<Database> databaseList = new LinkedList<Database>();
        for (Database database : databaseConfigurationMap.values()) {
            databaseList.add(database);
        }
        return databaseList;
    }
}


