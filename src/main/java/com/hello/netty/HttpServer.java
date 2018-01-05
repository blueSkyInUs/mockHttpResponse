package com.hello.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.*;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author lesson
 * @date 2018/1/5 16:15
 */
@Component
@Slf4j
public class HttpServer implements ApplicationListener {

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    @Autowired
    private HttpChannelInitService httpChannelInitService;

    @PostConstruct
    public void init(){
        log.info("777");
        new Thread(this::bind).start();
    }
    @SneakyThrows
    public void bind()  {
            int port=7777;
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(httpChannelInitService).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
    }


    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof ContextClosedEvent){
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
