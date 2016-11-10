package co.pixpie.api.android.auth;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.pixpie.api.android.AuthenticationListener;
import co.pixpie.api.android.beans.ApplicationInfo;
import co.pixpie.api.android.exception.PixpieApiException;
import co.pixpie.api.android.requests.AuthRequest;

public class AuthManager {

    private AuthInfo authInfo;
    private boolean authenticationInProgress = false;

    private List<AuthInfoListener> authInfoListeners = new ArrayList<>();

    public AuthInfo getAuthInfo() {
        return authInfo;
    }

    public boolean isAuthenticated() {
        return authInfo != null;
    }

    public boolean isAuthenticationInProgress() {
        return authenticationInProgress;
    }

    public void addAuthInfoListener(AuthInfoListener listener) {
        if (authInfo != null) {
            listener.onAuthInfo(authInfo);
        } else {
            authInfoListeners.add(listener);
        }
    }

    public void authenticate(RequestQueue requestQueue, String userId, String deviceId, ApplicationInfo applicationInfo,
                             final AuthenticationListener listener) {
        // TODO: consider cancelling previous request and starting new
        if (authenticationInProgress) {
            return;
        }
        authenticationInProgress = true;

        new AuthRequest(
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleAuthResponse(response, listener);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleAuthError(error, listener);
                    }
                }).executeInQueue(userId, deviceId, requestQueue, applicationInfo);
    }

    private void handleAuthResponse(String response, AuthenticationListener listener) {
        authenticationInProgress = false;

        try {
            parseAuthResponse(response);
            processAuthInfoListeners();
            if (listener != null) {
                listener.onComplete();
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
            if (listener != null) {
                listener.onPixpieError(new PixpieApiException("Failed to parse server JSON response", ex));
            }
        }
    }

    private void handleAuthError(VolleyError error, AuthenticationListener listener) {
        authenticationInProgress = false;
        listener.onNetworkError(error);
    }

    private void parseAuthResponse(String response) throws JSONException {
        JSONObject jsonResponse = new JSONObject(response);

        String authToken = jsonResponse.getString("authToken");
        String cdnUrl = jsonResponse.getString("cdnUrl");
        boolean abTestingEnabled = Boolean.valueOf(jsonResponse.getString("abTestingEnabled"));

        authInfo = new AuthInfo(authToken, cdnUrl, abTestingEnabled);
    }

    private void processAuthInfoListeners() {
        for (AuthInfoListener listener : authInfoListeners) {
            listener.onAuthInfo(authInfo);
        }
        authInfoListeners.clear();
    }
}
