package org.wso2.carbon.identity.application.mgt.dto;

public class TrustedIDPConfigDTO {

	private String idpIdentifier = null;
	private String[] types = null;
	private String[] endpoints = null;

	public String getIdpIdentifier() {
		return idpIdentifier;
	}

	public void setIdpIdentifier(String idpIdentifier) {
		this.idpIdentifier = idpIdentifier;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	public String[] getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(String[] endpoints) {
		this.endpoints = endpoints;
	}

	public String getTypesString() {
		if (types != null && types.length > 0) {
			StringBuffer buff = new StringBuffer();
			for (String type : types) {
				buff.append(type + ",");
			}
			return buff.toString();
		}
		return null;
	}
	
	public void setTypes(String typestring) {
		if(typestring != null) {
			types = typestring.split(",");
		}
	}
	
	public String getEndpointsString() {
		if (endpoints != null && endpoints.length > 0) {
			StringBuffer buff = new StringBuffer();
			for (String type : types) {
				buff.append(type + ",");
			}
			return buff.toString();
		}
		return null;
	}
	
	public void setEndpoints(String endpointString) {
		if(endpointString != null) {
			endpoints = endpointString.split(",");
		}
	}

}
