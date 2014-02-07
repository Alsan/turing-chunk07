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
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.wso2.carbon.automation.engine.TestNGExtensionExecutor;
import org.wso2.carbon.automation.engine.context.ContextConstants;
import org.wso2.carbon.automation.engine.context.InitialContextPublisher;
import org.wso2.carbon.automation.engine.context.extensions.Listener;
import org.wso2.carbon.automation.engine.context.extensions.ListenerExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class TestSuiteListener implements ISuiteListener {
    private static final Log log = LogFactory.getLog(TestSuiteListener.class);
    private HashMap<String, ListenerExtension> listenerList;
    private TestNGExtensionExecutor testNGExtensionExecutor;

    public void onStart(ISuite iSuite) {
        log.info("Started Suit manager onStart");
        listenerList = InitialContextPublisher.getContext().
                getListenerExtensionContext().getAllListenerExtensions();
        Listener listener =
                listenerList.get(ContextConstants.TESTNG_EXTENSION_PLATFORM_SUITE_MANAGER)
                        .getListener(ContextConstants.TESTNG_EXTENSION_PLATFORM_SUITE_MANAGER_START);
        testNGExtensionExecutor = new TestNGExtensionExecutor();
        try {
            testNGExtensionExecutor.executeServices(listener.getClassList());
        } catch (InstantiationException e) {
            log.error("Error occurred :", e);
        } catch (ClassNotFoundException e) {
            log.error("Error occurred :", e);
        } catch (IllegalAccessException e) {
            log.error("Error occurred :", e);
        } catch (NoSuchMethodException e) {
            log.error("Error occurred :", e);
        } catch (InvocationTargetException e) {
            log.error("Error occurred :", e);
        }
    }

    public void onFinish(ISuite iSuite) {
        log.info("Started Suit manager onFinish");
        listenerList = InitialContextPublisher.getContext().
                getListenerExtensionContext().getAllListenerExtensions();
        Listener listener =
                listenerList.get(ContextConstants.TESTNG_EXTENSION_PLATFORM_SUITE_MANAGER)
                        .getListener(ContextConstants.TESTNG_EXTENSION_PLATFORM_SUITE_MANAGER_START);
        testNGExtensionExecutor = new TestNGExtensionExecutor();
        try {
            testNGExtensionExecutor.executeServices(listener.getClassList());
        } catch (InstantiationException e) {
            log.error("Error occurred :", e);
        } catch (ClassNotFoundException e) {
            log.error("Error occurred :", e);
        } catch (IllegalAccessException e) {
            log.error("Error occurred :", e);
        } catch (NoSuchMethodException e) {
            log.error("Error occurred :", e);
        } catch (InvocationTargetException e) {
            log.error("Error occurred :", e);
        }
    }
}
