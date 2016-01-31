package com.rdc.imageloader.policy;

import com.rdc.imageloader.request.ImageRequest;

/**
 * Created by blackwhite on 16-1-31.
 */
public interface LoadPolicy {
    public int compare(ImageRequest request1,ImageRequest request2) ;
}
