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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:bundle basename="org.wso2.carbon.eventing.ui.i18n.Resources">
<%
String eventingService = request.getParameter("serviceName");
%>

<tr>
    <td colspan="2">
        <a href="../eventing/index.jsp?serviceName=<%=eventingService%>"><fmt:message key="manage.subscriptions"/></a>
    </td>
</tr>

<%--<a href="../eventing/index.jsp?serviceName=<%=eventingService%>"><fmt:message key="manage.subscriptions"/></a>--%>
</fmt:bundle>