<!--
 ~ Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 ~
 ~ WSO2 Inc. licenses this file to you under the Apache License,
 ~ Version 2.0 (the "License"); you may not use this file except
 ~ in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~    http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing,
 ~ software distributed under the License is distributed on an
 ~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 ~ KIND, either express or implied.  See the License for the
 ~ specific language governing permissions and limitations
 ~ under the License.
 -->
<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="org.wso2.carbon.xkms.mgt.ui.Utils" %>
<%@ page import="org.wso2.carbon.xkms.mgt.ui.client.XKMSMgtClient" %>
<%

    String serverURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
    ConfigurationContext configContext =
            (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
    String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);
    String forwardTo = null;
    String BUNDLE = "org.wso2.carbon.xkms.mgt.ui.i18n.Resources";
	ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE, request.getLocale());

    try {
        XKMSMgtClient client = new XKMSMgtClient(cookie, serverURL, configContext);
        XKMSConfigData configData = Utils.populateXKMSConfig(request);
        client.setXKMSConfig(configData);
        String message = resourceBundle.getString("xkms.configuration.updated.successfully");
        forwardTo="edit_configuration.jsp?region=region1&item=xkms_menu&ordinal=0";
        CarbonUIMessage.sendCarbonUIMessage(message,CarbonUIMessage.INFO, request);
    } catch (Exception e) {
        CarbonUIMessage.sendCarbonUIMessage(e.getMessage(), CarbonUIMessage.ERROR, request, e);
        forwardTo = "../admin/error.jsp";
    }

%>


<%@page import="java.util.ResourceBundle"%>
<%@ page import="org.wso2.carbon.xkms.mgt.stub.types.XKMSConfigData" %>
<script type="text/javascript">
    function forward() {
        location.href = "<%=forwardTo%>";
    }
</script>

<script type="text/javascript">
   forward();
</script>