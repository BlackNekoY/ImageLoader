package com.rdc.imageloader.cache;

import android.graphics.Bitmap;

/**
 * Created by blackwhite on 16-1-4.
 */
public class NoCache implements ImageCache {
    @Override
    public void put(String key, Bitmap bitmap) {

    }

    @Override
    public Bitmap get(String key) {
        return null;
    }
}
