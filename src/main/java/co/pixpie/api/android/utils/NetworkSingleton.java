package co.pixpie.api.android.utils;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import co.pixpie.api.android.networking.PixpieRequestQueueWrapper;

public class NetworkSingleton {

    public static final String TAG = NetworkSingleton.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private SyncImageLoader mImageLoader;
    private static NetworkSingleton mInstance;
    private LruBitmapCache cache;
    static Integer cacheEntries;
    private static Context mCtx;

    private NetworkSingleton() {
        mRequestQueue = getRequestQueue();
        cache = new LruBitmapCache(cacheEntries);
        mImageLoader = new SyncImageLoader(getRequestQueue(), cache);
    }

    public static synchronized NetworkSingleton getInstance(Integer cacheEntriesNumber) {
        cacheEntries = cacheEntriesNumber;
        if (mInstance == null) {
            mInstance = new NetworkSingleton();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = PixpieRequestQueueWrapper.newRequestQueue(mCtx); // Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public static void setContext(Context context){
        mCtx = context;
    }

    public void clearCache() {
        cache = new LruBitmapCache(cacheEntries);
        mImageLoader = new SyncImageLoader(getRequestQueue(), cache);
    }

    public SyncImageLoader getImageLoader() {
        getRequestQueue();
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
