<%--
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
--%>

<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="org.wso2.carbon.context.CarbonContext" %>

<%

    /*ConfigurationContext configContext =
            (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);*/
    String mgtConsoleUrl = CarbonUIUtil.getAdminConsoleURL(request);
    String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);
    String appName = (String) CarbonContext.getThreadLocalCarbonContext().getApplicationName();
    String tenantDomain = (String) CarbonContext.getThreadLocalCarbonContext().getTenantDomain();
    int tenantId = CarbonContext.getThreadLocalCarbonContext().getTenantId();
%>
<script type="text/javascript">
    jQuery(document).ready(function(){
        var SUPER_TENENT_ID = -1234;
        var tenentId = <%=tenantId%>;
        var tenantDomain = "<%=tenantDomain%>";
        var dashboardUrl;
        var contextRoot = "<%=appName%>";
        if(tenentId == SUPER_TENENT_ID){
            if((contextRoot === null) || (contextRoot == "") || (contextRoot == 'null')){
                dashboardUrl = "../../activitymonitoring/login.jag";
            }
            else{
                dashboardUrl = "../../../activitymonitoring/login.jag";
            }
        }
        else{
            if((contextRoot === null) || (contextRoot == "") || (contextRoot == 'null')){
                dashboardUrl = "../../../../activitymonitoring/login.jag";
            }
            else{
                dashboardUrl = "../../../../../activitymonitoring/login.jag";
            }
        }
        location.href = dashboardUrl;
    });

</script>