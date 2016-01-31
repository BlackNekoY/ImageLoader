package com.rdc.imageloader.core.loader;

import android.graphics.Bitmap;

import com.rdc.imageloader.core.request.ImageRequest;
import com.rdc.imageloader.util.ImageResizer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by blackwhite on 16-1-3.
 */
public class URLLoader implements Loader {
    @Override
    public Bitmap loadImage(ImageRequest request) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(request.getUri());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            bitmap = ImageResizer.decodeBitmapFromStream(is, request.getImageView().getWidth(), request.getImageView().getHeight());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
