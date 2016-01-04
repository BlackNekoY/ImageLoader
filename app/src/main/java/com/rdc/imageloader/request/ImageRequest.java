package com.rdc.imageloader.request;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.rdc.imageloader.config.DisplayConfig;
import com.rdc.imageloader.config.ImageLoaderConfig;
import com.rdc.imageloader.core.ImageLoader;

/**
 * Created by blackwhite on 16-1-3.
 */
public class ImageRequest implements Comparable<ImageRequest> {
    private ImageView imageView;
    private String uri;
    private ImageLoader.ImageListener listener;
    private ImageLoaderConfig config;
    private String cacheKey;

    public ImageRequest(ImageView imageView, String uri, String cacheKey, ImageLoaderConfig config,
                        ImageLoader.ImageListener listener) {
        this.imageView = imageView;
        this.uri = uri;
        this.listener = listener;
        this.config = config;
        this.cacheKey = cacheKey;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public String getUri() {
        return uri;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void onResponse(final Bitmap bitmap) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                doResponse(bitmap);
            }
        };

        handler.post(r);
    }

    private void doResponse(Bitmap bitmap) {
        if(bitmap != null) {
            config.memoryCache.put(cacheKey,bitmap);
        }
        if (listener != null) {
            parseBitmapToListener(bitmap);
        } else {
            setImageViewBitmap(bitmap);
        }

    }

    private void setImageViewBitmap(Bitmap bitmap) {
        String tag = (String) imageView.getTag();
        if (bitmap == null && config.displayConfig.imageFail != DisplayConfig.NOT_SET) {
            imageView.setImageResource(config.displayConfig.imageFail);
        }
        if (bitmap != null && tag.equals(uri)) {
            imageView.setImageBitmap(bitmap);
        }
    }

    private void parseBitmapToListener(Bitmap bitmap) {
        if (bitmap == null) {
            listener.onError(imageView);
        } else {
            listener.onComplete(imageView, bitmap);
        }
    }

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public int compareTo(ImageRequest another) {
        return 1;
    }
}
