<!--
~ Copyright (c) 2005-2013, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
~
~ WSO2 Inc. licenses this file to you under the Apache License,
~ Version 2.0 (the "License"); you may not use this file except
~ in compliance with the License.
~ You may obtain a copy of the License at
~
~ http://www.apache.org/licenses/LICENSE-2.0
~
~ Unless required by applicable law or agreed to in writing,
~ software distributed under the License is distributed on an
~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
~ KIND, either express or implied. See the License for the
~ specific language governing permissions and limitations
~ under the License.
-->

<%@ page import="org.apache.axis2.context.ConfigurationContext"%>
<%@ page import="org.wso2.carbon.CarbonConstants"%>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage"%>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil"%>
<%@ page import="org.wso2.carbon.utils.ServerConstants"%>

<%@ page import="java.util.ResourceBundle"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar"
	prefix="carbon"%>

<script type="text/javascript" src="extensions/js/vui.js"></script>
<script type="text/javascript" src="../extensions/core/js/vui.js"></script>
<script type="text/javascript" src="../admin/js/main.js"></script>

<jsp:include page="../dialog/display_messages.jsp" />

<fmt:bundle
	basename="org.wso2.carbon.identity.application.mgt.ui.i18n.Resources">
	<carbon:breadcrumb label="application.mgt"
		resourceBundle="org.wso2.carbon.identity.application.mgt.ui.i18n.Resources"
		topPage="true" request="<%=request%>" />

	<script type="text/javascript" src="../carbon/admin/js/breadcrumbs.js"></script>
	<script type="text/javascript" src="../carbon/admin/js/cookies.js"></script>
	<script type="text/javascript" src="../carbon/admin/js/main.js"></script>

	<div id="middle">

		<h2>
			<fmt:message key='application.list.application' />
		</h2>

		<div id="workArea">

			<table style="width: 100%" class="styledLeft">
				<tbody>
					<tr>
						<!-- SAML SSO -->
						<div
							style="border: solid 1px #ccc; background-color: #e3f2db; padding: 5px; margin-bottom: 10px;">
							<label><fmt:message
									key="application.list.application.applications" /></label>
						</div>
						<table class="styledLeft" width="100%" id="ServiceProviders">
							<thead>
								<tr style="white-space: nowrap">
									<th style="width: 100px"><fmt:message
											key="application.list.application.identifier" /></th>
									<th style="width: 100px"><fmt:message
											key="application.list.application.protocols" /></th>
									<th style="width: 100px"><fmt:message
											key="application.list.application.idps" /></th>
									<th style="width: 100px"><fmt:message
											key="application.list.application.action" /></th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>ebay</td>
									<td>SAML2,OIDC/OAuth</td>
									<td>FTIDP0, FTIDP1</td>
									<td style="width: 100px; white-space: nowrap;"><a
										title="Edit Service Providers" href="#" class="icon-link"
										style="background-image: url(../admin/images/edit.gif)">Edit</a>

										<a title="Remove Service Providers" href="#" class="icon-link"
										style="background-image: url(../admin/images/delete.gif)">Delete
									</a></td>
								</tr>
							</tbody>
						</table>
					</tr>
					<tr>
						<td class="buttonRow" style="padding-right: 0 !important; width: 30px;"><input
							name="update" type="button" class="button"
							value="<fmt:message key='application.list.application.done'/>"
							onclick="validate();" /></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</fmt:bundle>
