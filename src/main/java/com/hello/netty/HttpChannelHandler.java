package com.hello.netty;

import com.hello.handle.RequestHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.sun.deploy.net.HttpRequest.CONTENT_LENGTH;
import static com.sun.deploy.net.HttpRequest.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author lesson
 * @date 2018/1/5 16:13
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class HttpChannelHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    private List<RequestHandler> requestHandlers;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
         HttpRequest request ;
         FullHttpResponse response ;
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;
            String uri = request.uri();
            response=requestHandlers.stream().filter(requestHandler -> requestHandler.accept(uri)).findFirst().get().handle(request);
            ctx.write(response);
        }

        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            buf.release();
            String res = "I am OK";
             response = new DefaultFullHttpResponse(HTTP_1_1,
                     HttpResponseStatus.OK, Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().set(CONTENT_LENGTH,
                    response.content().readableBytes());
            ctx.write(response);
            ctx.flush();
            ctx.close();

        }

    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("server channelReadComplete..");
        ctx.flush();//刷新后才将数据发出到SocketChannel

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        log.error(cause.getMessage(),cause);
        ctx.close();
    }
}
