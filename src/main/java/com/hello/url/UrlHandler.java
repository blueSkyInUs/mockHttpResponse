package com.hello.url;

import io.netty.handler.codec.http.HttpRequest;

/**
 * @author lesson
 * @date 2018/1/9 18:11
 */
public interface UrlHandler {
    boolean accept(String urlTag);
    String handle(HttpRequest request );
}
