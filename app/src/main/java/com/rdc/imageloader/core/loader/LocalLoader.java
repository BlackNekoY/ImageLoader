package com.rdc.imageloader.core.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.rdc.imageloader.core.request.ImageRequest;

import java.io.File;

/**
 * Created by blackwhite on 16-1-4.
 */
public class LocalLoader extends AbsLoader {


    @Override
    protected Bitmap onLoadImage(ImageRequest request) {
        final String imagePath = Uri.parse(request.getUri()).getPath();
        final File imgFile = new File(imagePath);
        if(!imgFile.exists()) {
            return null;
        }
        return null;
    }
}
