package com.rdc.imageloader.core.loader;

import android.graphics.Bitmap;

import com.rdc.imageloader.core.request.ImageRequest;

/**
 * Created by blackwhite on 16-1-31.
 */
public abstract class AbsLoader implements Loader {

    @Override
    public final Bitmap loadImage(ImageRequest request) {
        String cacheKey = request.getCacheKey();
        Bitmap bitmap = null;
        if(request.getConfig().getCache() != null) {
            //用户使用了自定义Cache
            bitmap = request.getConfig().getCache().get(cacheKey);
        }else {
            //用户没有使用自定义缓存
            bitmap = request.getConfig().getDiskCache().get(cacheKey);
        }

        if(bitmap == null) {
            //从缓存中没有取到
            bitmap = onLoadImage(request);
            cacheBitmap(request,bitmap);
        }
        return bitmap;
    }

    private void cacheBitmap(ImageRequest request, Bitmap bitmap) {
        if(bitmap == null) {
            return;
        }
        String cacheKey = request.getCacheKey();
        if(request.getConfig().getCache() != null) {
            request.getConfig().getCache().put(cacheKey,bitmap);
        }else {
            request.getConfig().getMemoryCache().put(cacheKey,bitmap);
            request.getConfig().getDiskCache().put(cacheKey,bitmap);
        }
    }

    protected abstract Bitmap onLoadImage(ImageRequest request) ;
}
