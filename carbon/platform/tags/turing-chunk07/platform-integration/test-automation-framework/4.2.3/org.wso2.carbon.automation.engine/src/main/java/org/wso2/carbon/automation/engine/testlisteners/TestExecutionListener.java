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
package org.wso2.carbon.automation.engine.testlisteners;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.IExecutionListener;
import org.wso2.carbon.automation.engine.TestNGExtensionExecutor;
import org.wso2.carbon.automation.engine.context.ConfigurationMismatchException;
import org.wso2.carbon.automation.engine.context.ContextConstants;
import org.wso2.carbon.automation.engine.context.InitialContextPublisher;
import org.wso2.carbon.automation.engine.context.contextenum.Platforms;
import org.wso2.carbon.automation.engine.context.extensions.Listener;
import org.wso2.carbon.automation.engine.context.extensions.ListenerExtension;
import org.wso2.carbon.automation.engine.servers.wso2server.ServerManager;
import org.wso2.carbon.automation.engine.usermgt.UserPopulateHandler;
import org.xml.sax.SAXException;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;

public class TestExecutionListener implements IExecutionListener {
    private LinkedHashMap<String, ListenerExtension> listenerList;
    ServerManager serverManager;
    private TestNGExtensionExecutor testNGExtensionExecutor;
    private static final Log log = LogFactory.getLog(TestExecutionListener.class);
    InitialContextPublisher initialContextPublisher = null;

    /**
     * class before all test suits execution
     */
    public void onExecutionStart() {
        //read and build the automation context
        try {
            InitialContextPublisher.getContext();
            //start the server
            if (InitialContextPublisher.getContext().getConfigurationContext().getConfiguration().
                    getExecutionEnvironment().equals(Platforms.product.name())) {
                serverManager = new ServerManager();
                serverManager.startServer();
            }
            log.info("Start user populating");
            UserPopulateHandler.populateUsers();
            listenerList = InitialContextPublisher.getContext().
                    getListenerExtensionContext().getAllListenerExtensions();
            Listener listener =
                    listenerList.get(ContextConstants.TESTNG_EXTENSION_PLATFORM_EXECUTION_MANAGER)
                            .getListener(ContextConstants.
                                    TESTNG_EXTENSION_EXECUTION_MANAGER_EXECUTION_START);
            testNGExtensionExecutor = new TestNGExtensionExecutor();
            testNGExtensionExecutor.executeServices(listener.getClassList());
        } catch (SAXException e) {
            handleException("Error occurred reading automation.xml file", e);
        } catch (URISyntaxException e) {
            handleException("Error occurred reading automation.xml file", e);
        } catch (ConfigurationMismatchException e) {
            handleException("Automation.xml content is inconsistent ", e);
        } catch (InterruptedException e1) {
            handleException("Error on initializing system ", e1);
        } catch (Exception e1) {
            handleException("Error on initializing system ", e1);
        }
    }

    /**
     * calls after all test suite execution
     */
    public void onExecutionFinish() {
        Listener listener =
                listenerList.get(ContextConstants.TESTNG_EXTENSION_PLATFORM_EXECUTION_MANAGER)
                        .getListener(ContextConstants.
                                TESTNG_EXTENSION_EXECUTION_MANAGER_EXECUTION_FINISH);
        testNGExtensionExecutor = new TestNGExtensionExecutor();
        try {
            testNGExtensionExecutor.executeServices(listener.getClassList());
            log.info("Loaded the classList of the " + " listener");
            //delete populated users
            // UserPopulateHandler.deleteUsers();
            Thread.sleep(4000);
            //shutting down the carbon server
            // serverManager.shutdown();
        } catch (InstantiationException e) {
            handleException("Error when shutting down the test execution", e);
        } catch (ClassNotFoundException e) {
            handleException("Error when shutting down the test execution", e);
        } catch (IllegalAccessException e) {
            handleException("Error when shutting down the test execution", e);
        } catch (NoSuchMethodException e) {
            handleException("Error when shutting down the test execution", e);
        } catch (InvocationTargetException e) {
            handleException("Error when shutting down the test execution", e);
        } catch (Exception e) {
            handleException("Error while deleting users", e);
        }
    }

    private void handleException(String msg, Exception e) {
        throw new RuntimeException(msg, e);
    }
}