package com.hello.netty;

import com.hello.exception.BaseException;
import com.hello.handle.DynamicResourceHandle;
import com.hello.handle.StaticResourceHandle;
import com.hello.result.ResponseType;
import com.hello.result.Result;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
            if (request.uri().equals("/")){
                response= new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.FOUND, Unpooled.wrappedBuffer("".getBytes("UTF-8")));
                response.headers().add("Location","/admin/requestmeta/");
                ctx.write(response);
                return;
            }
            try{
                if (isDynamic(request.uri())){
                    response=dynamicResourceHandle.handle(request);
                }else{
                    response=staticResourceHandle.handle(request);
                }
            }catch(BaseException baseException){
                Result result=new Result(ResponseType.NOTIFY,baseException.getErrorCode(),baseException.getErrorMsg(),"");
                response= new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(result.toString().getBytes("UTF-8")));
            }catch(Exception exception){
                exception.printStackTrace();
                Result result=new Result(ResponseType.NOTIFY,"500","系统异常","");
                response= new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(result.toString().getBytes("UTF-8")));
            }
            ctx.write(response);
        }




    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
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
