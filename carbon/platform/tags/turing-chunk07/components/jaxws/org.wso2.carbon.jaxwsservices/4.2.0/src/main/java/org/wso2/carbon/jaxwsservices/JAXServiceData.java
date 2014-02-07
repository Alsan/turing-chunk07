/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.jaxwsservices;

import javax.activation.DataHandler;

/**
 * Represents JAX-WS service data set that is uploaded via the management console
 */

public class JAXServiceData {
    private String fileName;
    private DataHandler dataHandler;
    private String serviceHierarchy;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

        public DataHandler getDataHandler() {
        return dataHandler;
    }

    public void setDataHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public String getServiceHierarchy() {
        return serviceHierarchy;
    }

    public void setServiceHierarchy(String serviceHierarchy) {
        this.serviceHierarchy = serviceHierarchy;
    }
}