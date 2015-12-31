package com.rdc.imageloader.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by blackwhite on 15-12-31.
 */
public class MD5Util {
    /**
     * 根据url生成在内存和缓存的key,使用MD5加密方法
     *
     * @param url
     * @return
     */
    public static String hashKey(String url) {
        String cacheKey;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            cacheKey = bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
            e.printStackTrace();
        }
        return cacheKey;
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0XFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


}
