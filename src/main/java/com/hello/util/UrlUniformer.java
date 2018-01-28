package com.hello.util;

public class UrlUniformer {
    public static String adjustUrl(String url){
        if (!url.startsWith("/")){
            return "/"+url;
        }
        return url;
    }
}
