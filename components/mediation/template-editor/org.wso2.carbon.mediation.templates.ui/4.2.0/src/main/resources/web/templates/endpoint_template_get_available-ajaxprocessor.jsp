<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%--
  ~  Copyright (c) 2008, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
  ~
  ~  Licensed under the Apache License, Version 2.0 (the "License");
  ~  you may not use this file except in compliance with the License.
  ~  You may obtain a copy of the License at
  ~
  ~        http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~  Unless required by applicable law or agreed to in writing, software
  ~  distributed under the License is distributed on an "AS IS" BASIS,
  ~  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~  See the License for the specific language governing permissions and
  ~  limitations under the License.
  --%>

<%@ page import="org.apache.synapse.endpoints.Template" %>
<%@ page import="org.wso2.carbon.mediation.templates.ui.EndpointTemplateAdminClient" %>

<%

    EndpointTemplateAdminClient endpointClient
            = new EndpointTemplateAdminClient(this.getServletConfig(), session);

    String[] allEndpointTemplates = endpointClient.getAllTempalateNames();%>

        <option value="default">Select From Templates</option>
        <%
            for (String templateName : allEndpointTemplates) {%>

        <option value="<%=templateName%>"><%=templateName%>

        </option>

<%}%>


