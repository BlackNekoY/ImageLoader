package com.rdc.imageloader.util;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 图片压缩工具类
 *
 * @author BlackWhite
 */
public class ImageResizer {

    private Context mContext;
    private static final String TAG = "ImageResizer";

    public ImageResizer(Context context) {
        this.mContext = context;
    }

    /**
     * 从资源加载Bitmap
     *
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeBitmapFromResource(int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(mContext.getResources(), resId, options);
        options.inSampleSize = calcuteInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(mContext.getResources(), resId, options);
    }

    /**
     * 从文件描述符加载Bitmap
     *
     * @param fd
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Bitmap decodeBitmapFromFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight) throws FileNotFoundException, IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = calcuteInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

    /**
     * 根据需要计算出图片的inSampleSize
     *
     * @param options   Bitmap的信息参数
     * @param reqWidth  Bitmap所要展示的宽度
     * @param reqHeight Bitmap所要展示的高度
     * @return
     */
    public int calcuteInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;


        if (height > reqWidth || width > reqHeight) {
            int halfWidth = width / 2;
            int halfHeight = height / 2;

            while ((halfWidth / inSampleSize) >= reqWidth && (halfHeight / inSampleSize) >= reqHeight) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}