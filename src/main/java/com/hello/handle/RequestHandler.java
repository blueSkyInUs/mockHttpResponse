package com.hello.handle;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

public interface RequestHandler {
    boolean accept(String url);
    FullHttpResponse handle(HttpRequest request ) throws  Exception;
}
