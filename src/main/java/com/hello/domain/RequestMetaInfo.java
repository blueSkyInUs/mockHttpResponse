package com.hello.domain;

import com.hello.constant.HttpMethod;
import com.hello.constant.RequestContentType;
import com.hello.constant.ResponseContentType;
import io.netty.handler.codec.http.HttpRequest;
import lombok.Data;

import java.util.Objects;

/**
 * @author lesson
 * @date 2018/1/9 18:23
 */
@Data
public class RequestMetaInfo {
    private HttpMethod httpMethod;
    private String url;
    private String preRequestScript; //支持javascript脚本对请求 做逻辑处理
    private RequestContentType requestContentType=RequestContentType.BODY;
    private String preResponseScript;//支持javascript脚本对响应 做逻辑处理
    private ResponseContentType responseContentType;
    private String responseContent;  //统一用字符存储 根据responseContentType作不同处理

    public void check(HttpRequest request) {
        check(Objects.equals(request.method().name(),httpMethod.getMethod()));
        check(Objects.equals(request.uri(),url));
    }

    private void check(boolean result) {
        if (!result) throw new RuntimeException("not fit");
    }
}
