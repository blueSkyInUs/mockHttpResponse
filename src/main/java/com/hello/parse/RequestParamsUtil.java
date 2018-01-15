package com.hello.parse;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lesson
 * @date 2018/1/12 12:24
 */
@Component
public class RequestParamsUtil {
    @SneakyThrows
    public Map<String,String> findAllRequestParams(HttpRequest httpRequest){
        Map<String,String> paramsMap= new HashMap<>();
                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(httpRequest);
        List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();

        for (InterfaceHttpData parm : parmList) {
            Attribute data = (Attribute) parm;
            //TODO 数组怎么处理
            paramsMap.put(data.getName(), data.getValue());
        }
        return paramsMap;
    }
}
