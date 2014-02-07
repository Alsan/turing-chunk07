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

public abstract class ApplicationAuthenticatorConstants {
	
	public static final int STATUS_AUTHENTICATION_PASS = 1;
	public static final int STATUS_AUTHENTICATION_FAIL = 0;
	public static final int STATUS_AUTHENTICATION_CANNOT_HANDLE = -1;
	
	public static final String DO_AUTHENTICATION = "doAuthentication";
	public static final String AUTHENTICATED = "commonAuthAuthenticated";
	public static final String AUTHENTICATED_USER = "authenticatedUser";
	public static final String SESSION_DATA_KEY = "sessionDataKey";
	public static final String CALLER_PATH = "commonAuthCallerPath";
	public static final String QUERY_PARAMS = "commonAuthQueryParams";
}
