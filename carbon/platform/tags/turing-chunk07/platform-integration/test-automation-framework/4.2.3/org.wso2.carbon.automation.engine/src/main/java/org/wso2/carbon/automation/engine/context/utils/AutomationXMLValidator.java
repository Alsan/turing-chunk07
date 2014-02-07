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
package org.wso2.carbon.automation.engine.context.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Validate the automation.xml file
 */
public class AutomationXMLValidator {
    private File schemaFile;
    private Source automationXML;
    private static final Log log = LogFactory.getLog(AutomationXMLValidator.class);

    /**
     * @param automationXMLFilePath path to automation.xml file
     * @param schemaFilePath        path to schema xsd file
     * @throws java.net.MalformedURLException
     */
    public AutomationXMLValidator(String automationXMLFilePath, String schemaFilePath)
            throws MalformedURLException {
        this.automationXML = new StreamSource(new File(automationXMLFilePath));
        this.schemaFile = new File(schemaFilePath);
    }

    //validate the automation.xml vs the xsd file
    public boolean validateAutomationXML() throws SAXException, IOException {
        SchemaFactory schemaFactory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(schemaFile);
        Validator validator = schema.newValidator();
        try {
            validator.validate(automationXML);
        } catch (SAXException e) {
            log.error("Error occurred in the automation.xml file format. Error details: " +
                    e.getLocalizedMessage());
            throw new SAXException("Error occurred in the automation.xml file" +
                    " format. Error details: " +
                    e.getLocalizedMessage());
        }
        return true;
    }
}
