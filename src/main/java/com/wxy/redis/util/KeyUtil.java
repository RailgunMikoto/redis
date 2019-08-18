package com.wxy.redis.util;

public class KeyUtil {

    public static String getKey(String prefix, String suffix){
        return prefix+"_"+suffix;
    }

}
