package com.hello.domain;

import com.hello.constant.HttpMethod;
import com.hello.constant.RequestContentType;
import com.hello.constant.ResponseContentType;
import lombok.Data;
import lombok.SneakyThrows;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author lesson
 * @date 2018/1/9 18:23
 */
@Data
@XmlRootElement
public class RequestMetaInfo {
    private HttpMethod httpMethod;
    private String url;
    private String preRequestScript; //支持javascript脚本对请求响应 做逻辑处理
    private RequestContentType requestContentType=RequestContentType.BODY;
    private String preResponseScript;
    private ResponseContentType responseContentType;
    private String responseContent;  //统一用字符存储 根据responseContentType作不同处理

    @SneakyThrows
    public static void main(String[] args) {
        JAXBContext context = JAXBContext.newInstance(RequestMetaInfo.class);
        Marshaller marshaller = context.createMarshaller();
        StringWriter stringWriter=new StringWriter();
        marshaller.marshal(new RequestMetaInfo(), stringWriter);
        System.out.println(stringWriter.getBuffer().toString());

    }
}
