package com.rdc.imageloader.loader;

import android.content.Context;

/**
 * Created by blackwhite on 16-1-3.
 */
public class LoaderManager {

    private static LoaderManager intanse = new LoaderManager();
    private static Context context;

    private LoaderManager() {
    }

    public static LoaderManager getIntanse(Context context) {
        LoaderManager.context = context;
        return intanse;
    }

    public Loader getLoader(String schema) {
        if ("http".equals(schema) || "https".equals(schema)) {
            return new URLLoader();
        } else if ("file".equals(schema)) {
            return new LocalLoader(context);
        }
        throw new IllegalArgumentException("图片路径错误");
    }
}
