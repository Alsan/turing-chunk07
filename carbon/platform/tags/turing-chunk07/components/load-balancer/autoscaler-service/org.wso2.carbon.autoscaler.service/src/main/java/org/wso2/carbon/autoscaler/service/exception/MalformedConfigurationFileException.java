/*
*  Copyright (c) 2005-2011, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.autoscaler.service.exception;

public class MalformedConfigurationFileException extends RuntimeException {

    private static final long serialVersionUID = -1662095377704279326L;
    
    public MalformedConfigurationFileException(String msg) {
        super(msg);
    }
    
    public MalformedConfigurationFileException(String msg, Exception ex) {
        super(msg, ex);
    }

}
