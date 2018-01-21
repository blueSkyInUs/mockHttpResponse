package com.hello.netty;

import com.hello.handle.DynamicResourceHandle;
import com.hello.handle.StaticResourceHandle;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lesson
 * @date 2018/1/5 16:13
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class HttpChannelHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private StaticResourceHandle staticResourceHandle;
    @Autowired
    private DynamicResourceHandle dynamicResourceHandle;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        HttpRequest request ;
         FullHttpResponse response ;
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
            if (isDynamic(request.uri())){
                response=dynamicResourceHandle.handle(request);
            }else{
                response=staticResourceHandle.handle(request);
            }
            ctx.write(response);
        }

    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("server channelReadComplete..");
        ctx.flush();
        ctx.close();

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        log.error(cause.getMessage(),cause);
        ctx.close();
    }


    public boolean isDynamic(String url) {
        return !url.startsWith("/static");
    }
}
