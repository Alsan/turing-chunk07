package org.wso2.carbon.identity.application.mgt.ui;



public class ApplicationConfigBean {
	
	private String applicationIdentifier = "";
	private String publicCertificate = null;
	private SAMLSSOAppConfig samlssoConfig = null;
	private OAuthOIDCAppConfig oauthoidcConfig = null;
	private TrustedIDPConfig[] trustedIdpConfig = null;
	private String[] identityproviders = null;
	
	public ApplicationConfigBean() {
		String[] idps = new String[3];
		idps[0] = "IDP0";
		idps[1] = "IDP1";
		idps[2] = "IDP2";
		identityproviders = idps;
	}
	
	public String getApplicationIdentifier() {
		return applicationIdentifier;
	}
	public void setApplicationIdentifier(String applicationIdentifier) {
		this.applicationIdentifier = applicationIdentifier;
	}
	public String getPublicCertificate() {
		return publicCertificate;
	}
	public void setPublicCertificate(String publicCertificate) {
		this.publicCertificate = publicCertificate;
	}
	public SAMLSSOAppConfig getSamlssoConfig() {
		return samlssoConfig;
	}
	public void setSamlssoConfig(SAMLSSOAppConfig samlssoConfig) {
		this.samlssoConfig = samlssoConfig;
	}
	public OAuthOIDCAppConfig getOauthoidcConfig() {
		return oauthoidcConfig;
	}
	public void setOauthoidcConfig(OAuthOIDCAppConfig oauthoidcConfig) {
		this.oauthoidcConfig = oauthoidcConfig;
	}
	public TrustedIDPConfig[] getTrustedIdpConfig() {
		return trustedIdpConfig;
	}
	public void setTrustedIdpConfig(TrustedIDPConfig[] trustedIdpConfig) {
		this.trustedIdpConfig = trustedIdpConfig;
	}
	public String[] getIdentityproviders() {
		return identityproviders;
	}
	public void setIdentityproviders(String[] identityproviders) {
		this.identityproviders = identityproviders;
	}
	public void addTrustedIDPConfig(TrustedIDPConfig idpConfig) {
		if(trustedIdpConfig == null) {
			trustedIdpConfig = new TrustedIDPConfig[1];
			trustedIdpConfig[0] = idpConfig;
		} else {
			TrustedIDPConfig[] newArray = new TrustedIDPConfig[trustedIdpConfig.length + 1];
			System.arraycopy(trustedIdpConfig, 0, newArray, 0, trustedIdpConfig.length);
			newArray[trustedIdpConfig.length] = idpConfig;
			trustedIdpConfig = newArray;
		}
	}
	
}
