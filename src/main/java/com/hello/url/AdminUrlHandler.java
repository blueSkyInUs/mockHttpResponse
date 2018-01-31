package com.hello.url;

import com.hello.dao.XMLDao;
import com.hello.exception.ResourceFobiddenAccessException;
import com.hello.exception.URLNotFoundException;
import com.hello.url.request.BizController;
import com.hello.url.request.ControllerManagerUtil;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author lesson
 * @date 2018/1/9 18:12
 */
@Component
@Slf4j
public class AdminUrlHandler{


    @Autowired
    private XMLDao xmlDao;

    @Autowired
    private ControllerManagerUtil controllerManagerUtil;


    /**
     *  rest 风格url
     * @param request
     */
    public DefaultFullHttpResponse handle(HttpRequest request ) {
        BizController bizController=controllerManagerUtil.findBizControllerByUrl(request.uri().replaceFirst("/admin",""));
        if (Objects.isNull(bizController)){
            throw new URLNotFoundException();
        }
        return bizController.render(request);
    }



}
