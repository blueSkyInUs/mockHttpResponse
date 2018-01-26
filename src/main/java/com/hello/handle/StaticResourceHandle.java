package com.hello.handle;

import com.hello.util.UrlUniformer;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author lesson
 * @date 2018/1/8 15:00
 */
@Component
public class StaticResourceHandle{

    @Autowired
    private UrlUniformer urlUniformer;

    private LinkedHashMap<String,byte[]> staticContents=new LinkedHashMap<String,byte[]>(100){
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            if (size()>200){
                return true;
            }
            return super.removeEldestEntry(eldest);
        }
    };

    public DefaultFullHttpResponse handle(HttpRequest request ) throws  Exception{
        String url=urlUniformer.adjustUrl(request.uri());
        String resourcePath=url.substring(1);
        byte[] content=staticContents.get(resourcePath);
        if (Objects.isNull(content)){
            StringBuilder sb=new StringBuilder();
            try(BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(resourcePath)))){
                String temp;
                while(  (temp=bufferedReader.readLine())!=null){
                    sb.append(temp);
                }
            }
            content=sb.toString().getBytes();
            staticContents.put(resourcePath,content);
        }
        return new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(content));


    }

}
