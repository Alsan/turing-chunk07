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
package org.wso2.carbon.automation.engine.adminclients;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.server.admin.stub.ServerAdminException;
import org.wso2.carbon.server.admin.stub.ServerAdminStub;

import java.rmi.RemoteException;

public class ServerAdminServiceClient {
    private static final Log log = LogFactory.getLog(ServerAdminServiceClient.class);
    private ServerAdminStub serverAdminStub;
    private final String serviceName = "ServerAdmin";

    /**
     * @param backEndUrl    backend URL
     * @param sessionCookie session cookie
     * @throws org.apache.axis2.AxisFault
     */
    public ServerAdminServiceClient(String backEndUrl, String sessionCookie) throws AxisFault {
        String endPoint = backEndUrl + serviceName;
        serverAdminStub = new ServerAdminStub(endPoint);
        AuthenticateStubClient.authenticateStub(sessionCookie, serverAdminStub);
    }

    /**
     * @param backEndUrl backend URL
     * @param userName   username
     * @param password   password
     * @throws org.apache.axis2.AxisFault
     */
    public ServerAdminServiceClient(String backEndUrl, String userName, String password)
            throws AxisFault {
        String endPoint = backEndUrl + serviceName;
        serverAdminStub = new ServerAdminStub(endPoint);
        AuthenticateStubClient.authenticateStub(userName, password, serverAdminStub);
    }

    public void restartGracefully() throws ServerAdminException, RemoteException {
        serverAdminStub.restartGracefully();
    }

    public void restart() throws ServerAdminException, RemoteException {
        serverAdminStub.restart();
    }

    public void shutdown() throws RemoteException {
        serverAdminStub.shutdown();
    }

    public void shutdownGracefully() throws RemoteException {
        serverAdminStub.shutdownGracefully();
    }
}

