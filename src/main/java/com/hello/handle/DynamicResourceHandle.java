package com.hello.handle;

import com.hello.url.AdminUrlHandler;
import com.hello.url.UserUrlHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author lesson
 * @date 2018/1/8 16:47
 */
@Component
@Order(1)
@Slf4j
public class DynamicResourceHandle  {

    @Autowired
    AdminUrlHandler adminUrlHandler;

    @Autowired
    UserUrlHandler userUrlHandler;

    public DefaultFullHttpResponse handle(HttpRequest request) throws  Exception {

        DefaultFullHttpResponse response;
       String url=request.uri();
       if (url.startsWith("/admin")){
           response=adminUrlHandler.handle(request);
       }else{
           response=userUrlHandler.handle(request);
       }
        return response;

    }
}
