package org.wso2.carbon.identity.oauth.cache;

import org.wso2.carbon.identity.oauth2.model.OAuth2Parameters;

public class SessionDataCacheEntry  extends CacheEntry{

    private static final long serialVersionUID = -4123547630178387354L;

    private OAuth2Parameters oAuth2Parameters;

    private String consent;

    String oidcRequest;

    String oidcResponse;

    String oidcRP;

    public OAuth2Parameters getoAuth2Parameters() {
        return oAuth2Parameters;
    }

    public void setoAuth2Parameters(OAuth2Parameters oAuth2Parameters) {
        this.oAuth2Parameters = oAuth2Parameters;
    }

    public String getConsent() {
        return consent;
    }

    public void setConsent(String consent) {
        this.consent = consent;
    }

    public String getOidcRequest() {
        return oidcRequest;
    }

    public void setOidcRequest(String oidcRequest) {
        this.oidcRequest = oidcRequest;
    }

    public String getOidcResponse() {
        return oidcResponse;
    }

    public void setOidcResponse(String oidcResponse) {
        this.oidcResponse = oidcResponse;
    }

    public String getOidcRP() {
        return oidcRP;
    }

    public void setOidcRP(String oidcRP) {
        this.oidcRP = oidcRP;
    }

    public String getOidcLoggedInUser() {
        return oidcLoggedInUser;
    }

    public void setOidcLoggedInUser(String oidcLoggedInUser) {
        this.oidcLoggedInUser = oidcLoggedInUser;
    }

    String oidcLoggedInUser;

}
