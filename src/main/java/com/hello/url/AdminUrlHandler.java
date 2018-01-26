package com.hello.url;

import com.hello.dao.XMLDao;
import com.hello.url.request.BizController;
import com.hello.url.request.ControllerManagerUtil;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

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
    public String handle(HttpRequest request ) {
        BizController bizController=controllerManagerUtil.findBizControllerByUrl(request.uri().replaceFirst("/admin",""));
        return bizController.render(request);
    }



}
