package com.hello.domain;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hello.constant.HttpMethod;
import com.hello.constant.RequestContentType;
import com.hello.constant.ResponseContentType;
import com.hello.exception.ContentNotMatchTypeException;
import io.netty.handler.codec.http.HttpRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author lesson
 * @date 2018/1/9 18:23
 */
@Data
@Slf4j
public class RequestMetaInfo {
    private int id;
    private HttpMethod httpMethod;
    private String url;
    private String preRequestScript; //支持javascript脚本对请求 做逻辑处理
    private RequestContentType requestContentType=RequestContentType.BODY;
    private String preResponseScript;//支持javascript脚本对响应 做逻辑处理
    private ResponseContentType responseContentType=ResponseContentType.STRING;
    private String responseContent;  //统一用字符存储 根据responseContentType作不同处理

    public void check(HttpRequest request) {
        check(Objects.equals(request.method().name(),httpMethod.getMethod()));
        check(Objects.equals(request.uri(),url));
    }

    public void checkContentOk(){
        if (responseContentType==ResponseContentType.JSON_STRING){
            try{
                JSONObject jsonObject=JSONObject.parseObject(responseContent);
            }catch (JSONException jsonException){
                log.error(jsonException.getMessage(),jsonException);
                throw new ContentNotMatchTypeException("响应内容需要是json格式");
            }

        }
    }


    private void check(boolean result) {
        if (!result) throw new RuntimeException("not fit");
    }
}
