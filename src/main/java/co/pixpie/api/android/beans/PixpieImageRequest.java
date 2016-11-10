package co.pixpie.api.android.beans;


import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

/**
 * Created by accord on 8/5/16.
 */
public class PixpieImageRequest extends ImageRequest {

    public PixpieImageRequest(String url, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, Bitmap.Config decodeConfig, Response.ErrorListener errorListener) {
        super(url, listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(30000, 5, 1.0f));
    }
}
