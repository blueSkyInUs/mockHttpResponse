package com.hello.url;

import com.hello.dao.XMLDao;
import com.hello.domain.RequestMetaInfo;
import com.hello.url.request.BizController;
import com.hello.url.request.ControllerManagerUtil;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

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
        return ADMIN_PREFIX.equals(urlTag);
    }

    /**
     *  rest 风格url
     * @param request
     */
    @Override
    public void handle(HttpRequest request ) {
        String url=request.uri();
        RequestMetaInfo requestMetaInfo=xmlDao.getRequestMetaInfoByUrl(url);
        if (Objects.isNull(requestMetaInfo)){
            log.info("url:{} no request MetaInfo find",url);
            throw  new RuntimeException("url: "+url+"no request MetaInfo find");
        }
        requestMetaInfo.check(request);

        BizController bizController=controllerManagerUtil.findBizControllerByUrl(request.uri().replaceFirst("/"+ADMIN_PREFIX,""));
        bizController.process(request);


    }


}
