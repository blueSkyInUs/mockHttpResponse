package com.hello.convert;

import com.alibaba.fastjson.JSONObject;
import com.hello.domain.RequestMetaInfo;
import com.hello.parse.RequestParamsUtil;
import io.netty.handler.codec.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lesson
 * @date 2018/1/12 12:33
 */
@Component
public class RequestMetaInfoConverter {

    @Autowired
    private RequestParamsUtil requestParamsUtil;

    public RequestMetaInfo convertRequestMetaInfo(HttpRequest httpRequest){
        Map<String,Object> map= requestParamsUtil.findAllRequestParams(httpRequest);
        return JSONObject.parseObject(new JSONObject(map).toString(),RequestMetaInfo.class);
    }
}
