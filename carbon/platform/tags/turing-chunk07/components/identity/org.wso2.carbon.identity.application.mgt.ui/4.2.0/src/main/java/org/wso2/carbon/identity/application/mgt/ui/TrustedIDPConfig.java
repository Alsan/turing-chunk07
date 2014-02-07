package org.wso2.carbon.identity.application.mgt.ui;

public class TrustedIDPConfig {

	private String idpName = null;
	private String[] protocols = null;
	private String endpointUrl = null;

	public String getIdpName() {
		return idpName;
	}

	public void setIdpName(String idpName) {
		this.idpName = idpName;
	}

	public String[] getProtocols() {
		return protocols;
	}

	public void setProtocols(String[] protocols) {
		this.protocols = protocols;
	}

	public String getEndpointUrl() {
		return endpointUrl;
	}

	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	public String getProtocolsString() {
		if (protocols != null && protocols.length > 0) {
			StringBuffer protocolsBuff = new StringBuffer();
			for (String protocol : protocols) {
				protocolsBuff.append(protocol + " ");
			}
			return protocolsBuff.toString();
		} else {
			return "";
		}
	}

}
