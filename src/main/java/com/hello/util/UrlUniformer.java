package com.hello.util;

import org.springframework.stereotype.Component;

@Component
public class UrlUniformer {
    public String adjustUrl(String url){
        if (!url.startsWith("/")){
            return "/"+url;
        }
        return url;
    }
}
