package com.hello.handle;

import com.hello.url.UrlHandler;
import com.oracle.tools.packager.Log;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
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

    @Autowired
    List<UrlHandler> urlHandlers;
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

       String response= urlHandlers.stream().filter(urlHandler -> urlHandler.accept(request.uri())).findFirst().get().handle(request);
        Log.info(response);
        return new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(response.getBytes("UTF-8")));

    }
}
