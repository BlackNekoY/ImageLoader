package com.rdc.imageloader.core.config;


import android.content.Context;

import com.rdc.imageloader.core.cache.DiskCache;
import com.rdc.imageloader.core.cache.ImageCache;
import com.rdc.imageloader.core.cache.MemoryCache;
import com.rdc.imageloader.core.cache.NoCache;
import com.rdc.imageloader.core.policy.LoadPolicy;
import com.rdc.imageloader.core.policy.SerialPolicy;

/**
 * Created by blackwhite on 16-1-3.
 */
public class ImageLoaderConfig {

    private static final long DEFAULT_MEMORY_CACHE_SIZE = Runtime.getRuntime().maxMemory() / 8;
    private static final long DEFAULT_DISK_CACHE_SIZE = 10 * 1024 * 1024;
    private static final int DEFAULT_THREAD_COUNT = Runtime.getRuntime().availableProcessors() + 1;

    public Context context;

    public ImageCache memoryCache;
    public long memoryCacheSize;
    public boolean useMemoryCache;

    public ImageCache diskCache;
    public long diskCacheSize;
    public boolean useDiskCache;

    public int threadCount;
    public LoadPolicy loadPolicy;

    public DisplayConfig displayConfig;


    private ImageLoaderConfig(Builder builder) {
        context = builder.context;
        memoryCache = builder.memoryCache;
        memoryCacheSize = builder.memoryCacheSize;
        useMemoryCache = builder.useMemoryCache;
        diskCache = builder.diskCache;
        diskCacheSize = builder.diskCacheSize;
        useDiskCache = builder.useDiskCache;
        threadCount = builder.threadCount;
        loadPolicy = builder.loadPolicy;
        displayConfig = builder.displayConfig;
    }

    public void setDisplayConfig(DisplayConfig displayConfig) {
        this.displayConfig = displayConfig;
    }

    public static ImageLoaderConfig createDefault(Context context) {
        return new Builder(context)
                .memoryCache(new MemoryCache(DEFAULT_MEMORY_CACHE_SIZE))
                .memoryCacheSize(DEFAULT_MEMORY_CACHE_SIZE)
                .useMemoryCache(true)
                .diskCache(new NoCache())
                .diskCacheSize(DEFAULT_DISK_CACHE_SIZE)
                .useDiskCache(false)
                .threadCount(DEFAULT_THREAD_COUNT)
                .displayConfig(DisplayConfig.createDefault())
                .build();
    }

    @Override
    public String toString() {
        return "ImageLoaderConfig{" +
                "context=" + context +
                ", memoryCache=" + memoryCache +
                ", memoryCacheSize=" + memoryCacheSize +
                ", useMemoryCache=" + useMemoryCache +
                ", diskCache=" + diskCache +
                ", diskCacheSize=" + diskCacheSize +
                ", useDiskCache=" + useDiskCache +
                ", threadCount=" + threadCount +
                ", loadPolicy=" + loadPolicy +
                ", displayConfig=" + displayConfig +
                '}';
    }

    public static class Builder {

        private Context context;

        //默认使用内存缓存
        private ImageCache memoryCache = new MemoryCache(DEFAULT_MEMORY_CACHE_SIZE);
        //默认内存缓存大小为总内存四分之一
        private long memoryCacheSize = DEFAULT_MEMORY_CACHE_SIZE;
        private boolean useMemoryCache = true;

        //默认不使用磁盘缓存
        private ImageCache diskCache = new NoCache();
        //默认磁盘缓存大小为10MB
        private long diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
        private boolean useDiskCache = false;

        //默认线程数为CPU数+1
        private int threadCount = DEFAULT_THREAD_COUNT;
        //默认加载策略为顺序加载
        private LoadPolicy loadPolicy = new SerialPolicy();

        private DisplayConfig displayConfig = DisplayConfig.createDefault();

        public Builder(Context context) {
            this.context = context;
        }

        public Builder memoryCache(ImageCache memoryCache) {
            this.memoryCache = memoryCache;
            return this;
        }

        public Builder memoryCacheSize(long memoryCacheSize) {
            this.memoryCacheSize = memoryCacheSize;
            return this;
        }

        public Builder useMemoryCache(boolean useMemoryCache) {
            this.useMemoryCache = useMemoryCache;
            return this;
        }

        public Builder diskCache(ImageCache diskCache) {
            this.diskCache = diskCache;
            return this;
        }

        public Builder diskCacheSize(long diskCacheSize) {
            this.diskCacheSize = diskCacheSize;
            return this;
        }

        public Builder useDiskCache(boolean useDiskCache) {
            this.useDiskCache = useDiskCache;
            return this;
        }

        public Builder threadCount(int threadCount) {
            this.threadCount = threadCount;
            return this;
        }

        public Builder loadPolicy(LoadPolicy policy) {
            this.loadPolicy = policy;
            return this;
        }

        public Builder displayConfig(DisplayConfig displayConfig) {
            this.displayConfig = displayConfig;
            return this;
        }

        public ImageLoaderConfig build() {
            checkConfig();
            return new ImageLoaderConfig(this);
        }

        private void checkConfig() {
            if (context == null) {
                throw new IllegalArgumentException("Context can not be null.");
            }
            if (threadCount < 1) {
                threadCount = DEFAULT_THREAD_COUNT;
            }
            if (memoryCacheSize < 0) {
                memoryCacheSize = DEFAULT_MEMORY_CACHE_SIZE;
            }
            if (diskCacheSize < 0) {
                diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
            }
            if (!useMemoryCache) {
                memoryCache = new NoCache();
            }
            if (!useDiskCache) {
                diskCache = new NoCache();
            } else if (diskCache instanceof NoCache) {
                diskCache = new DiskCache(context, diskCacheSize);
            }
            if(loadPolicy == null) {
                loadPolicy = new SerialPolicy();
            }
            if (displayConfig == null) {
                displayConfig = DisplayConfig.createDefault();
            }
        }
    }

}
