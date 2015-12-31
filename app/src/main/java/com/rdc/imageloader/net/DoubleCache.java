package com.rdc.imageloader.net;

import android.content.Context;
import android.graphics.Bitmap;

import com.rdc.imageloader.net.base.ImageCache;

/**
 * Created by blackwhite on 15-12-31.
 */
public class DoubleCache implements ImageCache {

    private MemoryCache mMemoryCache;
    private DiskCache mDiskCache;

    public DoubleCache(Context context) {
        mMemoryCache = new MemoryCache(context);
        mDiskCache = new DiskCache(context);
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
