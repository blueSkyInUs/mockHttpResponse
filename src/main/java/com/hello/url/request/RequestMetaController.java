package com.hello.url.request;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hello.dao.XMLDao;
import com.hello.domain.AdminUrlMetaInfo;
import com.hello.domain.RequestMetaInfo;
import com.hello.domain.XMLDataBase;
import com.hello.parse.AdminUrlParser;
import com.hello.parse.RequestParamsUtil;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @author lesson
 * @date 2018/1/12 10:40
 */
@Component
public class RequestMetaController implements  BizController {

    @Autowired
    private AdminUrlParser adminUrlParser;
    private static final String ACCEPT_URL_PREFIX="requestmeta";

    @Autowired
    private XMLDao xmlDao;

    @Autowired
    private RequestParamsUtil requestParamsUtil;

    public String showMyAcceptUrl(){
        return ACCEPT_URL_PREFIX;
    }

    @Override
    public String process(HttpRequest request) {
        switch(request.method().name()){
            case "GET": getResource(request);break;
            case "POST":addResource(request);break;
            case "PUT":updateResource(request);break;
            case "DELETE":deleteResource(request);break;
        }
        return "";
    }

    private void deleteResource(HttpRequest request) {
        AdminUrlMetaInfo adminUrlMetaInfo=adminUrlParser.parse(request.uri());

    }

    private void updateResource(HttpRequest request) {
        AdminUrlMetaInfo adminUrlMetaInfo=adminUrlParser.parse(request.uri());
    }

    private String addResource(HttpRequest request) {
        Map<String,String>  map= requestParamsUtil.findAllRequestParams(request);


    }

    private String getResource(HttpRequest request) {
        AdminUrlMetaInfo adminUrlMetaInfo=adminUrlParser.parse(request.uri());
        if (Objects.isNull(adminUrlMetaInfo.getId())){
            return JSONArray.toJSONString(xmlDao.showAllMetaInfo());
        }else {
            return JSONObject.toJSONString(xmlDao.getRequestMetaInfoByUrl(request.uri()));
        }
    }

    public static void main(String[] args) {
        System.out.println(Integer.parseInt(null));
    }




}
