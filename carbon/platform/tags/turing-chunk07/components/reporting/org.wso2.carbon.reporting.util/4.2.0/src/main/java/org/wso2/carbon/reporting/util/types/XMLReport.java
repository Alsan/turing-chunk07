/**
 * Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.reporting.util.types;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import org.wso2.carbon.reporting.api.ReportingException;

import java.io.ByteArrayOutputStream;


/**
 * used to generate xml data report from given JasperPrint
 */
public class XMLReport {
    public ByteArrayOutputStream generateXmlReport(JasperPrint jasperPrint)
            throws JRException, ReportingException {
        ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
        if(jasperPrint==null){
            throw new ReportingException("jasperPrint null, can't convert to  XML report");
        }
        JRXmlExporter jrXmlExporter = new JRXmlExporter();
        jrXmlExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        jrXmlExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xmlOutputStream);
        try {
            jrXmlExporter.exportReport();

        } catch (JRException e) {
            throw new JRException("Error occurred exporting PDF report ", e);
        }
        return xmlOutputStream;
    }
}
