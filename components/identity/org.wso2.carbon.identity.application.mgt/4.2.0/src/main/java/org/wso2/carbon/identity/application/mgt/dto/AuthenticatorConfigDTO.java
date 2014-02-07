package org.wso2.carbon.identity.application.mgt.dto;

public class AuthenticatorConfigDTO {
	
	private TrustedIDPConfigDTO[] idps = null;
	private String authnticatorIdentifier = null;

	public TrustedIDPConfigDTO[] getIdps() {
		return idps;
	}

	public void setIdps(TrustedIDPConfigDTO[] idps) {
		this.idps = idps;
	}

	public String getAuthnticatorIdentifier() {
		return authnticatorIdentifier;
	}

	public void setAuthnticatorIdentifier(String authnticatorIdentifier) {
		this.authnticatorIdentifier = authnticatorIdentifier;
	}

}
