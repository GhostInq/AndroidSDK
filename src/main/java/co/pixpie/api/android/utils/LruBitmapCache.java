package co.pixpie.api.android.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class LruBitmapCache extends LruCache<String, Bitmap> implements
        SyncImageLoader.ImageCache {

    public LruBitmapCache(Integer sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}