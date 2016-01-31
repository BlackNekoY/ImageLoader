package com.rdc.imageloader.core.policy;

import com.rdc.imageloader.core.request.ImageRequest;

/**
 * Created by blackwhite on 16-1-31.
 */
public class ReversePolicy implements LoadPolicy {
    @Override
    public int compare(ImageRequest request1, ImageRequest request2) {
        return request2.getNumber() - request1.getNumber();
    }
}
