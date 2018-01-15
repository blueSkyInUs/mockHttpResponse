package com.hello.dao;

import com.hello.domain.RequestMetaInfo;
import com.hello.domain.XMLDataBase;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lesson
 * @date 2018/1/10 18:33
 */
@Component
public class XMLDao  {

    private Map<String,RequestMetaInfo>  requestMetaInfoMap=new ConcurrentHashMap<>();

    private XMLDataBase xmlDataBase;

    @PostConstruct
    @SneakyThrows
    public void init(){
        Resource resource=new ClassPathResource("db.xml");
        JAXBContext context = JAXBContext.newInstance(XMLDataBase.class);
        Unmarshaller unmarshaller=context.createUnmarshaller();
        xmlDataBase= (XMLDataBase) unmarshaller.unmarshal(resource.getInputStream());
        requestMetaInfoMap.clear();
        xmlDataBase.getRequestMetaInfos().stream().forEach(this::copyIntoMap);
    }

    private void copyIntoMap(RequestMetaInfo requestMetaInfo){
        requestMetaInfoMap.put(requestMetaInfo.getUrl(),requestMetaInfo);
    }

    public RequestMetaInfo getRequestMetaInfoByUrl(String url){
        return requestMetaInfoMap.get(url);
    }

    public void reload(){
        init();
    }

    @SneakyThrows
    public void addMeta(RequestMetaInfo requestMetaInfo){
        if (Objects.nonNull(requestMetaInfoMap.get(requestMetaInfo.getUrl()))){
            throw new RuntimeException("url already exist...");
        }
        requestMetaInfoMap.put(requestMetaInfo.getUrl(),requestMetaInfo);
        synchronized (this){
            xmlDataBase.getRequestMetaInfos().add(requestMetaInfo);
            JAXBContext context = JAXBContext.newInstance(XMLDataBase.class);
            String path=this.getClass().getClassLoader().getResource("db.xml").getPath();
            try(FileWriter fileWriter=new FileWriter(path)){
                context.createMarshaller().marshal(xmlDataBase,fileWriter);
            }
        }
    }

    public List<RequestMetaInfo> showAllMetaInfo(){
        return Collections.unmodifiableList(xmlDataBase.getRequestMetaInfos())
    }
}
