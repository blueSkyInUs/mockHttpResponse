package com.hello.url;

import com.hello.dao.XMLDao;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author lesson
 * @date 2018/1/9 18:14
 */
@Component
@Order(2)
@Slf4j
public class UserUrlHandler{
    @Autowired
    private XMLDao xmlDao;

    public String handle(HttpRequest request ) {


      return null;

    }

}
