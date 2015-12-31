package com.rdc.imageloader.net;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.rdc.imageloader.net.base.DiskLruCache;
import com.rdc.imageloader.net.base.ImageCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by blackwhite on 15-12-31.
 */
public class DiskCache implements ImageCache {

    //同一个Key能够对应多少个缓存文件，默认为1
    private final int VALUE_COUNT = 1;
    //读写缓存从哪一个位置读写，默认为0
    private final int INDEX = 0;
    //最大缓存空间大小，默认为50MB
    private final long MAX_SIZE = 20 * 1024 * 1024;

    private DiskLruCache mDiskLruCache;
    private int mValueCount = VALUE_COUNT;
    private long mMaxSize = MAX_SIZE;

    private boolean isCacheCreated = false;
    private Context mContext;

    public DiskCache(Context context) {
        mContext = context;
        initCacheOptions();
    }

    private void initCacheOptions() {
        try {
            File cacheDirectory = getCacheDir();
            if(!cacheDirectory.exists()) {
                cacheDirectory.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDirectory, getAppVersion(), mValueCount, mMaxSize);
            isCacheCreated = true;
        } catch (IOException e) {
            isCacheCreated = false;
            e.printStackTrace();
        }
    }

    /**
     * 得到应用缓存目录
     */
    private File getCacheDir() {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = mContext.getExternalCacheDir().getPath();
        } else {
            cachePath = mContext.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + "Bitmap");
    }

    /**
     * 得到应用版本号
     * @return
     */
    private int getAppVersion() {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if (editor != null) {
                OutputStream os = editor.newOutputStream(INDEX);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                editor.commit();
                mDiskLruCache.flush();
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bitmap get(String key) {
        Bitmap bitmap = null;
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                InputStream is = snapshot.getInputStream(INDEX);
                bitmap = BitmapFactory.decodeStream(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}

