package org.wso2.carbon.identity.application.authentication.framework.config;

import java.util.Map;

public class ExternalIdPConfig {
	
	private String name;
	private Map<String, String> parameterMap;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, String> getParameterMap() {
		return parameterMap;
	}
	
	public void setParameterMap(Map<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}
}
