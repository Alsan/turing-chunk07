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
package org.wso2.carbon.automation.engine.servers.wso2server;

import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.automation.engine.FrameworkConstants;
import org.wso2.carbon.automation.engine.adminclients.ServerAdminServiceClient;
import org.wso2.carbon.automation.engine.context.AutomationContext;
import org.wso2.carbon.automation.engine.context.InitialContextPublisher;
import org.wso2.carbon.automation.engine.context.contextenum.Platforms;
import org.wso2.carbon.automation.engine.context.platformcontext.Instance;
import org.wso2.carbon.automation.engine.frameworkutils.ClientConnectionUtil;
import org.wso2.carbon.automation.engine.frameworkutils.CodeCoverageUtils;
import org.wso2.carbon.automation.engine.servermgt.CarbonPackageManager;
import org.wso2.carbon.automation.engine.servers.utils.ServerLogReader;
import org.wso2.carbon.utils.ServerConstants;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ServerManager {
    private static final Log log = LogFactory.getLog(ServerManager.class);
    private int portOffset;
    private Process process;
    private AutomationContext automationContext;
    private ServerLogReader inputStreamHandler;
    private Thread carbonThread = null;
    private Process tempProcess;
    private String hostName;
    boolean isRunning = false;
    private Map<String, String> commandMap;
    private String carbonHome;
    private boolean isCoverageEnable;
    private ServerAdminServiceClient serverAdminServiceClient;
    private static final String SERVER_SHUTDOWN_MESSAGE = "Halting JVM";
    private static final String SERVER_STARTUP_MESSAGE = "Mgt Console URL";
    private static final long DEFAULT_START_STOP_WAIT_MS = 1000 * 60 * 5;

    public ServerManager(Map<String, String> commandMap) throws ZipException {
        this.commandMap = commandMap;
        automationContext = InitialContextPublisher.getContext();
        portOffset = Integer.parseInt(commandMap.get(FrameworkConstants
                .SERVER_STARTUP_PORT_OFFSET_COMMAND));
        CarbonPackageManager carbonPackageManager = new CarbonPackageManager();
        carbonHome = carbonPackageManager.unzipCarbonPackage();
    }

    public ServerManager(int portOffset) throws ZipException {
        this.commandMap = new HashMap<String, String>();
        automationContext = InitialContextPublisher.getContext();
        this.portOffset = portOffset;
        CarbonPackageManager carbonPackageManager = new CarbonPackageManager();
        carbonHome = carbonPackageManager.unzipCarbonPackage();
    }

    public ServerManager() throws ZipException {
        this.commandMap = new HashMap<String, String>();
        automationContext = InitialContextPublisher.getContext();
        this.portOffset = FrameworkConstants.DEFAULT_CARBON_PORT_OFFSET;
        CarbonPackageManager carbonPackageManager = new CarbonPackageManager();
        carbonHome = carbonPackageManager.unzipCarbonPackage();
    }

    public synchronized void startServer() throws Exception {
        startCarbonServer();
    }

    public synchronized void startCarbonServer() throws Exception {
        if (process != null) { // An instance of the server is running
            return;
        }
        isCoverageEnable = automationContext.getConfigurationContext()
                .getConfiguration().isCoverageEnabled();
        try {
            File commandDir = new File(carbonHome);
            String scriptName = FrameworkConstants.SEVER_STARTUP_SCRIPT_NAME;
            if (System.getProperty(FrameworkConstants.SYSTEM_PROPERTY_OS_NAME).
                    toLowerCase().contains("windows")) {
                commandDir = new File(carbonHome + File.separator + "bin");
                String[] cmdArray;
                if (isCoverageEnable) {
                    CodeCoverageUtils.init();
                    CodeCoverageUtils.instrument(carbonHome);
                    cmdArray = new String[]{"cmd.exe", "/c", scriptName + ".bat",
                            "-Demma.properties=" + System.getProperty("emma.properties"),
                            "-Demma.rt.control.port=" + (47653 + portOffset),
                            expandServerStartupCommandList(commandMap)};
                } else {
                    cmdArray = new String[]{"cmd.exe", "/c", scriptName + ".bat",
                            expandServerStartupCommandList(commandMap)};
                }
                tempProcess = Runtime.getRuntime().exec(cmdArray, null, commandDir);
            } else {
                String[] cmdArray;
                if (isCoverageEnable) {
                    CodeCoverageUtils.init();
                    CodeCoverageUtils.instrument(carbonHome);
                    cmdArray = new String[]{"sh", "bin/" + scriptName + ".sh",
                            "-Demma.properties=" + System.getProperty("emma.properties"),
                            expandServerStartupCommandList(commandMap)};
                } else {
                    cmdArray = new String[]{"sh", "bin/" + scriptName + ".sh"};
                }
                tempProcess = Runtime.getRuntime().exec(cmdArray, null, commandDir);
            }
            ServerLogReader errorStreamHandler =
                    new ServerLogReader("errorStream", tempProcess.getErrorStream());
            inputStreamHandler = new ServerLogReader("inputStream", tempProcess.getInputStream());

            /* start the stream readers*/
            inputStreamHandler.start();
            errorStreamHandler.start();
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        shutdown();
                    } catch (Exception e) {
                        log.error("Error while shutting down server ..", e);
                    }
                }
            });
            System.setProperty("user.dir", carbonHome);
            //get the host name of first standalone node
            String hostName =
                    InitialContextPublisher.getContext().getPlatformContext().
                            getAllProductGroups().get(0).getAllStandaloneInstances().get(0).getHost();
            ClientConnectionUtil.waitForPort(Integer.parseInt(FrameworkConstants.
                    SERVER_DEFAULT_HTTPS_PORT) + portOffset,
                    DEFAULT_START_STOP_WAIT_MS, false, hostName);
            //wait until Mgt console url printed.
            long time = System.currentTimeMillis() + 60 * 1000;
            while (! inputStreamHandler.getOutput().contains(SERVER_STARTUP_MESSAGE) &&
                    System.currentTimeMillis() < time) {
                // wait until server startup is completed
            }
            //ClientConnectionUtil.waitForLogin();
        } catch (IOException e) {
            throw new RuntimeException("Unable to start server :", e);
        }
        process = tempProcess;
    }

    public synchronized void shutdown() throws Exception {
        if (process != null) {
            if (ClientConnectionUtil.isPortOpen(Integer.parseInt(FrameworkConstants.
                    SERVER_DEFAULT_HTTPS_PORT) + portOffset, hostName)) {
                String executionEnvironment =
                        automationContext.getConfigurationContext().
                                getConfiguration().getExecutionEnvironment();
                if (executionEnvironment.equals(Platforms.product.name())) {
                    String productGroupName = automationContext.getPlatformContext().
                            getFirstProductGroup().getGroupName();
                    List<Instance> instanceList = automationContext.getPlatformContext().
                            getFirstProductGroup().getAllStandaloneInstances();
                    String backendURL = InitialContextPublisher.getSuperTenantEnvironmentContext
                            (productGroupName, instanceList.get(0).getName()).
                            getEnvironmentConfigurations().getBackEndUrl();
                    String sessionCookie = InitialContextPublisher.getAdminUserSessionCookie
                            (productGroupName, instanceList.get(0).getName());
                    serverAdminServiceClient =
                            new ServerAdminServiceClient(backendURL, sessionCookie);
                    serverAdminServiceClient.shutdown();
                    long time = System.currentTimeMillis() + DEFAULT_START_STOP_WAIT_MS;
                    while (! inputStreamHandler.getOutput().contains(SERVER_SHUTDOWN_MESSAGE) &&
                            System.currentTimeMillis() < time) {
                        // wait until server shutdown is completed
                    }
                    log.info("Server stopped successfully...");
                }
                tempProcess.destroy();
                process.destroy();
                log.info("Server stopped successfully...");
                if (isCoverageEnable) {
                    List<File> list = new ArrayList<File>();
                    list.add(new File(carbonHome));
                    CodeCoverageUtils.generateReports(list);
                }
                if (portOffset == 0) {
                    System.clearProperty(ServerConstants.CARBON_HOME);
                }
            }
            inputStreamHandler.stop();
        }
    }

    private String expandServerStartupCommandList(Map<String, String> commandMap) {
        StringBuilder keyValueBuffer = new StringBuilder();
        String keyValueArray;
        if (commandMap.isEmpty()) {
            commandMap.put(FrameworkConstants.SERVER_STARTUP_PORT_OFFSET_COMMAND, "0");
        }
        Set<Map.Entry<String, String>> entries = commandMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value == null || value.isEmpty()) {
                keyValueBuffer.append(key).append(",");
            } else {
                keyValueBuffer.append(key).append("=").append(value).append(",");
            }
        }
        keyValueArray = keyValueBuffer.toString();
        keyValueArray = keyValueArray.substring(0, keyValueArray.length() - 1);
        return keyValueArray;
    }
}
