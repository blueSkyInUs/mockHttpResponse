package com.hello.url.request;

import com.alibaba.fastjson.JSONArray;
import com.hello.domain.AdminUrlMetaInfo;
import com.hello.parse.AdminUrlParser;
import com.hello.result.ResponseType;
import com.hello.result.Result;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.SneakyThrows;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public interface BizController {
     String showMyAcceptUrl();
     Result<String> process(HttpRequest request);

     @SneakyThrows(UnsupportedEncodingException.class)
     default DefaultFullHttpResponse render(HttpRequest httpRequest){
          Result<String> result=process(httpRequest);
          if (result.getResponseType()== ResponseType.NOTIFY){
               return new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(result.toString().getBytes("UTF-8")));
          }
          VelocityEngine velocityEngine=new VelocityEngine();
          velocityEngine.setProperty(Velocity.RESOURCE_LOADER,"class");
          velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
          velocityEngine.init();
          AdminUrlMetaInfo adminUrlMetaInfo= AdminUrlParser.parse(httpRequest.uri());
          String templateFile= Objects.isNull(adminUrlMetaInfo.getId())?"template/requestMetaInfoList.vm":"template/requestMetaInfoDetail.vm";
          Template template=velocityEngine.getTemplate(templateFile,"utf-8");
          VelocityContext context = new VelocityContext();
          context.put("requestMetaInfos", JSONArray.parseArray(result.getData()));
          StringWriter stringWriter=new StringWriter();
          template.merge(context,stringWriter);
          return new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(stringWriter.toString().getBytes("UTF-8")));

     }

}
