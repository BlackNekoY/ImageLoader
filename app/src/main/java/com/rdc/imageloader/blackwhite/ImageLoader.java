package com.rdc.imageloader.blackwhite;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.rdc.imageloader.blackwhite.cache.DiskCache;
import com.rdc.imageloader.blackwhite.cache.MemoryCache;
import com.rdc.imageloader.blackwhite.cache.base.ImageCache;
import com.rdc.imageloader.blackwhite.network.BasicNetwork;
import com.rdc.imageloader.blackwhite.network.Network;
import com.rdc.imageloader.blackwhite.request.ImageRequest;
import com.rdc.imageloader.util.MD5Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by blackwhite on 15-12-31.
 */
public class ImageLoader {

    private static final String TAG = "ImageLoader";

    private ImageCache mCache;
    private ImageHandler mHandler;
    private RequestQueue mQueue;

    private Map<String, BatchedImageRequest> mBatchedImagequests;

    public ImageLoader(Context context, ImageCache cache, Network network) {
        mCache = cache;
        mBatchedImagequests = new HashMap<String, BatchedImageRequest>();
        mQueue = new RequestQueue(network, new DiskCache(context), 1);
        mQueue.start();
        mHandler = new ImageHandler(Looper.getMainLooper());

    }


    public ImageLoader(Context context) {
        this(context, new MemoryCache(context), new BasicNetwork());
    }

    public void loadBitmap(final String urlStr, final ImageListener listener) {
        loadBitmap(urlStr, 0, 0, listener);
    }

    public void loadBitmap(String urlStr, int maxWidth, int maxHeight, ImageListener listener) {
        String cacheKey = getCacheKey(urlStr, maxWidth, maxHeight);

        Bitmap bitmap = mCache.get(cacheKey);
        if (bitmap != null) {
            ImageContainer container = new ImageContainer(urlStr, bitmap, listener);
            listener.onResonse(container);
            return;
        }

        ImageContainer container = new ImageContainer(urlStr, null, listener);
        listener.onResonse(container);

        BatchedImageRequest batchedImageRequest = mBatchedImagequests.get(cacheKey);
        if (batchedImageRequest != null) {
            batchedImageRequest.add(container);
            return;
        }

        ImageRequest request = new ImageRequest(urlStr, maxWidth, maxHeight, cacheKey, makeImageRequestListener(cacheKey));
        batchedImageRequest = new BatchedImageRequest(request, container);
        mBatchedImagequests.put(cacheKey, batchedImageRequest);
        mQueue.add(request);
    }

    private ImageRequest.ImageRequestListener makeImageRequestListener(final String cacheKey) {
        return new ImageRequest.ImageRequestListener() {
            @Override
            public void onResponse(Bitmap bitmap) {
                getImageSuccess(cacheKey, bitmap);
            }

            @Override
            public void onError() {
                getImageFail(cacheKey);
            }
        };
    }

    private void getImageFail(String cacheKey) {
        BatchedImageRequest batchedImageRequest = mBatchedImagequests.remove(cacheKey);
        batchedImageRequest.isError = false;
        Message msg = Message.obtain();
        msg.obj = batchedImageRequest;
        mHandler.sendMessage(msg);
    }

    private void getImageSuccess(String cacheKey, Bitmap bitmap) {
        mCache.put(cacheKey, bitmap);
        BatchedImageRequest batchedImageRequest = mBatchedImagequests.remove(cacheKey);
        batchedImageRequest.bitmap = bitmap;
        batchedImageRequest.isError = false;
        Message msg = Message.obtain();
        msg.obj = batchedImageRequest;
        mHandler.sendMessage(msg);
    }


    private static String getCacheKey(String url, int maxWidth, int maxHeight) {
        StringBuilder builder = new StringBuilder().append("#W").append(maxWidth)
                .append("#H").append(maxHeight).append("#URL").append(url);
        return MD5Util.hashKey(builder.toString());
    }


    class BatchedImageRequest {
        ImageRequest request;
        Bitmap bitmap;
        List<ImageContainer> imageContainers;
        boolean isError;

        BatchedImageRequest(ImageRequest request, ImageContainer imageContainer) {
            this.request = request;
            imageContainers = new ArrayList<>();
            imageContainers.add(imageContainer);
        }

        public void add(ImageContainer imageContainer) {
            imageContainers.add(imageContainer);
        }
    }

    public class ImageContainer {
        private String urlStr;
        private Bitmap bitmap;
        private ImageListener listener;

        public ImageContainer(String urlStr, Bitmap bitmap, ImageListener listener) {
            this.urlStr = urlStr;
            this.bitmap = bitmap;
            this.listener = listener;
        }

        public String getUrlStr() {
            return urlStr;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }
    }

    private class ImageHandler extends Handler {

        ImageHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            BatchedImageRequest request = (BatchedImageRequest) msg.obj;
            batchImage(request);
        }
    }

    private void batchImage(BatchedImageRequest request) {
        for (ImageContainer cir : request.imageContainers) {
            cir.bitmap = request.bitmap;
            cir.listener.onResonse(cir);
        }
    }

    public interface ImageListener {
        public void onResonse(ImageContainer imageContainer);
    }
}
