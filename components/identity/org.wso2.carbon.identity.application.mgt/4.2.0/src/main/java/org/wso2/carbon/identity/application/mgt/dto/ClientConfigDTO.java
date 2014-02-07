package org.wso2.carbon.identity.application.mgt.dto;

public class ClientConfigDTO {
	
	private String clientID = null;
	private String clientSecrete = null;
	private String callbackUrl = null;
	private String type = null;
	
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public String getClientSecrete() {
		return clientSecrete;
	}
	public void setClientSecrete(String clientSecrete) {
		this.clientSecrete = clientSecrete;
	}
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	

}
