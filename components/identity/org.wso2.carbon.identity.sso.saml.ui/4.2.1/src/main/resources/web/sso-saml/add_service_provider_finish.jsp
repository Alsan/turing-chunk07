<!--
~ Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
<%@page import="org.apache.axis2.context.ConfigurationContext"%>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.wso2.carbon.identity.sso.saml.stub.types.SAMLSSOServiceProviderDTO" %>
<%@ page import="org.wso2.carbon.identity.sso.saml.ui.SAMLSSOUIConstants" %>
<%@ page import="org.wso2.carbon.identity.sso.saml.ui.client.SAMLSSOConfigServiceClient" %>
<%@page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%@page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="org.wso2.carbon.identity.sso.saml.ui.SAMLSSOUIUtil" %>
<%@ page import="org.wso2.carbon.identity.application.mgt.ui.ApplicationConfigBean"%>
<%@ page import="org.wso2.carbon.identity.application.mgt.ui.SAMLSSOAppConfig"%>

<jsp:useBean id="samlSsoServuceProviderConfigBean"
             type="org.wso2.carbon.identity.sso.saml.ui.SAMLSSOProviderConfigBean"
             class="org.wso2.carbon.identity.sso.saml.ui.SAMLSSOProviderConfigBean"
             scope="session"/>
<jsp:setProperty name="samlSsoServuceProviderConfigBean" property="*"/>

<jsp:useBean id="appBean" class="org.wso2.carbon.identity.application.mgt.ui.ApplicationConfigBean" scope="session"/>

<script type="text/javascript" src="global-params.js"></script>
<script type="text/javascript" src="../carbon/admin/js/breadcrumbs.js"></script>
<script type="text/javascript" src="../carbon/admin/js/cookies.js"></script>
<script type="text/javascript" src="../carbon/admin/js/main.js"></script>


<%
	String backendServerURL;
    ConfigurationContext configContext;
    String cookie;
    String user = null;
    SAMLSSOConfigServiceClient client;
    session.setAttribute(SAMLSSOUIConstants.CONFIG_CLIENT, null);

    backendServerURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
    configContext = (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
    cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);
    String BUNDLE = "org.wso2.carbon.identity.sso.saml.ui.i18n.Resources";
    ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE, request.getLocale());

    try {
        client = new SAMLSSOConfigServiceClient(cookie, backendServerURL, configContext);

        SAMLSSOServiceProviderDTO serviceProviderDTO = new SAMLSSOServiceProviderDTO();
        boolean isEditingSP = false; 
        if ("editServiceProvider".equals(SAMLSSOUIUtil.getSafeInput(request, "SPAction"))) {
            isEditingSP = true;
            serviceProviderDTO.setIssuer(SAMLSSOUIUtil.getSafeInput(request, "hiddenIssuer"));
        } else {
            serviceProviderDTO.setIssuer(SAMLSSOUIUtil.getSafeInput(request, "issuer"));
        }

        serviceProviderDTO.setAssertionConsumerUrl(SAMLSSOUIUtil.getSafeInput(request, "assrtConsumerURL"));
        serviceProviderDTO.setCertAlias(SAMLSSOUIUtil.getSafeInput(request, "alias"));

        if ("true".equals(request.getParameter("useFullQualifiedUsername"))) {
            serviceProviderDTO.setUseFullyQualifiedUsername(true);
        }
        if ("true".equals(request.getParameter("enableSingleLogout"))) {
            serviceProviderDTO.setDoSingleLogout(true);
        }
        if ("true".equals(request.getParameter("enableResponseSignature"))) {
            serviceProviderDTO.setDoSignResponse(true);
        }
        if ("true".equals(request.getParameter("enableAssertionSignature"))) {
            serviceProviderDTO.setDoSignAssertions(true);
        }
        
        serviceProviderDTO.setNameIDFormat(request.getParameter("nameIdFormat"));
        
        if (serviceProviderDTO.getNameIDFormat()!=null) {
        	serviceProviderDTO.setNameIDFormat(serviceProviderDTO.getNameIDFormat().replace(":", "/"));
        }

        if ("true".equals(request.getParameter("enableAttributeProfile"))) {
            serviceProviderDTO.setRequestedClaims(samlSsoServuceProviderConfigBean.getSelectedClaimsAttay());
            
            if ("true".equals(request.getParameter("enableDefaultAttributeProfileHidden"))) {
                serviceProviderDTO.setEnableAttributesByDefault(true);
            }
        }
        
        if ("true".equals(request.getParameter("enableNameIdClaimUriHidden"))) {            
                serviceProviderDTO.setNameIdClaimUri(request.getParameter("nameIdClaim"));
        }
               
               
        if ("true".equals(request.getParameter("enableAudienceRestriction"))) {
            serviceProviderDTO.setRequestedAudiences(samlSsoServuceProviderConfigBean.getSelectedAudiencesArray());
        }
        
        if (request.getParameter("logoutURL")!=null && !"null".equals(request.getParameter("logoutURL"))) {
            serviceProviderDTO.setLogoutURL(request.getParameter("logoutURL"));
        }
        
        if (request.getParameter("loginPageURL")!=null && !"null".equals(request.getParameter("loginPageURL"))) {
            serviceProviderDTO.setLoginPageURL(request.getParameter("loginPageURL"));
        }

        if ("true".equals(request.getParameter("enableAttributeProfile"))) {

        String claimsCountParameter = SAMLSSOUIUtil.getSafeInput(request, "claimPropertyCounter");
        if (claimsCountParameter != null && !"".equals(claimsCountParameter)) {
            try {
                int claimsCount = Integer.parseInt(claimsCountParameter);
                for (int i = 0; i < claimsCount; i++) {
                    String claim = SAMLSSOUIUtil.getSafeInput(request, "claimPropertyName" + i);
                    if (claim != null && !"".equals(claim) && !"null".equals(claim)) {
                        String[] currentClaims = serviceProviderDTO.getRequestedClaims();
                        boolean isClaimAlreadyAdded = false;
                        for (String currentClaim : currentClaims) {
                            if (claim.equals(currentClaim)) {
                                isClaimAlreadyAdded = true;
                                break;
                            }
                        }

                        if (!isClaimAlreadyAdded) {
                            serviceProviderDTO.addRequestedClaims(claim);
                        }
                    }
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid number", e);
            }
        }
        }

        if ("true".equals(request.getParameter("enableAudienceRestriction"))) {

        	String audiencesCountParameter = SAMLSSOUIUtil.getSafeInput(request, "audiencePropertyCounter");
        	if (audiencesCountParameter != null && !"".equals(audiencesCountParameter)) {
            	try {
                	int audiencesCount = Integer.parseInt(audiencesCountParameter);
                	for (int i = 0; i < audiencesCount; i++) {
                    	String audience = SAMLSSOUIUtil.getSafeInput(request, "audiencePropertyName" + i);
                    	if (audience != null && !"".equals(audience) && !"null".equals(audience)) {
                        	String[] currentAudiences = serviceProviderDTO.getRequestedAudiences();
                        	boolean isAudienceAlreadyAdded = false;
                        	for (String currentAudience : currentAudiences) {
                            	if (audience.equals(currentAudience)) {
                               		isAudienceAlreadyAdded = true;
                                	break;
                            	}
                        	}

                        	if (!isAudienceAlreadyAdded) {
                            	serviceProviderDTO.addRequestedAudiences(audience);
                        	}
                    	}
                	}
            	} catch (NumberFormatException e) {
                	throw new RuntimeException("Invalid number", e);
            	}
        	}
        }

        serviceProviderDTO.setAttributeConsumingServiceIndex(SAMLSSOUIUtil.getSafeInput(request, "attributeConsumingServiceIndex"));

        if ("true".equals(request.getParameter("enableIdPInitSSO"))) {
            serviceProviderDTO.setIdPInitSSOEnabled(true);
        }

        if (isEditingSP) {
            client.removeServiceProvier(serviceProviderDTO.getIssuer());
        }
        boolean status = client.addServiceProvider(serviceProviderDTO);

        samlSsoServuceProviderConfigBean.clearBean();

        String message;
        if (status) {
        	// setting app bean
        	SAMLSSOServiceProviderDTO dto = client.getServiceProvider(serviceProviderDTO.getIssuer());
        	if (dto != null) {
				SAMLSSOAppConfig ssoConfig = new SAMLSSOAppConfig();
				ssoConfig.setIssuer(dto.getIssuer());
				ssoConfig.setConsumerIndex(dto.getAttributeConsumingServiceIndex());
				ssoConfig.setAcsUrl(dto.getAssertionConsumerUrl());
				appBean.setSamlssoConfig(ssoConfig);
			}

			if (isEditingSP) {
				message = resourceBundle.getString("sp.updated.successfully");
			} else {
				message = resourceBundle.getString("sp.added.successfully");
			}
		} else {
			message = resourceBundle.getString("error.adding.sp");
		}

		if (status) {
			CarbonUIMessage.sendCarbonUIMessage(message, CarbonUIMessage.INFO, request);
		} else {
			CarbonUIMessage.sendCarbonUIMessage(message, CarbonUIMessage.ERROR, request);
		}
%>
<script>
    location.href = '../application/create-app.jsp';
</script>
<%

} catch (Exception e) {
    CarbonUIMessage.sendCarbonUIMessage(e.getMessage(), CarbonUIMessage.ERROR, request, e);
%>
<script type="text/javascript">
    location.href = "../admin/error.jsp";
</script>
<%
        return;
    }
%>