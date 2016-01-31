package com.rdc.imageloader.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.rdc.imageloader.core.config.DisplayConfig;
import com.rdc.imageloader.core.config.ImageLoaderConfig;
import com.rdc.imageloader.core.request.ImageRequest;
import com.rdc.imageloader.util.MD5Util;

/**
 * Created by blackwhite on 16-1-3.
 */
public class ImageLoader {

    private static final String TAG = "ImageLoader";
    private static ImageLoader sInstance = new ImageLoader();

    private Context context;

    private ImageLoaderConfig config;

    private RequestQueue queue;

    private ImageLoader() {

    }

    public static ImageLoader getInstance() {
        return sInstance;
    }

    public void init(ImageLoaderConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("ImageLoaderConfig is null.");
        }
        Log.e(TAG,config.toString());
        this.config = config;
        this.context = config.context;
        queue = new RequestQueue(context, config.threadCount, config.diskCache);
        queue.start();
    }


    public interface ImageListener {
        public void onComplete(ImageView imageView, Bitmap bitmap);

        public void onError(ImageView imageView);
    }


    public void displayImage(ImageView imageView, String uri, ImageListener listener) {
        imageView.setTag(uri);
        if (TextUtils.isEmpty(uri) && config.displayConfig.imageEmpty != DisplayConfig.NOT_SET) {
            imageView.setImageResource(config.displayConfig.imageEmpty);
            return;
        }
        String cacheKey = getCacheKey(uri);
        Bitmap bitmap = config.memoryCache.get(cacheKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        if(config.displayConfig.imageLoading != DisplayConfig.NOT_SET) {
            imageView.setImageResource(config.displayConfig.imageLoading);
        }
        ImageRequest request = new ImageRequest(imageView, uri, cacheKey, config, listener);
        queue.add(request);
    }

    public void displayImage(ImageView imageView, String uri) {
        displayImage(imageView, uri, null);
    }


    private String getCacheKey(String uri) {
        return MD5Util.hashKey(uri);
    }


}
