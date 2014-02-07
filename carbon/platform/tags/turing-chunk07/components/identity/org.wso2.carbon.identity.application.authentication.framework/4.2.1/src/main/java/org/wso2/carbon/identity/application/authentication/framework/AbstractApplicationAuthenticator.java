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

package org.wso2.carbon.identity.application.authentication.framework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractApplicationAuthenticator implements ApplicationAuthenticator {

	@Override
    public abstract int doAuthentication(HttpServletRequest request, HttpServletResponse response);

	@Override
	public boolean isDisabled() {
		if (getAuthenticatorConfig() != null){
			return getAuthenticatorConfig().isDisabled();
		}
		return true;
	}

	@Override
    public int getFactor() {
		if (getAuthenticatorConfig() != null){
			return getAuthenticatorConfig().getFactor();
		}
		return -1;
    }
	
	@Override
    public int getStatus(HttpServletRequest request) {
		if (getAuthenticatorConfig() != null && getAuthenticatorConfig().getStatusMap() != null){
			return Integer.valueOf(getAuthenticatorConfig().getStatusMap().entrySet().iterator().next().getKey());
		}
		return -1;
	}
	
	protected ApplicationAuthenticatorsConfiguration.AuthenticatorConfig getAuthenticatorConfig() {
		return ApplicationAuthenticatorsConfiguration.getInstance().getAuthenticatorConfig(getAuthenticatorName());
    }
	
	protected boolean isSingleFactorMode(){
		return ApplicationAuthenticatorsConfiguration.getInstance().isSingleFactor();
	}
}