package org.wso2.carbon.identity.application.mgt.ui;

public class OAuthOIDCAppConfig {
	
	private String clientID = null;
	private String clientSecret = null;
	private String callbackUrl = null;
	
	
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	

}
