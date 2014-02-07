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
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import org.wso2.carbon.automation.engine.TestNGExtensionExecutor;
import org.wso2.carbon.automation.engine.annotations.ExecutionEnvironment;
import org.wso2.carbon.automation.engine.annotations.SetEnvironment;
import org.wso2.carbon.automation.engine.context.ContextConstants;
import org.wso2.carbon.automation.engine.context.InitialContextPublisher;
import org.wso2.carbon.automation.engine.context.extensions.Listener;
import org.wso2.carbon.automation.engine.context.extensions.ListenerExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class TestTransformerListener implements IAnnotationTransformer {
    private static final Log log = LogFactory.getLog(TestTransformerListener.class);
    private HashMap<String, ListenerExtension> listenerList;
    private TestNGExtensionExecutor testNGExtensionExecutor;

    public void transform(ITestAnnotation iTestAnnotation, Class aClass,
                          Constructor constructor, Method method) {
        log.info("Started Transform manager transform");
        if (method.getDeclaringClass().getAnnotation(SetEnvironment.class) != null) {
            ExecutionEnvironment[] classAnnotationList = method.getDeclaringClass().getAnnotation(SetEnvironment.class).executionEnvironments();
            compareAnnotation(iTestAnnotation, method, classAnnotationList);
        } else if (method.getAnnotation(SetEnvironment.class) != null) {
            ExecutionEnvironment[] annotationList = method.getAnnotation(SetEnvironment.class).executionEnvironments();
            compareAnnotation(iTestAnnotation, method, annotationList);
        } else {
            iTestAnnotation.setGroups(new String[]{method.getClass().getName()});
            iTestAnnotation.setTestName(method.getClass().getName());
        }
        listenerList = InitialContextPublisher.getContext().
                getListenerExtensionContext().getAllListenerExtensions();
        Listener listener =
                listenerList.get(ContextConstants.TESTNG_EXTENSION_PLATFORM_ANNOTATION_MANAGER)
                        .getListener(ContextConstants.
                                TESTNG_EXTENSION_PLATFORM_ANNOTATION_MANAGER_TRANSFORM);
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




    private void compareAnnotation(ITestAnnotation iTestAnnotation, Method method,
                                   ExecutionEnvironment[] classAnnotationList) {
        for (ExecutionEnvironment annotation : classAnnotationList) {
            if (annotationComparator(annotation.toString())) {
                iTestAnnotation.setGroups(new String[]{method.getClass().getName()});
                iTestAnnotation.setTestName(method.getClass().getName());
            } else {
                iTestAnnotation.setEnabled(false);
                log.info("Skipped method <" + method.getName() + "> on annotation <" +
                        annotation.name() + ">");
                break;
            }
        }
    }

    private boolean annotationComparator(String annotation) {
        boolean compSetup = false;
        if (annotation.equals(ExecutionEnvironment.all.name())) {
            compSetup = true;
        } else if (annotation.equals(ExecutionEnvironment.product.name())) {

            compSetup = true;

        } else if (annotation.equals(ExecutionEnvironment.platform.name())) {

            compSetup = true;
        }
        return compSetup;
    }
}
