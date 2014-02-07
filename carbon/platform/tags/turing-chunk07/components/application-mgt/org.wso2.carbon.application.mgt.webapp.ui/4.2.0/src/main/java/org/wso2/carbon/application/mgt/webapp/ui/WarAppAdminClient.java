/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.application.mgt.webapp.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Options;
import org.wso2.carbon.application.mgt.webapp.stub.WarApplicationAdminStub;
import org.wso2.carbon.application.mgt.webapp.stub.types.carbon.WarCappMetadata;

import java.util.ResourceBundle;
import java.util.Locale;

public class WarAppAdminClient {

    private static final Log log = LogFactory.getLog(WarAppAdminClient.class);
    private static final String BUNDLE = "org.wso2.carbon.application.mgt.webapp.ui.i18n.Resources";
    private ResourceBundle bundle;
    public WarApplicationAdminStub stub;

    public WarAppAdminClient(String cookie,
                             String backendServerURL,
                             ConfigurationContext configCtx,
                             Locale locale) throws AxisFault {
        String serviceURL = backendServerURL + "WarApplicationAdmin";
        bundle = ResourceBundle.getBundle(BUNDLE, locale);

        stub = new WarApplicationAdminStub(configCtx, serviceURL);
        ServiceClient client = stub._getServiceClient();
        Options option = client.getOptions();
        option.setManageSession(true);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);
    }

    public WarCappMetadata[] getWarAppData(String appName) throws AxisFault {
        try {
            return stub.getWarAppData(appName);
        } catch (java.lang.Exception e) {
            handleException(bundle.getString("cannot.get.service.data"), e);
        }
        return null;
    }

    /**
     * This is used to get the jaxws webapps metadata from the BE
     * @param appName - input app name
     * @return Array of WarCappMetadata with found jaxws webapps
     * @throws AxisFault rror on retrieving service data from BE
     */
    public WarCappMetadata[] getJaxWSWarAppData(String appName) throws AxisFault {
        try {
            return stub.getJaxWSWarAppData(appName);
        } catch (java.lang.Exception e) {
            handleException(bundle.getString("cannot.get.service.data"), e);
        }
        return null;
    }

    private void handleException(String msg, java.lang.Exception e) throws AxisFault {
        log.error(msg, e);
        throw new AxisFault(msg, e);
    }
}
