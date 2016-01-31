package com.rdc.imageloader.core.loader;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by blackwhite on 16-1-3.
 */
public class LoaderManager {

    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final String FILE = "file";

    private Context context;
    private Map<String,Loader> loaderMap = new HashMap<>();
    private Loader nullLoader = new NullLoader();

    private volatile static LoaderManager sLoaderManager;

    public static LoaderManager getInstance() {
        if(sLoaderManager == null) {
            synchronized (LoaderManager.class) {
                if(sLoaderManager == null) {
                    sLoaderManager = new LoaderManager();
                }
            }
        }
        return sLoaderManager;
    }

    private LoaderManager () {
        register(HTTP,new URLLoader());
        register(HTTPS, new URLLoader());
        register(FILE,new LocalLoader());
    }

    public Loader getLoader(String schema) {
        if(loaderMap.containsKey(schema)) {
            return loaderMap.get(schema);
        }
        return nullLoader;
    }

    public void register(String schema,Loader loader) {
        if(loaderMap.containsKey(schema)) {
            return;
        }
        loaderMap.put(schema,loader);
    }
}
