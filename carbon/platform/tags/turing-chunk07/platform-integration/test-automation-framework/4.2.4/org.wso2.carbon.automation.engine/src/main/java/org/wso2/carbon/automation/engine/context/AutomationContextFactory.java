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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.automation.engine.context.environmentcontext.EnvironmentContextFactory;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.net.URISyntaxException;

public class AutomationContextFactory {
    private static final Log log = LogFactory.getLog(AutomationContextFactory.class);
    AutomationContext automationContext;
    AutomationContextReader automationContextReader;

    public AutomationContextFactory() {
        automationContext = new AutomationContext();
        automationContextReader = new AutomationContextReader();
    }

    public void createAutomationContext(String instanceGroupName, String instanceName, String domain,
                                        String tenantId) throws SAXException, URISyntaxException,
            IOException,XMLStreamException, ConfigurationMismatchException {
        AutomationContext tempContext;
        automationContextReader.readAutomationContext();
        tempContext = automationContextReader.getAutomationContext();
        EnvironmentContextFactory environmentContextFactory = new EnvironmentContextFactory();
        environmentContextFactory.createEnvironmentContext
                (tempContext, instanceGroupName, instanceName, domain, tenantId);
        automationContext.setConfigurationContext(tempContext.getConfigurationContext());
        automationContext.setUserManagerContext(tempContext.getUserManagerContext());
        automationContext.setEnvironmentContext(environmentContextFactory.getEnvironmentContext());
        automationContext.setToolContext(tempContext.getToolContext());
        automationContext.setDatabaseContext(tempContext.getDatabaseContext());
        automationContext.setPlatformContext(tempContext.getPlatformContext());
        automationContext.setFeatureManagementContext(tempContext.getFeatureManagementContext());
        automationContext.setSecurityContext(tempContext.getSecurityContext());
        automationContext.setListenerExtensionContext(tempContext.getListenerExtensionContext());
    }

    protected AutomationContext getBasicContext() throws SAXException, URISyntaxException,
            ConfigurationMismatchException, IOException, XMLStreamException {
        AutomationContext context;
        automationContextReader.readAutomationContext();
        context = automationContextReader.getAutomationContext();
        //checking the semantic error of the automatiom.xml file
        // ContextErrorChecker contextErrorChecker = new ContextErrorChecker(context);
        // contextErrorChecker.checkErrors();
        return context;
    }

    public AutomationContext getAutomationContext() {
        return automationContext;
    }
}
