package com.hello.handle;

import com.hello.url.AdminUrlHandler;
import com.hello.url.UserUrlHandler;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

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

       String response;
       String url=request.uri();
       if (url.startsWith("/admin")){
           response=adminUrlHandler.handle(request);
       }else{
           response=userUrlHandler.handle(request);
       }
        return new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(response.getBytes("UTF-8")));

    }
}
