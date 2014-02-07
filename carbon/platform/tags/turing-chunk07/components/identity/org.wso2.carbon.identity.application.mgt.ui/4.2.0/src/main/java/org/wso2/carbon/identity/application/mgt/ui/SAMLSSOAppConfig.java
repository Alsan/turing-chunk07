package org.wso2.carbon.identity.application.mgt.ui;

public class SAMLSSOAppConfig {

	private String issuer = null;
	private String consumerIndex = null;
	private String acsUrl = null;

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String consumerKey) {
		this.issuer = consumerKey;
	}

	public String getConsumerIndex() {
		return consumerIndex;
	}

	public void setConsumerIndex(String consumerIndex) {
		this.consumerIndex = consumerIndex;
	}

	public String getAcsUrl() {
		return acsUrl;
	}

	public void setAcsUrl(String acsUrl) {
		this.acsUrl = acsUrl;
	}

}
