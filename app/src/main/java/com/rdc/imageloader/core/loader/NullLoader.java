package com.rdc.imageloader.core.loader;

import android.graphics.Bitmap;

import com.rdc.imageloader.core.request.ImageRequest;

/**
 * Created by blackwhite on 16-1-31.
 */
public class NullLoader extends AbsLoader {
    @Override
    protected Bitmap onLoadImage(ImageRequest request) {
        return null;
    }
}
