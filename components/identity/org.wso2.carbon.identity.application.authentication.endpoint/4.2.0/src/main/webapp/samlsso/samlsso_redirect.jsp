<!DOCTYPE html>
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
<%@page import="java.net.URLDecoder" %>
<%@page import="org.wso2.carbon.identity.application.authentication.endpoint.samlsso.SAMLSSOConstants" %>
<html>
<head></head>
<body>
<%
    String assertionConsumerURL = (String) request.getAttribute(SAMLSSOConstants.ASSRTN_CONSUMER_URL);
    String samlResp = (String) request.getAttribute(SAMLSSOConstants.SAML_RESP);
    String relayState = (String) request.getAttribute(SAMLSSOConstants.RELAY_STATE);
	
	if(relayState != null){
	    relayState = URLDecoder.decode(relayState, "UTF-8");
	    relayState = relayState.replaceAll("&", "&amp;").replaceAll("\"", "&quot;").replaceAll("'", "&apos;").
	            replaceAll("<", "&lt;").replaceAll(">", "&gt;").replace("\n", "");
	}
%>
<p>You are now redirected back to <%=assertionConsumerURL%>. If the
 redirection fails, please click the post button.</p>
<form method="post" action="<%=assertionConsumerURL%>">
    <p><input type="hidden" name="SAMLResponse" value="<%=samlResp%>"/>
        <input type="hidden" name="RelayState" value="<%=relayState%>"/>
        <button type="submit">POST</button>
    </p>
</form>

<script type="text/javascript">
    document.forms[0].submit();
</script>

</body>
</html>