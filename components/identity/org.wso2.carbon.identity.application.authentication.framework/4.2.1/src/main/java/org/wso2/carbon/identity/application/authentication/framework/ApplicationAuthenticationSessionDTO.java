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

public class ApplicationAuthenticationSessionDTO {

	private String callerPath;
	private String callerSessionKey;
	private String queryParams;
	private String requestType;

	public String getCallerPath() {
		return callerPath;
	}

	public void setCallerPath(String callerPath) {
		this.callerPath = callerPath;
	}

	public String getCallerSessionKey() {
		return callerSessionKey;
	}

	public void setCallerSessionKey(String callerSessionKey) {
		this.callerSessionKey = callerSessionKey;
	}

	public String getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(String queryParams) {
		this.queryParams = queryParams;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
}
