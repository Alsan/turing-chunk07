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
package org.wso2.carbon.automation.engine.servermgt;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.automation.engine.FrameworkConstants;
import org.wso2.carbon.automation.engine.FrameworkPathUtil;
import org.wso2.carbon.automation.engine.context.InitialContextPublisher;
import org.wso2.carbon.automation.engine.context.databasecontext.Database;
import org.wso2.carbon.utils.FileManipulator;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CarbonPackageManager {
    private static final Log log = LogFactory.getLog(CarbonPackageManager.class);

    public String unzipCarbonPackage() throws ZipException {
        String carbonTempHome = FrameworkPathUtil.getCarbonTempLocation();
        String carbonHome;
        String carbonZipFileLocation = FrameworkPathUtil.getCarbonZipLocation();
        if (new File(carbonTempHome).exists()) {
            FileManipulator.deleteDir(new File(carbonTempHome));
        }
        try {
            ZipFile zipFile = new ZipFile(FrameworkPathUtil.getCarbonZipLocation());
            carbonHome = carbonTempHome + File.separator + carbonZipFileLocation.
                    substring(carbonZipFileLocation.lastIndexOf(File.separator) + 1,
                    carbonZipFileLocation.indexOf(".zip"));
            log.info("Extracting carbon distribution into : " + carbonTempHome);
            zipFile.extractAll(carbonTempHome);
        } catch (ZipException e) {
            log.error("Unable to extract distribution at " +
                    FrameworkPathUtil.getCarbonZipLocation());
            throw new ZipException("Unable to extract distribution at " +
                    FrameworkPathUtil.getCarbonZipLocation(), e);
        }
        return carbonHome;
    }

    public void copyDBDrivers() throws IOException {
        File lib = new File(FrameworkPathUtil.getCarbonServerLibLocation());
        String driverFolder = FrameworkPathUtil.getSystemResourceLocation() + File.separator
                + "jar" + File.separator;
        File folder = new File(driverFolder);
        File[] listOfFiles = folder.listFiles();
        String[] supportedDatabaseList = FrameworkConstants.LIST_SUPPORTED_DATABASES.split(",");
        List<Database> databaseList = InitialContextPublisher.getContext().getDatabaseContext()
                .getAllDataBaseConfigurations();
        if (listOfFiles != null && listOfFiles.length > 0) {
            for (File driverName : listOfFiles) {
                if (driverName.isFile() &&
                        (driverName.getName().toLowerCase().endsWith(".jar"))) {
                    String dbKey;
                    for (String key : supportedDatabaseList) {
                        if (driverName.getName().contains(key)) {
                            dbKey = key;
                            for (Database database : databaseList) {
                                if (database.getDriverClassName().contains(dbKey)) {
                                    File jar = new File(driverFolder + driverName);
                                    FileManipulator.copyFile(jar, lib);
                                    log.info("Copping JDBC Driver " +
                                            driverName + " to component/lib");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void copySecurityVerificationService() throws IOException {
        String secVerifierDir = System.getProperty(FrameworkConstants.
                SYSTEM_PROPERTY_SEC_VERIFIER_DIRECTORY);
        File srcFile = new File(secVerifierDir + FrameworkConstants.SERVICE_FILE_SEC_VERIFIER);
        String deploymentPath = FrameworkPathUtil.getCarbonServerAxisServiceDirectory();
        File depFile = new File(deploymentPath);
        if (! depFile.exists() && ! depFile.mkdir()) {
            throw new IOException("Error while creating the deployment folder : " + deploymentPath);
        }
        File dstFile = new File(depFile.getAbsolutePath() + File.separator
                + FrameworkConstants.SERVICE_FILE_SEC_VERIFIER);
        log.info("Copying " + srcFile.getAbsolutePath() + " => " + dstFile.getAbsolutePath());
        FileManipulator.copyFile(srcFile, dstFile);
    }
}
