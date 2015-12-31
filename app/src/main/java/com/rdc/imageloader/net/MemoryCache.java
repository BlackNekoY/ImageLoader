package com.rdc.imageloader.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.rdc.imageloader.net.base.ImageCache;

/**
 * Created by blackwhite on 15-12-31.
 */
public class MemoryCache implements ImageCache {

    private LruCache<String, Bitmap> mLruCache;
    private Context mContext;

    public MemoryCache(Context context) {
        mContext = context;
        initCacheOptions();
    }

    private void initCacheOptions() {
        //可用内存（MB）
        int maxSize = (int) Runtime.getRuntime().maxMemory() / 1024;
        mLruCache = new LruCache<String, Bitmap>(maxSize / 4) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
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
