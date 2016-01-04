package com.rdc.imageloader.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.rdc.imageloader.cache.ImageCache;
import com.rdc.imageloader.loader.Loader;
import com.rdc.imageloader.loader.LoaderManager;
import com.rdc.imageloader.request.ImageRequest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by blackwhite on 16-1-3.
 */
public class RequestQueue {

    public static final int DEFAULT_THREAD_COUNT = 5;

    private Context context;
    private ImageCache cache;
    private BlockingQueue<ImageRequest> mQueue = new PriorityBlockingQueue<>();
    private ImageDispatcher[] dispatchers;

    public RequestQueue(Context context, int threadCount, ImageCache cache) {
        this.context = context;
        dispatchers = new ImageDispatcher[threadCount];
        this.cache = cache;
    }

    private class ImageDispatcher extends Thread {
        @Override
        public void run() {
            while (!this.isInterrupted()) {
                try {
                    final ImageRequest request = mQueue.take();
                    String cacheKey = request.getCacheKey();
                    Bitmap bitmap = cache.get(cacheKey);
                    if (bitmap == null) {
                        String schema = parseSchema(request.getUri());
                        Loader loader = LoaderManager.getIntanse(context).getLoader(schema);
                        bitmap = loader.loadImage(request);
                        if (bitmap != null) {
                            cache.put(cacheKey, bitmap);
                        }
                    }
                    request.onResponse(bitmap);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private String parseSchema(String uri) {
            if (uri.contains("://")) {
                return uri.split("://")[0];
            } else {
                Log.e(getName(), "### wrong scheme, image uri is : " + uri);
            }
            return "";
        }
    }

    public void start() {
        for (int i = 0; i < dispatchers.length; i++) {
            dispatchers[i] = new ImageDispatcher();
            dispatchers[i].start();
        }
    }

    public void stop() {
        for (int i = 0; i < dispatchers.length; i++) {
            dispatchers[i].interrupt();
        }
    }

    public void add(ImageRequest request) {
        mQueue.add(request);
    }


}
