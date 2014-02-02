package com.flaminga.client.utility;

import com.squareup.picasso.LruCache;

/**
 * Created by mjanes on 2/1/14.
 */
public class UiUtil {

    public enum ProfileImageSize {
        MINI, // 24x24
        NORMAL, // 48x48
        BIGGER, // 73x73
        ORIGINAL, // undefined. This will be the size the image was originally
        // uploaded in.
        // The filesize of original images can be very big so use this
        // parameter with
        // caution.
    }

    private static LruCache sPicassoCache;

    public static LruCache getPicassoCache() {
        if(sPicassoCache == null){
            sPicassoCache = new LruCache(1048576);// 1 mb
        }
        return sPicassoCache;
    }
}
