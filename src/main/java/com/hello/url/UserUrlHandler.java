package com.hello.url;

import com.hello.dao.XMLDao;
import com.hello.domain.RequestMetaInfo;
import com.hello.domain.XMLDataBase;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author lesson
 * @date 2018/1/9 18:14
 */
@Component
@Order(2)
@Slf4j
public class UserUrlHandler implements  UrlHandler {
    @Autowired
    private XMLDao xmlDao;

    @Override
    public boolean accept(String urlTag) {
        return !"admin".equals(urlTag);
    }

    @Override
    public void handle(HttpRequest request ) {
        String url=request.uri();
        RequestMetaInfo requestMetaInfo=xmlDao.getRequestMetaInfoByUrl(url);
        if (Objects.isNull(requestMetaInfo)){
            log.info("url:{} no request MetaInfo find",url);
            throw  new RuntimeException("url: "+url+"no request MetaInfo find");
        }
        requestMetaInfo.check(request);



    }

}
