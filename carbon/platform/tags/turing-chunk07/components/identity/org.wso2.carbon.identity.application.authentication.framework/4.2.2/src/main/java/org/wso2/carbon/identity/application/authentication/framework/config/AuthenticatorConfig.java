/*
*  Copyright (c) 2005-2013, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.carbon.identity.application.authentication.framework.config;

import java.util.List;
import java.util.Map;

import org.wso2.carbon.identity.application.authentication.framework.ApplicationAuthenticator;

public class AuthenticatorConfig {

    private String name;
    private boolean enabled;
    private ApplicationAuthenticator applicationAuthenticator;
    private Map<String, String> parameterMap;
    private List<ExternalIdPConfig> idpList;

	public AuthenticatorConfig(String name, boolean enabled, 
	                            Map<String, String> parameterMap) {
		this.name = name;
		this.enabled = enabled;
		this.parameterMap = parameterMap;
	}

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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<ExternalIdPConfig> getIdpList() {
		return idpList;
	}

	public void setIdpList(List<ExternalIdPConfig> idpList) {
		this.idpList = idpList;
	}

	public ApplicationAuthenticator getApplicationAuthenticator() {
		return applicationAuthenticator;
	}

	public void setApplicationAuthenticator(
			ApplicationAuthenticator applicationAuthenticator) {
		this.applicationAuthenticator = applicationAuthenticator;
	}
}