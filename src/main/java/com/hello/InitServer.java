package com.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author lesson
 * @date 2018/1/5 15:20
 */

@ComponentScan
@Slf4j
public class InitServer {
    public static void main(String[] args) {
        log.info("hello");
        SpringApplication springApplication=new SpringApplication(InitServer.class);
        springApplication.run(args);
    }
}
