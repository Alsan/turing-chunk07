package org.wso2.carbon.identity.application.mgt.dto;

public class ApplicationConfigDTO {
	
	private String applicatIdentifier = null;
	private ClientConfigDTO[] clientConfig = null;
	private AuthenticationStepConfigDTO[] authenticationStep = null;
	
	
	public String getApplicatIdentifier() {
		return applicatIdentifier;
	}
	public void setApplicatIdentifier(String applicatIdentifier) {
		this.applicatIdentifier = applicatIdentifier;
	}
	public AuthenticationStepConfigDTO[] getAuthenticationSteps() {
		return authenticationStep;
	}
	public void setAuthenticationSteps(AuthenticationStepConfigDTO[] authenticationSteps) {
		this.authenticationStep = authenticationSteps;
	}
	public ClientConfigDTO[] getClientConfig() {
		return clientConfig;
	}
	public void setClientConfig(ClientConfigDTO[] clientConfig) {
		this.clientConfig = clientConfig;
	}

}
