package com.rdc.imageloader.blackwhite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.rdc.imageloader.blackwhite.cache.base.ImageCache;
import com.rdc.imageloader.blackwhite.cache.DoubleCache;
import com.rdc.imageloader.blackwhite.network.BasicNetwork;
import com.rdc.imageloader.blackwhite.network.Network;
import com.rdc.imageloader.blackwhite.request.Request;
import com.rdc.imageloader.blackwhite.response.HttpResponse;
import com.rdc.imageloader.util.ImageResizer;
import com.rdc.imageloader.util.MD5Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by blackwhite on 15-12-31.
 */
public class ImageLoader {

    private static final String TAG = "ImageLoader";
    //CPU数目
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //核心线程数目
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    //最大线程数目
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    //保活
    private static final int KEEP_ALIVE = 1;

    private ImageCache mCache;
    private ThreadFactory mThreadFactory;
    private Executor mThreadPool;
    private ImageHandler mHandler;
    private Network mNetwork;

    private ImageResizer mImageResizer;


    public ImageLoader(Context context, ImageCache cache, Network network) {
        initImageLoader();
        mCache = cache;
        mNetwork = network;
        mHandler = new ImageHandler(Looper.getMainLooper());
        mImageResizer = new ImageResizer(context);
    }


    public ImageLoader(Context context) {
        this(context, new DoubleCache(context), new BasicNetwork());
    }

    public interface ImageListener {
        public void onResonse(Bitmap bitmap);
    }


    private void initImageLoader() {
        mThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                return new Thread(r, "ImageLoader #" + mCount.getAndIncrement());
            }
        };

        mThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
                TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), mThreadFactory);
    }

/*    public void bindImageView(final String url, final ImageView imageView) {
        imageView.setTag(url);
        final String key = MD5Util.hashKey(url);

        final Runnable r = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = mCache.get(key);
                if (bitmap == null) {
                    bitmap = loadBitmap(url);
                }
                if (bitmap != null) {
                    ImageResult result = new ImageResult();
                    result.bitmap = bitmap;
                    result.url = url;
                    result.imageView = imageView;
                    Message msg = Message.obtain();
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            }
        };
        mThreadPool.execute(r);
    }*/

    public void loadBitmap(final String urlStr, final ImageListener listener) {
        loadBitmap(urlStr, 0, 0, listener);
    }

    public void loadBitmap(final String urlStr, int maxWidth, int maxHeight, final ImageListener listener) {
        final ImageRequest request = new ImageRequest(null, urlStr, maxWidth, maxHeight, listener);
        listener.onResonse(null);

        final String key = getCacheKey(urlStr, maxWidth, maxHeight);
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = mCache.get(key);
                if (bitmap == null) {
                    HttpResponse response = null;
                    try {
                        response = mNetwork.peformRequest(request);
                        bitmap = request.parseResponse(response);
                        if (bitmap != null) {
                            mCache.put(key, bitmap);
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "network is error");
                    }
                }
                request.bitmap = bitmap;
                Message msg = Message.obtain();
                msg.obj = request;
                mHandler.sendMessage(msg);
            }
        };
        mThreadPool.execute(task);
    }


    private static String getCacheKey(String url, int maxWidth, int maxHeight) {
        StringBuilder builder = new StringBuilder().append("#W").append(maxWidth)
                .append("#H").append(maxHeight).append("#URL").append(url);
        return MD5Util.hashKey(builder.toString());
    }


    private class ImageRequest extends Request<Bitmap> {
        Bitmap bitmap;
        ImageListener listener;
        int maxWidth;
        int maxHeight;

        public ImageRequest(Bitmap bitmap, String url, int maxWidth, int maxHeight, ImageListener listener) {
            super(url);
            this.bitmap = bitmap;
            this.maxWidth = maxWidth;
            this.maxHeight = maxHeight;
            this.listener = listener;
        }

        @Override
        public Bitmap parseResponse(HttpResponse response) {
            Bitmap bitmap = null;
            InputStream is = response.getInputStream();
            if (maxWidth == 0 && maxHeight == 0) {
                bitmap = BitmapFactory.decodeStream(is);
            } else {
                try {
                    bitmap = mImageResizer.decodeBitmapFromStream(is, maxWidth, maxHeight);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.bitmap = bitmap;
            return bitmap;
        }
    }


    private class ImageHandler extends Handler {

        ImageHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            ImageRequest request = (ImageRequest) msg.obj;
            request.listener.onResonse(request.bitmap);
        }
    }


}
