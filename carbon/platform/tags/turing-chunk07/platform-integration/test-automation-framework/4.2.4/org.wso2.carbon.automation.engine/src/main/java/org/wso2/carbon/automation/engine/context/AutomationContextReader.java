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
package org.wso2.carbon.automation.engine.context;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.automation.engine.FrameworkConstants;
import org.wso2.carbon.automation.engine.FrameworkPathUtil;
import org.wso2.carbon.automation.engine.context.configurationcontext.ConfigurationContextFactory;
import org.wso2.carbon.automation.engine.context.databasecontext.DatabaseContextFactory;
import org.wso2.carbon.automation.engine.context.extensions.ListenerExtensionContextFactory;
import org.wso2.carbon.automation.engine.context.featuremgtcontext.FeatureManagementContextFactory;
import org.wso2.carbon.automation.engine.context.platformcontext.PlatformContextFactory;
import org.wso2.carbon.automation.engine.context.securitycontext.SecurityContextFactory;
import org.wso2.carbon.automation.engine.context.toolcontext.ToolContextFactory;
import org.wso2.carbon.automation.engine.context.usercontext.UserManagerContextFactory;
import org.wso2.carbon.automation.engine.context.utils.AutomationXMLValidator;
import org.xml.sax.SAXException;

import javax.activation.DataHandler;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;

public class AutomationContextReader {
    private static final Log log = LogFactory.getLog(AutomationContextReader.class);
    private static AutomationContextReader contextReaderInstance;
    static AutomationContext automationContext;
    XMLStreamReader xmlStream = null;

    public AutomationContextReader readAutomationContext()
            throws SAXException, URISyntaxException, IOException,
            XMLStreamException, ConfigurationMismatchException {
        synchronized (AutomationContextReader.class) {
            if (contextReaderInstance == null) {
                contextReaderInstance = new AutomationContextReader();
                readContext();
            }
        }
        return contextReaderInstance;
    }

    public AutomationContext getAutomationContext() {
        return automationContext;
    }

    //convert InputStream to temp file
    private static File convertStreamToFile(InputStream in) throws IOException {
        final File tempFile = File.createTempFile
                (FrameworkConstants.AUTOMATION_SCHEMA_NAME,
                        FrameworkConstants.AUTOMATION_SCHEMA_EXTENSION);
        tempFile.deleteOnExit();
        FileOutputStream out = new FileOutputStream(tempFile);
        IOUtils.copy(in, out);
        return tempFile;
    }

    //read context
    private void readContext()
            throws SAXException, URISyntaxException, IOException, XMLStreamException,
            ConfigurationMismatchException {
        DataHandler handler;
        String nodeFile = ContextConstants.CONTEXT_FILE_NAME;
        URL clusterXmlURL = new File(String.format("%s%s",
                FrameworkPathUtil.getSystemResourceLocation(), nodeFile)).toURI().toURL();
        //crete a tem file from getting the resource/automationXMLSchema as input stream
        InputStream inputStream = getClass().getResourceAsStream
                (File.separator + FrameworkConstants.AUTOMATION_SCHEMA_NAME +
                        FrameworkConstants.AUTOMATION_SCHEMA_EXTENSION);
        //create validator class instance giving the automation.xml and automationXMLSchema.xml paths
        AutomationXMLValidator automationXMLValidator = new AutomationXMLValidator
                (clusterXmlURL.getPath(), convertStreamToFile(inputStream).getAbsolutePath());
        //validate the automation.xml file
        //  Boolean isConfigFileCorrect = automationXMLValidator.validateAutomationXML();
        log.info("Automation.xml file is validated successfully.");
        handler = new DataHandler(clusterXmlURL);
        xmlStream = XMLInputFactory.newInstance()
                .createXMLStreamReader(handler.getInputStream());
        getAutomationPlatform(xmlStream);
    }

    private AutomationContext getAutomationPlatform(XMLStreamReader xmlStreamReader)
            throws ConfigurationMismatchException {
        StAXOMBuilder builder = new StAXOMBuilder(xmlStreamReader);
        OMElement endPointElem = builder.getDocumentElement();
        automationContext = new AutomationContext();
        DatabaseContextFactory databaseContextFactory = new DatabaseContextFactory();
        ConfigurationContextFactory configContextFactory = new ConfigurationContextFactory();
        PlatformContextFactory platformContextFactory = new PlatformContextFactory();
        SecurityContextFactory securityContextFactory = new SecurityContextFactory();
        ToolContextFactory toolsContextFactory = new ToolContextFactory();
        ListenerExtensionContextFactory listenerExtensionContextFactory = new ListenerExtensionContextFactory();
        UserManagerContextFactory userManagerContextFactory = new UserManagerContextFactory();
        FeatureManagementContextFactory featureManagementContextFactory
                = new FeatureManagementContextFactory();
        OMElement nodeElement;
        Iterator elemChildren = endPointElem.getChildElements();
        while (elemChildren.hasNext()) {
            nodeElement = (OMElement) elemChildren.next();
            if (nodeElement.getLocalName().equals(ContextConstants.CONFIGURATION_CONTEXT_NODE)) {
                configContextFactory.createConfigurationContext(nodeElement);
            }
            if (nodeElement.getLocalName().equals(ContextConstants.DATABASE_CONTEXT_NODE)) {
                databaseContextFactory.createDatabaseContext(nodeElement);
            }
            if (nodeElement.getLocalName().equals(ContextConstants.PLATFORM_CONTEXT_NODE)) {
                platformContextFactory.createPlatformContext(nodeElement);
            }
            if (nodeElement.getLocalName().equals(ContextConstants.SECURITY_CONTEXT_NODE)) {
                securityContextFactory.createSecurityContext(nodeElement);
            }
            if (nodeElement.getLocalName().equals(ContextConstants.TOOLS_CONTEXT_NODE)) {
                toolsContextFactory.createToolContext(nodeElement);
            }
            if (nodeElement.getLocalName().equals(ContextConstants.USER_MANAGEMENT_CONTEXT_NODE)) {
                userManagerContextFactory.createUserManagementContext(nodeElement);
            }
            if (nodeElement.getLocalName().equals(ContextConstants.FEATURE_MANAGEMENT_CONTEXT_NODE)) {
                featureManagementContextFactory.createFeatureManagementContext(nodeElement);
            }
            if (nodeElement.getLocalName().equals(ContextConstants.LISTENER_EXTENSION_NODE)) {
                listenerExtensionContextFactory.createListerExtensionContext(nodeElement);
            }
        }
        automationContext.setConfigurationContext(configContextFactory.getConfigurationContext());
        automationContext.setDatabaseContext(databaseContextFactory.getDatabaseContext());
        automationContext.setFeatureManagementContext(featureManagementContextFactory
                .getFeatureManagementContext());
        automationContext.setPlatformContext(platformContextFactory.getPlatformContext());
        automationContext.setSecurityContext(securityContextFactory.getSecurityContext());
        automationContext.setUserManagerContext(userManagerContextFactory
                .getUserManagementContext());
        automationContext.setToolContext(toolsContextFactory.getToolContext());
        automationContext.setListenerExtensionContext(listenerExtensionContextFactory.getListenerExtensionContext());
        return automationContext;
    }
}

