package com.hello.url.request;

import com.alibaba.fastjson.JSONArray;
import com.hello.result.ResponseResult;
import com.hello.result.ResponseType;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;

public interface BizController {
     String showMyAcceptUrl();
     ResponseResult<String> process(HttpRequest request);

     default String render(HttpRequest httpRequest){
          ResponseResult<String> result=process(httpRequest);
          if (result.getResponseType()== ResponseType.NOTIFY){
               return result.getData();
          }
          VelocityEngine velocityEngine=new VelocityEngine();
          velocityEngine.setProperty(Velocity.RESOURCE_LOADER,"class");
          velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
          velocityEngine.init();
          Template template=velocityEngine.getTemplate("template/requestMetaInfo.vm","utf-8");
          VelocityContext context = new VelocityContext();
          context.put("requestMetaInfos", JSONArray.parseArray(result.getData()));
          StringWriter stringWriter=new StringWriter();
          template.merge(context,stringWriter);
          return stringWriter.toString();

     }

}
