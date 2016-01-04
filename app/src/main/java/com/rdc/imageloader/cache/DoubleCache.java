package com.rdc.imageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by blackwhite on 15-12-31.
 */
public class DoubleCache implements ImageCache {

    private MemoryCache mMemoryCache;
    private DiskCache mDiskCache;

    public DoubleCache(Context context,long memoryCacheSize,long diskCacheSize) {
        mMemoryCache = new MemoryCache(memoryCacheSize);
        mDiskCache = new DiskCache(context,diskCacheSize);
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        mMemoryCache.put(key,bitmap);
        mDiskCache.put(key,bitmap);
    }

    @Override
    public Bitmap get(String key) {
        Bitmap bitmap = null;
        bitmap = mMemoryCache.get(key);
        if(bitmap == null) {
            bitmap = mDiskCache.get(key);
        }
        return bitmap;
    }
}
