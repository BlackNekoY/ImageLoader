package com.rdc.imageloader.loader;

import android.graphics.Bitmap;

import com.rdc.imageloader.request.ImageRequest;

/**
 * Created by blackwhite on 16-1-3.
 */
public interface Loader {

    public Bitmap loadImage(ImageRequest request);
}
