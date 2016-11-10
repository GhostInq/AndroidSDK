package co.pixpie.api.android.auth;

public class AuthInfo {

    private final String authToken;
    private final String cdnUrl;
    private final boolean abTestingEnabled;

    public AuthInfo(String authToken, String cdnUrl, boolean abTestingEnabled) {
        this.authToken = authToken;
        this.cdnUrl = cdnUrl;
        this.abTestingEnabled = abTestingEnabled;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getCdnUrl() {
        return cdnUrl;
    }

    public boolean isAbTestingEnabled() {
        return abTestingEnabled;
    }
}
