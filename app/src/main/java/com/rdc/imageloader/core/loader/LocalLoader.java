package com.rdc.imageloader.core.loader;

import android.content.Context;
import android.graphics.Bitmap;

import com.rdc.imageloader.core.request.ImageRequest;

/**
 * Created by blackwhite on 16-1-4.
 */
public class LocalLoader implements Loader {
    private Context context;

    public LocalLoader(Context context) {
        this.context = context;
    }

    @Override
    public Bitmap loadImage(ImageRequest request) {
        Bitmap bitmap = null;
        return bitmap;
    }
}
