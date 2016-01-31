package com.rdc.imageloader.core.config;

import android.widget.ImageView;

/**
 * Created by blackwhite on 16-1-3.
 */
public class DisplayConfig {
    public static final int NOT_SET = -Integer.MAX_VALUE;
    public int imageFail;
    public int imageLoading;
    public int imageEmpty;
    public ImageView.ScaleType scaleType;

    private DisplayConfig(Builder builder) {
        imageFail = builder.imageFail;
        imageLoading = builder.imageLoading;
        imageEmpty = builder.imageEmpty;
        scaleType = builder.scaleType;
    }

    @Override
    public String toString() {
        return "DisplayConfig{" +
                "imageFail=" + imageFail +
                ", imageLoading=" + imageLoading +
                ", imageEmpty=" + imageEmpty +
                ", scaleType=" + scaleType +
                '}';
    }

    public static DisplayConfig createDefault() {
        return new Builder().scaleType(ImageView.ScaleType.CENTER_INSIDE)
                .imageEmptyResource(NOT_SET)
                .imageFailResource(NOT_SET)
                .imageLoadingResource(NOT_SET)
                .build();
    }


    public static class Builder {
        private int imageFail = NOT_SET;
        private int imageLoading = NOT_SET;
        private int imageEmpty = NOT_SET;
        private ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_INSIDE;

        public Builder imageFailResource(int resId) {
            this.imageFail = resId;
            return this;
        }

        public Builder imageLoadingResource(int resId) {
            this.imageLoading = resId;
            return this;
        }

        public Builder imageEmptyResource(int resId) {
            this.imageEmpty = resId;
            return this;
        }

        public Builder scaleType(ImageView.ScaleType scaleType) {
            this.scaleType = scaleType;
            return this;
        }

        public DisplayConfig build() {
            checkConfig();
            return new DisplayConfig(this);
        }

        private void checkConfig() {

        }

    }
}
