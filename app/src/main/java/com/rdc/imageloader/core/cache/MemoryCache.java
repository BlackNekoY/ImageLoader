package com.rdc.imageloader.core.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by blackwhite on 15-12-31.
 */
public class MemoryCache implements ImageCache {

    private LruCache<String, Bitmap> mLruCache;

    public MemoryCache(long cacheSize) {
        initCacheOptions(cacheSize);
    }

    private void initCacheOptions(long cacheSize) {
        mLruCache = new LruCache<String, Bitmap>((int) (cacheSize)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        if (get(key) == null) {
            mLruCache.put(key, bitmap);
        }
    }

    @Override
    public Bitmap get(String key) {
        return mLruCache.get(key);
    }
}
