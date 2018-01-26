package com.hello.url;

import com.alibaba.fastjson.JSONObject;
import com.hello.dao.XMLDao;
import com.hello.domain.RequestMetaInfo;
import com.hello.exception.DecryptException;
import com.hello.exception.URLNotFoundException;
import com.hello.javascript.JavaScriptEngine;
import com.hello.parse.RequestParamsUtil;
import io.netty.handler.codec.http.HttpRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;

/**
 * @author lesson
 * @date 2018/1/9 18:14
 */
@Component
@Slf4j
public class UserUrlHandler{
    @Autowired
    private XMLDao xmlDao;

    @Autowired
    private RequestParamsUtil requestParamsUtil ;

    @Autowired
    private JavaScriptEngine javaScriptEngine;

    private static final String PRE_HANDLE_REQUEST_PARAMS_FUNCTION_NAME="handleRequestParams";

    private static final String PRE_HANDLE_RESPONSE_CONTENT_FUNCTION_NAME="handleResponseParams";

    @SneakyThrows
    public String handle(HttpRequest request ) {
        String url=request.uri();
        RequestMetaInfo requestMetaInfo=xmlDao.findByUrl(url);
        if (Objects.isNull(requestMetaInfo)){
            //异常统一由上层捕获
            throw new URLNotFoundException();
        }
        String response=requestMetaInfo.getResponseContent();
        StringBuilder sb=new StringBuilder();
        try(BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("jsUtil/encrypt.js")))){
            String temp;
            while(  (temp=bufferedReader.readLine())!=null){
                sb.append(temp);
            }
        }
        String prefix=sb.toString();

        Map<String,Object> requestMap=requestParamsUtil.findAllRequestParams(request);
        if (StringUtils.isNotEmpty(requestMetaInfo.getPreRequestScript())){
            response=javaScriptEngine.invokerJSRequestScript(prefix+requestMetaInfo.getPreRequestScript(),PRE_HANDLE_REQUEST_PARAMS_FUNCTION_NAME,new JSONObject(requestMap));
            if ("-1".equals(response)){
                throw new DecryptException();
            }
        }
        if(StringUtils.isNotEmpty(requestMetaInfo.getPreResponseScript())){
            response=javaScriptEngine.invokerJSResponseScript(prefix+requestMetaInfo.getPreResponseScript(),PRE_HANDLE_RESPONSE_CONTENT_FUNCTION_NAME,JSONObject.parseObject(response),requestMetaInfo.getResponseContent());
        }
        return response;
    }
}
