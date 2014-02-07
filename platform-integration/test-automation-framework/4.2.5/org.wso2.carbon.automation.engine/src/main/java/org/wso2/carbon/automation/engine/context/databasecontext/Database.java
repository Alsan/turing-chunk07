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

import java.util.HashMap;

/*
 *
 */
public class Database {
    private String name;
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private HashMap<String, String> properties = new HashMap<String, String>();

    /**
     * @param propertyName  Name of the property desired to add
     * @param propertyValue Value of the property desired to add
     */
    public void addProperty(String propertyName, String propertyValue) {
        properties.put(propertyName, propertyValue);
    }

    /**
     * @param key name of the database property
     * @return property of the database
     */
    public String getPropertyByKey(String key) {
        return properties.get(key);
    }

    /**
     * @param name name to be given to the database
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param url URL value to be set
     */
    public void setURL(String url) {
        this.url = url;
    }

    /**
     * @param userName username to be set
     */
    public void setUsername(String userName) {
        this.username = userName;
    }

    /**
     * @param password password to be set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param driverClassName driver class to be set
     */
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    /**
     * @return name of the database
     */
    public String getName() {
        return name;
    }

    /**
     * @return URL of the database
     */
    public String getURL() {
        return url;
    }

    /**
     * @return username of the database
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return password of the database
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return driver class name of the database
     */
    public String getDriverClassName() {
        return driverClassName;
    }
}
