package com.hello.url.request;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hello.convert.RequestMetaInfoConverter;
import com.hello.dao.XMLDao;
import com.hello.domain.AdminUrlMetaInfo;
import com.hello.domain.RequestMetaInfo;
import com.hello.parse.AdminUrlParser;
import com.hello.result.ResponseResult;
import com.hello.result.ResponseType;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private RequestMetaInfoConverter requestMetaInfoConverter;

    private ThreadLocal<ResponseType> responseTypeThreadLocal=new ThreadLocal<>();



    public String showMyAcceptUrl(){
        return ACCEPT_URL_PREFIX;
    }

    @Override
    public ResponseResult<String> process(HttpRequest request) {

        VelocityEngine velocityEngine=new VelocityEngine();
        switch(request.method().name()){
            case "GET": return getResource(request);
            case "POST":return addResource(request);
            case "PUT":return updateResource(request);
            case "DELETE":return deleteResource(request);
        }
        return null;
    }

    /**
     * 主要需求应该是列表和添加  那么用List就比map好一些
     * @param request
     */
    private ResponseResult<String> deleteResource(HttpRequest request) {
        responseTypeThreadLocal.set(ResponseType.NOTIFY);
        AdminUrlMetaInfo adminUrlMetaInfo=adminUrlParser.parse(request.uri());
        xmlDao.deleteById(adminUrlMetaInfo.getId());
        return new ResponseResult<>(ResponseType.NOTIFY,"");
    }

    private ResponseResult<String> updateResource(HttpRequest request) {
        responseTypeThreadLocal.set(ResponseType.NOTIFY);
        AdminUrlMetaInfo adminUrlMetaInfo=adminUrlParser.parse(request.uri());
        RequestMetaInfo requestMetaInfo=requestMetaInfoConverter.convertRequestMetaInfo(request);
        xmlDao.updateById(adminUrlMetaInfo.getId(),requestMetaInfo);
        return new ResponseResult<>(ResponseType.NOTIFY,"");
    }

    private ResponseResult<String> addResource(HttpRequest request) {
        responseTypeThreadLocal.set(ResponseType.NOTIFY);
        RequestMetaInfo requestMetaInfo=requestMetaInfoConverter.convertRequestMetaInfo(request);
        xmlDao.addMeta(requestMetaInfo);
        return new ResponseResult<>(ResponseType.NOTIFY,"");
    }

    private ResponseResult<String> getResource(HttpRequest request) {
        responseTypeThreadLocal.set(ResponseType.CONTENT);
        AdminUrlMetaInfo adminUrlMetaInfo=adminUrlParser.parse(request.uri());
        if (Objects.isNull(adminUrlMetaInfo.getId())){
            return new ResponseResult<>(ResponseType.CONTENT,JSONArray.toJSONString(xmlDao.showAllMetaInfo()));
        }else {
            return new ResponseResult<>(ResponseType.CONTENT,JSONObject.toJSONString(xmlDao.findById(adminUrlMetaInfo.getId())));
        }
    }

    public static void main(String[] args) {
        System.out.println(Integer.parseInt(null));
    }




}
