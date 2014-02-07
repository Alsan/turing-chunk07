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

package org.wso2.carbon.automation.core.utils.frameworkutils.productsetters;

import org.wso2.carbon.automation.core.utils.environmentutils.EnvironmentBuilder;
import org.wso2.carbon.automation.core.utils.environmentutils.ProductUrlGeneratorUtil;
import org.wso2.carbon.automation.core.utils.frameworkutils.EnvironmentSetter;
import org.wso2.carbon.automation.core.utils.frameworkutils.productvariables.ProductVariables;
import org.wso2.carbon.automation.core.utils.frameworkutils.productvariables.WorkerVariables;

import java.util.Properties;

public class AMSetter extends EnvironmentSetter {

    ProductVariables productVariables = new ProductVariables();
    WorkerVariables workerVariables = new WorkerVariables();
    EnvironmentBuilder environmentBuilder = new EnvironmentBuilder();
    public String managerHostName;
    public String managerHttpPort;
    public String managerNHttpPort;
    public String managerHttpsPort;
    public String managerNHttpsPort;
    public String managerWebContextRoot;
    public String workerHostName = null;
    public String workerHttpPort = null;
    public String workerNHttpPort = null;
    public String workerHttpsPort = null;
    public String workerNHttpsPort = null;
    public static String workerWebContextRoot = null;

    public Properties properties;


    public AMSetter() {
        this.properties = new ProductUrlGeneratorUtil().getStream();
        String hostNames;
        if (Boolean.parseBoolean(prop.getProperty("stratos.test"))) {
            hostNames = (properties.getProperty("am.service.host.name", "am.stratoslive.wso2.com"));
        } else {
            hostNames = (properties.getProperty("am.host.name", "localhost"));
        }
        String httpPorts = (prop.getProperty("am.http.port", "9765"));
        String httpsPorts = (prop.getProperty("am.https.port", "9445"));
        String webContextRoots = (prop.getProperty("am.webContext.root", null));
        String nHttpPorts = (prop.getProperty("am.nhttp.port", "8280"));
        String nHttpsPorts = (prop.getProperty("am.nhttps.port", "8243"));

        if (hostNames.contains(",")) {
            managerHostName = hostNames.split(",")[0];
            workerHostName = hostNames.split(",")[1];
        } else {
            managerHostName = hostNames;
        }
        if (httpPorts.contains(",")) {
            managerHttpPort = httpPorts.split(",")[0];
            workerHttpPort = httpPorts.split(",")[1];
        } else {
            managerHttpPort = httpPorts;
        }
        if (httpsPorts.contains(",")) {
            managerHttpsPort = httpsPorts.split(",")[0];
            workerHttpsPort = httpsPorts.split(",")[1];
        } else {
            managerHttpsPort = httpsPorts;
        }
        if (webContextRoots != null) {
            if (webContextRoots.contains(",")) {
                managerWebContextRoot = webContextRoots.split(",")[0];
                workerWebContextRoot = webContextRoots.split(",")[1];
            } else {
                managerWebContextRoot = webContextRoots;
            }
        } else {
            managerWebContextRoot = webContextRoots;
        }

        if (nHttpPorts.contains(",")) {
            managerNHttpPort = nHttpPorts.split(",")[0];
            workerNHttpPort = nHttpPorts.split(",")[1];
        } else {
            managerNHttpPort = nHttpPorts;
        }
        if (nHttpsPorts.contains(",")) {
            managerNHttpsPort = nHttpsPorts.split(",")[0];
            workerNHttpsPort = nHttpsPorts.split(",")[1];
        } else {
            managerNHttpsPort = nHttpsPorts;
        }
    }

    public ProductVariables getProductVariables() {
        ProductUrlGeneratorUtil productUrlGeneratorUtil = new ProductUrlGeneratorUtil();
        this.properties = new ProductUrlGeneratorUtil().getStream();

        productVariables.setProductVariables
                (this.managerHostName, this.managerHttpPort, this.managerHttpsPort,
                 this.managerWebContextRoot, this.managerNHttpPort, this.managerNHttpsPort,
                 productUrlGeneratorUtil.getBackendUrl(managerHttpsPort, managerHostName,
                                                       managerWebContextRoot));

        return productVariables;
    }

    public WorkerVariables getWorkerVariables() {
        ProductUrlGeneratorUtil productUrlGeneratorUtil = new ProductUrlGeneratorUtil();
        this.properties = new ProductUrlGeneratorUtil().getStream();

        if (environmentBuilder.getFrameworkSettings().getEnvironmentSettings().isClusterEnable()) {

            workerVariables.setWorkerVariables(workerHostName, workerHttpPort, workerHttpsPort,
                                               workerWebContextRoot, workerNHttpPort, workerNHttpsPort,
                                               productUrlGeneratorUtil.getBackendUrl(workerHttpsPort, workerHostName,
                                                                                     workerWebContextRoot));
        }
        return workerVariables;
    }
}
