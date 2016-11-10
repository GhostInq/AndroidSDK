package co.pixpie.api.android.requests;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Collections;
import java.util.Map;

import co.pixpie.api.android.PixpieApi;
import co.pixpie.api.android.auth.AuthInfo;
import co.pixpie.api.android.beans.ApplicationInfo;
import co.pixpie.api.android.beans.PixpieResult;
import co.pixpie.api.android.utils.ServerStatusRequestObject;

public class DeleteImageRequest {

    private final String TAG = PixpieApi.class.getName();
    private boolean deleteImageResult;
    private boolean deleteImageExecuted;

    private String imageName;
    private final String pathToImage;

    private Response.Listener<PixpieResult> listener;

    public DeleteImageRequest(@NonNull String imageName, @NonNull String pathToImage) {
        this.imageName = imageName;
        this.pathToImage = pathToImage;
    }

    public void executeInQueue(RequestQueue queue, ApplicationInfo applicationInfo, final AuthInfo authInfo,
                               final Response.Listener<PixpieResult> listener) {
        queue.add(new ServerStatusRequestObject(Request.Method.DELETE, buildUrl(applicationInfo,
                pathToImage, imageName), null, null,
                new Response.Listener<Integer>() {
                    @Override
                    public void onResponse(Integer response) {
                        if (response >= 200 && response < 300) {
                            listener.onResponse(new PixpieResult(true, null, null));
                        }
                        else {
                            listener.onResponse(new PixpieResult(false, null, "Response status code: " + response));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onResponse(new PixpieResult(false, error, ""));
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return Collections.singletonMap("pixpieAuthToken", authInfo.getAuthToken());
            }
        });
    }

    private String buildUrl(ApplicationInfo applicationInfo, String pathToImage, String imageName) {
        return new Uri.Builder()
                .scheme(applicationInfo.getServerScheme())
                .encodedAuthority(applicationInfo.getServerAddress())
                .path("images/delete")
                .appendPath(applicationInfo.getReverseUrlId())
                .appendPath(pathToImage)
                .appendPath(imageName)
                .build()
                .toString();
    }
}
