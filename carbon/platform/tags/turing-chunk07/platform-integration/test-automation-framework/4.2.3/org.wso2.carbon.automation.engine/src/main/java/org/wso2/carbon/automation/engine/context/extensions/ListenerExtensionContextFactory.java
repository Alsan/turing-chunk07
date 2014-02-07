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
package org.wso2.carbon.automation.engine.context.extensions;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.automation.engine.context.ConfigurationMismatchException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/*
 * this class used to create and get the ListenerExtensionContext
 */
public class ListenerExtensionContextFactory {
    private static final Log log = LogFactory.getLog(ListenerExtensionContextFactory.class);
    ListenerExtensionContext listenerExtensionContext;

    public ListenerExtensionContextFactory() {
        listenerExtensionContext = new ListenerExtensionContext();
    }

    /**
     * creates the ListenerExtensionContext
     *
     * @param omElement OMElement type data given by the context reader
     */
    public void createListerExtensionContext(OMElement omElement)
            throws ConfigurationMismatchException {
        LinkedHashMap<String, ListenerExtension> listenerExtensionHashMap = new LinkedHashMap<String, ListenerExtension>();
        Iterator listenerExtensions = omElement.getChildElements();
        OMElement listenerExtensionNode;
        while (listenerExtensions.hasNext()) {
            ListenerExtension listenerExtension = new ListenerExtension();
            listenerExtensionNode = (OMElement) listenerExtensions.next();
            listenerExtension.setName(listenerExtensionNode.getLocalName());
            listenerExtension.setListeners(setListenerMap(listenerExtensionNode));
            listenerExtensionHashMap.put(listenerExtensionNode.getLocalName(), listenerExtension);
        }
        listenerExtensionContext.setListenerExtensions(listenerExtensionHashMap);
    }

    /**
     * returns the ListenerExtensionContext
     *
     * @return the listener extension context
     */
    public ListenerExtensionContext getListenerExtensionContext() {
        return listenerExtensionContext;
    }

    private List<TestClass> setTestClasses(OMElement listerNode)
            throws ConfigurationMismatchException {
        List<TestClass> testClassList = new ArrayList<TestClass>();
        Iterator classIterator = listerNode.getChildElements();
        OMElement classElement;
        while (classIterator.hasNext()) {
            classElement = (OMElement) classIterator.next();
            TestClass cls = new TestClass();
            String classPath = classElement.getText();
            if (classPath.equals("")) {
                log.error("Error in Automation.xml.One of Listener extension has" +
                        " class element without path to the class");
                throw new ConfigurationMismatchException("Error occurred in Automation.xml");
            }
            cls.setClassPath(classElement.getText());
            testClassList.add(cls);
        }
        return testClassList;
    }

    private LinkedHashMap<String, Listener> setListenerMap(OMElement omElement1)
            throws ConfigurationMismatchException {
        LinkedHashMap<String, Listener> listenerHashMap = new LinkedHashMap<String, Listener>();
        Iterator listenerIterator = omElement1.getChildElements();
        while (listenerIterator.hasNext()) {
            Listener listener = new Listener();
            OMElement classListNode = (OMElement) listenerIterator.next();
            listener.setName(classListNode.getLocalName());
            listener.setClassList(setTestClasses(classListNode));
            listenerHashMap.put(classListNode.getLocalName(), listener);
        }
        return listenerHashMap;
    }
}
