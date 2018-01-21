package com.hello.url.request;

import com.hello.result.ResponseResult;
import io.netty.handler.codec.http.HttpRequest;
import org.springframework.stereotype.Component;

/**
 * @author lesson
 * @date 2018/1/12 10:56
 */
@Component
public class DefaultController  implements  BizController{


    @Override
    public String showMyAcceptUrl() {
        return "*";
    }

    @Override
    public ResponseResult<String> process(HttpRequest request) {
        return null;
    }
}
