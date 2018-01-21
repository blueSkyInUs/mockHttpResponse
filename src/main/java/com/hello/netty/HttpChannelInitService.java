package com.hello.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lesson
 * @date 2018/1/5 16:15
 */
@Component
public class HttpChannelInitService extends ChannelInitializer<SocketChannel> {

    @Autowired
    private HttpChannelHandler httpChannelHandler;
    @Override
    protected void initChannel(SocketChannel sc)
            throws Exception {
        sc.pipeline().addLast(new HttpRequestDecoder());
        sc.pipeline().addLast(new HttpResponseEncoder());
        sc.pipeline().addLast(new HttpObjectAggregator(1048576));
        sc.pipeline().addLast(httpChannelHandler);

    }

}
