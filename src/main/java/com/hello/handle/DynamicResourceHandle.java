package com.hello.handle;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.SneakyThrows;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author lesson
 * @date 2018/1/8 16:47
 */
@Component
@Order(1)
public class DynamicResourceHandle implements RequestHandler {
    @Override
    public boolean accept(String url) {
        Pattern pattern = Pattern.compile(".+/(.+)\\?(.+)");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.matches()){
            pattern=Pattern.compile(".+/(.+)");
            matcher=pattern.matcher(url);
            if (!matcher.matches()){
                return false;
            }
        }
        String urlEnd=matcher.group(1);
        return !(urlEnd.endsWith(".jpg")||urlEnd.endsWith(".png")||urlEnd.endsWith(".ico"));
    }

    @Override
    public DefaultFullHttpResponse handle(HttpRequest request) throws  Exception {
           return new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer("hello".getBytes("UTF-8")));
    }
}
