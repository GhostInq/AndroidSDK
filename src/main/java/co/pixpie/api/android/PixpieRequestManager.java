package co.pixpie.api.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.apache.commons.lang3.Validate;

import co.pixpie.api.android.auth.AuthInfo;
import co.pixpie.api.android.auth.AuthInfoListener;
import co.pixpie.api.android.auth.AuthManager;
import co.pixpie.api.android.beans.ApplicationInfo;
import co.pixpie.api.android.beans.PixpieResult;
import co.pixpie.api.android.exception.SDKDisabledException;
import co.pixpie.api.android.image.CropAlignType;
import co.pixpie.api.android.image.ImageFormat;
import co.pixpie.api.android.image.ImageLocationType;
import co.pixpie.api.android.image.ImageTransformation;
import co.pixpie.api.android.requests.DeleteImageRequest;
import co.pixpie.api.android.requests.UploadImageRequest;
import co.pixpie.api.android.utils.NetworkSingleton;
import co.pixpie.api.android.utils.SyncImageLoader;
import co.pixpie.api.android.utils.Utils;

public class PixpieRequestManager {

    private ApplicationInfo applicationInfo;
    private AuthManager authManager;
    private Context context;
    private RequestQueue requestQueue;
    private boolean sdkDisabled;

    PixpieRequestManager(ApplicationInfo applicationInfo, AuthManager authManager, Context context,
                         RequestQueue requestQueue, boolean sdkDisabled) {
        this.applicationInfo = applicationInfo;
        this.authManager = authManager;
        this.context = context;
        this.requestQueue = requestQueue;
        this.sdkDisabled = sdkDisabled;
    }

    public String getImageUrl(String path, ImageTransformation transformation) {
        checkSDKStatus();

        return buildUrl(ImageLocationType.LOCAL, path, transformation);
    }

    public String getRemoteImageUrl(String url, ImageTransformation transformation) {
        return buildUrl(ImageLocationType.REMOTE, url, transformation);
    }

    public void getRemoteImageUrl(final String url, final ImageTransformation transformation,
                                  final ImageUrlListener listener) {
        if (authManager.isAuthenticated()) {
            listener.onImageUrl(buildUrl(ImageLocationType.REMOTE, url, transformation));
        } else {
            authManager.addAuthInfoListener(new AuthInfoListener() {
                @Override
                public void onAuthInfo(AuthInfo authInfo) {
                    listener.onImageUrl(buildUrl(ImageLocationType.REMOTE, url, transformation));
                }
            });
        }
    }

    public void getImage(String path, ImageView view, ImageTransformation transformation) {
        getImage(path, view, transformation, null);
    }

    public void getImage(String path, ImageView view, ImageTransformation transformation,
                         Response.Listener<PixpieResult> listener) {
        checkSDKStatus();

        NetworkSingleton.getInstance(applicationInfo.getCacheEntriesNumber()).getImageLoader()
                .get(buildUrl(ImageLocationType.LOCAL, path, transformation),
                        new DefaultImageListener(view, listener));
    }

    public void getRemoteImage(String url, ImageView view, ImageTransformation transformation) {
        getRemoteImage(url, view, transformation, null);
    }

    public void getRemoteImage(String url, ImageView view, ImageTransformation transformation,
                               Response.Listener<PixpieResult> listener) {
        NetworkSingleton.getInstance(applicationInfo.getCacheEntriesNumber()).getImageLoader()
                .get(buildUrl(ImageLocationType.REMOTE, url, transformation),
                        new DefaultImageListener(view, listener));
    }

    public void uploadImage(@NonNull byte[] image, @NonNull String contentType,
                            @NonNull String encodedImageName, @NonNull String innerPath,
                            Response.Listener<PixpieResult> listener) {
        checkSDKStatus();

        new UploadImageRequest(image, contentType, encodedImageName, innerPath)
                .executeInQueue(requestQueue, applicationInfo, authManager.getAuthInfo(), listener);
    }

    public void deleteImage(@NonNull String imageName, @NonNull String pathToImage,
                            Response.Listener<PixpieResult> listener) {
        checkSDKStatus();

        new DeleteImageRequest(imageName, pathToImage)
                .executeInQueue(requestQueue, applicationInfo, authManager.getAuthInfo(), listener);
    }

    private String buildUrl(ImageLocationType locationType, String location, ImageTransformation transformation) {
        validateTransformation(transformation);

        if (!authManager.isAuthenticated() && locationType == ImageLocationType.REMOTE) {
            return location;
        }

        Integer width = transformation.getWidth();
        Integer height = transformation.getHeight();
        Integer quality = transformation.getQuality();
        ImageFormat format = transformation.getFormat();

        // add quality if wasn't provided
        if (quality == null) {
            quality = Utils.calculateImageQuality(context);
        }

        if (sdkDisabled || (authManager.isAuthenticated() && authManager.getAuthInfo().isAbTestingEnabled())) {
            if (locationType == ImageLocationType.REMOTE) {
                return location;
            } else if (locationType == ImageLocationType.LOCAL) {
                width = height = quality = null;
                format = ImageFormat.DEFAULT;
            }
        }

        if (transformation.isOriginal()) {
            width = height = quality = null;
        }

        return getUrl(locationType, location, format, width, height, quality, transformation.getCropAlignType());
    }

    private void validateTransformation(ImageTransformation transformation) {
        if (transformation.getWidth() != null) {
            Validate.isTrue(transformation.getWidth() > 0, "Width should be greater than 0");
        }
        if (transformation.getHeight() != null) {
            Validate.isTrue(transformation.getHeight() > 0, "Height should be greater than 0");
        }
        if (transformation.getQuality() != null) {
            Validate.inclusiveBetween(1L, 100L, transformation.getQuality(),
                    "Quality should be greater than 0 and less or equal 100");
        }
        if (transformation.getWidth() == null && transformation.getHeight() == null &&
                transformation.getCropAlignType() != null) {
            Validate.isTrue(false, "Transformation could not have crop value set, when at least height or width " +
                    "is not provided");
        }
    }

    private String getUrl(ImageLocationType locationType, String location, ImageFormat format,
                          Integer width, Integer height, Integer quality, CropAlignType сropAlignType) {
        if (!authManager.isAuthenticated()) {
            // shouldn't happen
            return "";
        }

        // {pathToCdn}/{requestType}/{imgType}/{params}/.../path-to-image
        return authManager.getAuthInfo().getCdnUrl() + "/" + locationType.getLocationString() +
                "/" + format.getFormatString() + "/" + getParamsString(width, height, quality, сropAlignType) +
                "/" + location;
    }

    private String getParamsString(Integer width, Integer height, Integer quality, CropAlignType cropAlignType) {
        StringBuilder sb = new StringBuilder();
        appendIntegerParam(sb, "w_", width);
        appendIntegerParam(sb, "h_", height);
        appendIntegerParam(sb, "q_", quality);
        appendCropAlignTypeParam(sb, "c_", cropAlignType);
        if (sb.length() == 0) {
            sb.append("w_0,h_0");
        }
        return sb.toString();
    }

    private void appendIntegerParam(StringBuilder sb, String prefix, Integer value) {
        if (value != null) {
            if (sb.length() != 0) {
                sb.append(',');
            }
            sb.append(prefix).append(value);
        }
    }

    private void appendCropAlignTypeParam(StringBuilder sb, String appendValue, CropAlignType cropAlignType) {
        if (cropAlignType != null) {
            sb.append(',').append(appendValue).append(cropAlignType.getUrlValue());
        }
    }

    private void checkSDKStatus() throws SDKDisabledException {
        if (sdkDisabled) {
            throw new SDKDisabledException("SDK was manually disabled");
        }
        if (!authManager.isAuthenticated()) {
            throw new SDKDisabledException("SDK is not authenticated");
        }
    }

    private class DefaultImageListener implements SyncImageLoader.ImageListener {

        private ImageView view;
        private Response.Listener<PixpieResult> listener;

        DefaultImageListener(ImageView view, Response.Listener<PixpieResult> listener) {
            this.view = view;
            this.listener = listener;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            if (listener != null) {
                listener.onResponse(new PixpieResult(false, error, ""));
            }
        }

        @Override
        public void onResponse(SyncImageLoader.ImageContainer response, boolean isImmediate) {
            if (response.getBitmap() != null) {
                view.setImageBitmap(response.getBitmap());
            }
            if (listener != null) {
                listener.onResponse(new PixpieResult(true, null, ""));
            }
        }
    }
}
