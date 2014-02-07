package org.wso2.carbon.identity.application.mgt.dto;

public class AuthenticationStepConfigDTO {
	
	private AuthenticatorConfigDTO[] authenticators = null;
	private String stepIdentifier = null;

	public AuthenticatorConfigDTO[] getAuthenticators() {
		return authenticators;
	}

	public void setAuthenticators(AuthenticatorConfigDTO[] authenticators) {
		this.authenticators = authenticators;
	}

	public String getStepIdentifier() {
		return stepIdentifier;
	}

	public void setStepIdentifier(String stepIdentifier) {
		this.stepIdentifier = stepIdentifier;
	}

}
