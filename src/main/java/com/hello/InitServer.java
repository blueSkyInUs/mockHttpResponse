package com.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author lesson
 * @date 2018/1/5 15:20
 */

@ComponentScan(basePackages = "com.hello")
public class InitServer {
    public static void main(String[] args) {
        SpringApplication springApplication=new SpringApplication(InitServer.class);
        springApplication.run(args);
    }
}
