package org.wso2.carbon.identity.sts.passive;

/**
 * Copyright (c) 2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ClaimDTO {
    private String realm;
    private String[] defaultClaims;
    private String claimDialect;

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String[] getDefaultClaims() {
        return defaultClaims;
    }

    public void setDefaultClaims(String[] defaultClaims) {
        this.defaultClaims = defaultClaims;
    }

    public String getClaimDialect() {
        return claimDialect;
    }

    public void setClaimDialect(String claimDialect) {
        this.claimDialect = claimDialect;
    }
}
