package com.hello.url.request;

import com.hello.result.ResponseResult;
import io.netty.handler.codec.http.HttpRequest;

public interface BizController {
     String showMyAcceptUrl();
     ResponseResult<String> process(HttpRequest request);
}
