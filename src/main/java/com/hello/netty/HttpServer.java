package com.hello.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
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
        new Thread(this::bind).start();
    }
    @SneakyThrows
    public void bind()  {
            String port=System.getProperties().getProperty("project.port","7777");
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(httpChannelInitService);
            ChannelFuture f = b.bind(Integer.parseInt(port)).sync();
            log.info("listener port:{}",port);
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
