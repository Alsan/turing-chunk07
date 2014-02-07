package org.wso2.carbon.cassandra.common.cache;


import java.io.Serializable;

public class UserAccessKeyCache implements Serializable {

    private String accessKey;

    public UserAccessKeyCache(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessKey() {
        return accessKey;
    }
}
