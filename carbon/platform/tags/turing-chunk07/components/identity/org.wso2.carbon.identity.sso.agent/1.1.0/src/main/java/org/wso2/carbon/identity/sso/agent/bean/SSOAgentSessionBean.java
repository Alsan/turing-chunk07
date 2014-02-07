/*
*  Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.sso.agent.bean;

import org.openid4java.discovery.DiscoveryInformation;

import java.util.List;
import java.util.Map;

public class SSOAgentSessionBean {

    private String subjectId;

    private String idPSession;

    private DiscoveryInformation discoveryInformation;

    private String claimedId;

    private Map<String,List> openIdAttributes;

    private Map<String,String> samlSSOAttributes;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public Map<String, String> getSamlSSOAttributes() {
        return samlSSOAttributes;
    }

    public void setSamlSSOAttributes(Map<String, String> samlSSOAttributes) {
        this.samlSSOAttributes = samlSSOAttributes;
    }

    public String getIdPSession() {
        return idPSession;
    }

    public void setIdPSession(String idPSession) {
        this.idPSession = idPSession;
    }

    public DiscoveryInformation getDiscoveryInformation() {
        return discoveryInformation;
    }

    public void setDiscoveryInformation(DiscoveryInformation discoveryInformation) {
        this.discoveryInformation = discoveryInformation;
    }

    public String getClaimedId() {
        return claimedId;
    }

    public void setClaimedId(String claimedId) {
        this.claimedId = claimedId;
    }

    public Map<String, List> getOpenIdAttributes() {
        return openIdAttributes;
    }

    public void setOpenIdAttributes(Map<String, List> openIdAttributes) {
        this.openIdAttributes = openIdAttributes;
    }

}
