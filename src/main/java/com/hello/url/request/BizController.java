package com.hello.url.request;

import io.netty.handler.codec.http.HttpRequest;

public interface BizController {
     String showMyAcceptUrl();
     String process(HttpRequest request);
}
