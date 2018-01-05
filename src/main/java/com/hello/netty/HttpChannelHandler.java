package com.hello.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        System.out.println(ctx);
        System.out.println(msg);
         HttpRequest request ;
         FullHttpResponse response ;
        if (msg instanceof HttpRequest) {
            log.info("http");
            request = (HttpRequest) msg;
            String uri = request.uri();
            String res = "";
             response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
            ctx.write(response);
        }

        if (msg instanceof HttpContent) {
            log.info("msg");
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
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
        log.info("server exceptionCaught..");
        ctx.close();
    }
}
