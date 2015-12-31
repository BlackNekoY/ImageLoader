package com.rdc.imageloader.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.rdc.imageloader.net.base.ImageCache;
import com.rdc.imageloader.util.MD5Util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    //CPU数目
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //核心线程数目
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    //最大线程数目
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    //保活
    private static final int KEEP_ALIVE = 1;
    private static final String TAG = "ImageLoader";

    private ImageCache mCache;
    private ThreadFactory mThreadFactory;
    private Executor mThreadPool;
    private ImageHandler mHandler;


    public ImageLoader(Context context) {
        mCache = new DoubleCache(context);
        initImageLoader();
        mHandler = new ImageHandler();
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

    public void setImageCache(ImageCache imageCache) {
        mCache = imageCache;
    }

    public void bindImageView(final String url, final ImageView imageView) {
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
    }

    public Bitmap loadBitmap(String urlStr) {
        String key = MD5Util.hashKey(urlStr);
        Bitmap bitmap = mCache.get(key);
        if (bitmap == null) {
            bitmap = downloadBitmapFromNet(urlStr);
            if (bitmap != null) {
                mCache.put(key, bitmap);
            }
        }
        return bitmap;
    }

    /**
     * 从网络下载图片
     *
     * @param urlStr
     * @return
     */
    private Bitmap downloadBitmapFromNet(String urlStr) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    private class ImageResult {
        ImageView imageView;
        String url;
        Bitmap bitmap;
    }

    private class ImageHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            ImageResult result = (ImageResult) msg.obj;
            String tag = (String) result.imageView.getTag();
            if (tag.equals(result.url)) {
                result.imageView.setImageBitmap(result.bitmap);
            } else {
                Log.e(TAG, "set image bitmap.but url has changed,ignored.");
            }
        }
    }

}
