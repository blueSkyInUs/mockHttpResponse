package com.hello.url;

import com.hello.dao.XMLDao;
import com.hello.url.request.BizController;
import com.hello.url.request.ControllerManagerUtil;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author lesson
 * @date 2018/1/9 18:12
 */
@Component
@Order(1)
@Slf4j
public class AdminUrlHandler implements  UrlHandler {

    private static final String ADMIN_PREFIX="admin";

    @Autowired
    private XMLDao xmlDao;

    @Autowired
    private ControllerManagerUtil controllerManagerUtil;

    @Override
    public boolean accept(String urlTag) {
        return urlTag.contains("admin");
    }

    /**
     *  rest 风格url
     * @param request
     */
    @Override
    public String handle(HttpRequest request ) {
        BizController bizController=controllerManagerUtil.findBizControllerByUrl(request.uri().replaceFirst("/"+ADMIN_PREFIX,""));
        return bizController.render(request);
    }


}
