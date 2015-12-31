package com.rdc.imageloader.net.base;

import android.graphics.Bitmap;

/**
 * Created by blackwhite on 15-12-31.
 */
public interface ImageCache {

    public void put(String key, Bitmap bitmap);

    public Bitmap get(String key);
}
