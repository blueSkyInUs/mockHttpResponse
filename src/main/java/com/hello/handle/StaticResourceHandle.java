package com.hello.handle;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lesson
 * @date 2018/1/8 15:00
 */
@Component
@Order(2)
public class StaticResourceHandle  implements  RequestHandler{

    @Override
    public boolean accept(String url) {
        //说明动态请求资源控制器不支持 那么只能交由静态资源来处理
        return true;
    }

    @Override
    public DefaultFullHttpResponse handle(HttpRequest request ) throws  Exception{
                return  null;
    }

}
