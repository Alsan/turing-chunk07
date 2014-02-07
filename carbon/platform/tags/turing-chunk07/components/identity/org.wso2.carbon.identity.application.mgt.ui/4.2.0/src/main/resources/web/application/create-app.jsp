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

<%@page import="org.wso2.carbon.identity.application.mgt.ui.ApplicationConfigBean"%>
<%@ page import="org.apache.axis2.context.ConfigurationContext"%>
<%@ page import="org.wso2.carbon.CarbonConstants"%>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage"%>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil"%>
<%@ page import="org.wso2.carbon.utils.ServerConstants"%>
<%@ page import="org.wso2.carbon.identity.application.mgt.ui.ApplicationConfigBean"%>
<%@ page import="org.wso2.carbon.identity.application.mgt.ui.TrustedIDPConfig"%>

<%@ page import="java.util.ResourceBundle"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar"
	prefix="carbon"%>
	
<jsp:useBean id="appBean" class="org.wso2.carbon.identity.application.mgt.ui.ApplicationConfigBean" scope="session"/>

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
	
	<script type="text/javascript">
		function onSamlSsoClick() {
			var appid = document.getElementById("application_identifier").value;
			if( appid != '') {
				document.getElementById("saml_link").href="../sso-saml/add_service_provider.jsp?appid="+appid;
			} else {
				CARBON.showWarningDialog('<fmt:message key="application.create.alert.defineappid"/>');
				document.getElementById("saml_link").href="#"
			}
		}
		
		function onOauthClick() {
			var appid = document.getElementById("application_identifier").value;
			if( appid != '') {
				document.getElementById("oauth_link").href="../oauth/add.jsp?appid=" + appid;
			} else {
				CARBON.showWarningDialog('<fmt:message key="application.create.alert.defineappid"/>');
				document.getElementById("oauth_link").href="#"
			}
		}
		
		function createAppOnclick() {
			var appid = document.getElementById("application_identifier").value;
			if( appid == '') {
				CARBON.showWarningDialog('<fmt:message key="application.create.alert.defineappid"/>');
				location.href = '#';
			} else {
				location.href='add-app.jsp?appid=' + appid;
			}
		}
		
		function onClickAddIDP() {
			var appid = document.getElementById("application_identifier").value;
			if( appid == '') {
				CARBON.showWarningDialog('<fmt:message key="application.create.alert.defineappid"/>');
				location.href = '#';
			} else {
				document.getElementById("add_idp_form").submit();
			}
		}
		
	</script>

	<div id="middle">

		<h2>
			<fmt:message key='application.create.application' />
		</h2>

		<div id="workArea">

			<table style="width: 100%" class="styledLeft">
				<tbody>
					<tr>

						<!-- Basic Info -->
						<div
							style="border: solid 1px #ccc; background-color: #e3f2db; padding: 5px; margin-bottom: 10px;">
							<label><fmt:message key="application.info.basic" /></label>
						</div>
						<table class="styledLeft noBorders">
							<tbody>
								<tr>
									<td class="leftCol-big" style="padding-right: 0 !important;"><fmt:message
											key="application.info.basic.identifier" /><font color="red">*</font></td>
									<td><input type="text" name="application_identifier"
										id="application_identifier" value="<%=appBean.getApplicationIdentifier()%>" /></td>
								</tr>
							</tbody>
						</table>

						<p>&nbsp;</p>
						<!-- SAML SSO -->
						<%
						if(appBean.getSamlssoConfig() != null) {
						%>
						<div
							style="border: solid 1px #ccc; background-color: #e3f2db; padding: 5px; margin-bottom: 10px;">
							<label><fmt:message key="application.info.saml2sso" /></label>
						</div>
						<% } %>
						<!-- If no SSO config found, then this -->
						<%
						if(appBean.getSamlssoConfig() == null) {
						%>
						<div>
							<a id="saml_link" href="javascript:document.location.href='sj'"
								class="icon-link" onclick="onSamlSsoClick()"
								style="background-image: url(../admin/images/add.gif);"><fmt:message
									key='application.info.saml2sso.register' /></a>
						</div>
						<%
						} else {
						%>
						<!-- Else this -->
						<table class="styledLeft" width="100%" id="ServiceProviders">
							<thead>
								<tr style="white-space: nowrap">
									<th style="width: 100px"><fmt:message
											key="application.info.saml2sso.issuer" /></th>
									<th style="width: 100px"><fmt:message
											key="application.info.saml2sso.consumerindex" /></th>
									<th style="width: 100px"><fmt:message
											key="application.info.saml2sso.acsurl" /></th>
									<th style="width: 100px"><fmt:message
											key="application.info.saml2sso.action" /></th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td><%=appBean.getSamlssoConfig().getIssuer()%></td>
									<td><%=appBean.getSamlssoConfig().getConsumerIndex()%></td>
									<td><%=appBean.getSamlssoConfig().getAcsUrl() %></td>
									<td style="width: 100px; white-space: nowrap;"><a
										title="Edit Service Providers" href="#" class="icon-link"
										style="background-image: url(../admin/images/edit.gif)">Edit</a>

										<a title="Remove Service Providers" href="#" class="icon-link"
										style="background-image: url(../admin/images/delete.gif)">Delete
									</a></td>
								</tr>
							</tbody>
						</table>
						
						<% } %>

						<p>&nbsp;</p>
						<!-- OAuth -->
						<%
						if(appBean.getOauthoidcConfig() != null) {
						%>
						<div
							style="border: solid 1px #ccc; background-color: #e3f2db; padding: 5px; margin-bottom: 10px;">
							<label><fmt:message key="application.info.oauthoidc" /></label>
						</div>
						<% } %>
						<!-- If no OAuth config found, then this -->
						<%
						if(appBean.getOauthoidcConfig() == null) {
						%>
						<div>
							<a id="oauth_link" href="javascript:document.location.href='sj'"
								class="icon-link" onclick="onOauthClick()"
								style="background-image: url(../admin/images/add.gif);"><fmt:message
									key='application.info.oauthoidc.register' /></a>
						</div>
						<!-- Else this -->
						<%
						} else {
						%>
						<table class="styledLeft" width="100%" id="OAuthOIDC">
							<thead>
								<tr style="white-space: nowrap">
									<th style="width: 100px"><fmt:message
											key="application.info.oauthoidc.clientid" /></th>
									<th style="width: 100px"><fmt:message
											key="application.info.oauthoidc.clientsecret" /></th>
									<th style="width: 100px"><fmt:message
											key="application.info.oauthoidc.callbackURL" /></th>
									<th style="width: 100px"><fmt:message
											key="application.info.oauthoidc.action" /></th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td><%=appBean.getOauthoidcConfig().getClientID() %></td>
									<td><%=appBean.getOauthoidcConfig().getClientSecret() %></td>
									<td><%=appBean.getOauthoidcConfig().getCallbackUrl() %></td>
									<td style="width: 100px; white-space: nowrap;"><a
										title="Edit Service Providers" href="#" class="icon-link"
										style="background-image: url(../admin/images/edit.gif)">Edit</a>

										<a title="Remove Service Providers" href="#" class="icon-link"
										style="background-image: url(../admin/images/delete.gif)">Delete
									</a></td>
								</tr>
							</tbody>
						</table>
						<% } %>
						<p>&nbsp;</p>
						<!-- Trusted IDPs -->
						<% if(appBean.getIdentityproviders() != null && appBean.getIdentityproviders().length > 0) { 
						
								String[] idps = appBean.getIdentityproviders();							
						%>
						<div
							style="border: solid 1px #ccc; background-color: #e3f2db; padding: 5px; margin-bottom: 10px;">
							<label><fmt:message key="application.info.trustedidps" /></label>
						</div>
						<table class="styledLeft noBorders">
							<tbody>
							<form method="POST" action="add-idp.jsp" id="add_idp_form">
								<tr>
									<td class="leftCol-big" style="padding-right: 0 !important;">
									<select id="idpselect" name="idpselect">
									<% for(String idp : idps) {%>
											<option value="<%=idp%>"><%=idp%></option>
									<% } %>
									</select></td>
									<td><input type="submit" name="application_identifier"
										style="padding-right: 0 !important;"
										id="application_identifier" value="Add To Trust" onclick="onClickAddIDP();"/></td>
								</tr>
								</form>
							</tbody>
						</table>

						<p>&nbsp;</p>
						
						<% } %>
						
						<% if(appBean.getTrustedIdpConfig() != null) { 
							TrustedIDPConfig[] idpConfigs = appBean.getTrustedIdpConfig();
						%>
						<table class="styledLeft" width="100%" id="OAuthOIDC">
							<thead>
								<tr style="white-space: nowrap">
									<th style="width: 100px"><fmt:message
											key="application.info.trustedidp.idp" /></th>
									<th style="width: 100px"><fmt:message
											key="application.info.trustedidp.type" /></th>
									<th style="width: 100px"><fmt:message
											key="application.info.trustedidp.endpoint" /></th>
									<th style="width: 100px"><fmt:message
											key="application.info.trustedidp.action" /></th>
								</tr>
							</thead>
							<tbody>
							<%for(TrustedIDPConfig idpConfig : idpConfigs) {%>
								<tr>
									<td><%=idpConfig.getIdpName() %></td>
									<td><%=idpConfig.getProtocolsString() %></td>
									<td><%=idpConfig.getEndpointUrl() %></td>
									<td style="width: 100px; white-space: nowrap;"><a
										title="Edit Service Providers" href="#" class="icon-link"
										style="background-image: url(../admin/images/edit.gif)">Edit</a>

										<a title="Remove Service Providers" href="#" class="icon-link"
										style="background-image: url(../admin/images/delete.gif)">Delete
									</a></td>
								</tr>
							<% }%>
							</tbody>
						</table>
						<p>&nbsp;</p>
						<% } %>
					</tr>
					<tr>
						<td class="buttonRow" style="padding-right: 0 !important;"><input
							name="update" type="button" class="button"
							value="<fmt:message key='application.create.createapp'/>"
							onclick="createAppOnclick();" /> 
							<input type="button" class="button"
							onclick="javascript:location.href='add-app.jsp'"
							value="<fmt:message key='application.create.cancel'/>" /></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</fmt:bundle>
