package co.pixpie.api.android.requests;

import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import co.pixpie.api.android.PixpieApi;
import co.pixpie.api.android.beans.ApplicationInfo;
import co.pixpie.api.android.utils.Utils;

public class AuthRequest {

    private final String TAG = PixpieApi.class.getName();

    private final Response.Listener<String> responseListener;
    private final Response.ErrorListener errorListener;

    public AuthRequest(Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        this.responseListener = responseListener;
        this.errorListener = errorListener;
    }

    public void executeInQueue(final String userUniqueId, final String deviceUniqueId, RequestQueue queue,
                               final ApplicationInfo applicationInfo) {
        StringRequest request = new StringRequest(Request.Method.POST, buildUrl(applicationInfo),
                responseListener, errorListener){
            @Override
            protected Map<String,String> getParams() {
                Map<String, String> params = new HashMap<>();

                Long timestampLong = System.currentTimeMillis() / 1000;
                String toHash = applicationInfo.getSecretKey() + applicationInfo.getSalt() + String.valueOf(timestampLong);

                MessageDigest digest;
                try {
                    digest = MessageDigest.getInstance("SHA-256");
                } catch (NoSuchAlgorithmException e) {
                    Log.e(TAG, "Failed to calculate hash", e);
                    return params;
                }

                digest.update(toHash.getBytes());
                String hashed = Utils.bin2hex(digest.digest());

                params.put("reverseUrlId", applicationInfo.getReverseUrlId());
                params.put("timestamp", String.valueOf(timestampLong));
                params.put("hash", hashed);
                params.put("clientSdkType", "2"); // Android SDK id
                params.put("userUniqueId", userUniqueId);
                params.put("deviceUniqueId", deviceUniqueId);
                params.put("deviceDescription", applicationInfo.getDeviceDescription());
                params.put("sdkVersion", applicationInfo.getSdkVersion());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Collections.singletonMap("Content-Type", "application/x-www-form-urlencoded");
            }
        };
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(retryPolicy);
        queue.add(request);
    }

    private String buildUrl(ApplicationInfo applicationInfo) {
        return new Uri.Builder()
                .scheme(applicationInfo.getServerScheme())
                .encodedAuthority(applicationInfo.getServerAddress())
                .path("authentication/token/client_sdk")
                .build()
                .toString();
    }
}
