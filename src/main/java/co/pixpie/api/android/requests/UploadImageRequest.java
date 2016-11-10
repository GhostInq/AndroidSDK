package co.pixpie.api.android.requests;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Collections;
import java.util.Map;

import co.pixpie.api.android.PixpieApi;
import co.pixpie.api.android.auth.AuthInfo;
import co.pixpie.api.android.beans.ApplicationInfo;
import co.pixpie.api.android.beans.PixpieResult;

public class UploadImageRequest {

    private final String TAG = PixpieApi.class.getName();

    private byte[] image;
    private String contentType;
    private String encodedImageName;
    private String innerPath;
    private ApplicationInfo applicationInfo;
    private AuthInfo authInfo;

    public UploadImageRequest(@NonNull byte[] image, @NonNull String contentType,
                              @NonNull String encodedImageName, @NonNull String innerPath) {
        this.image = image;
        this.contentType = contentType;
        this.encodedImageName = encodedImageName;
        this.innerPath = innerPath;
    }

    public void executeInQueue(RequestQueue queue, ApplicationInfo applicationInfo, AuthInfo authInfo,
                               Response.Listener<PixpieResult> listener) {
        this.applicationInfo = applicationInfo;
        this.authInfo = authInfo;
        queue.add(buildRequest(listener));
    }

    private MultipartRequest buildRequest(final Response.Listener<PixpieResult> listener) {
        String url = buildImageUploadRequestUrl(innerPath, encodedImageName);
        Map<String, String> headers = Collections.singletonMap("pixpieAuthToken", authInfo.getAuthToken());
        return new MultipartRequest(url, headers, contentType, image,
            new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    listener.onResponse(new PixpieResult(true, null, null));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.onResponse(new PixpieResult(false, error, ""));
                }
        });
    }

    private String buildImageUploadRequestUrl(@NonNull String innerPath, @NonNull String imageName) {
        return new Uri.Builder()
                .scheme(applicationInfo.getServerScheme())
                .encodedAuthority(applicationInfo.getServerAddress())
                .path("async/images/upload")
                .appendPath(applicationInfo.getReverseUrlId())
                .appendPath(innerPath)
                .appendPath(imageName)
                .build()
                .toString();
    }
}
