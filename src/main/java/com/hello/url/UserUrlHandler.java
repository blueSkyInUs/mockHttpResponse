package com.hello.url;

import io.netty.handler.codec.http.HttpRequest;

/**
 * @author lesson
 * @date 2018/1/9 18:14
 */
public class UserUrlHandler implements  UrlHandler {
    @Override
    public boolean accept(String urlTag) {
        return !"admin".equals(urlTag);
    }

    @Override
    public void handle(HttpRequest request ) {

    }
}
