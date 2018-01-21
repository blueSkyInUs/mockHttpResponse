package com.hello.domain;

import com.hello.constant.HttpMethod;
import lombok.Data;
import lombok.SneakyThrows;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lesson
 * @date 2018/1/9 19:07
 */
@XmlRootElement
@Data
@XmlAccessorType(XmlAccessType.NONE)
public class XMLDataBase {
    @XmlElementWrapper(name="requestMetaInfos")
    @XmlElement(name = "requestMetaInfo")
    List<RequestMetaInfo> requestMetaInfos=new ArrayList<>();


    @SneakyThrows
    public static void main(String[] args) {
        JAXBContext context = JAXBContext.newInstance(XMLDataBase.class);
        Marshaller marshaller = context.createMarshaller();
        StringWriter stringWriter=new StringWriter();
        XMLDataBase xmlDataBase=new XMLDataBase();
        RequestMetaInfo requestMetaInfo=new RequestMetaInfo();
        requestMetaInfo.setId(100);
        requestMetaInfo.setHttpMethod(HttpMethod.GET);
        requestMetaInfo.setUrl("/lesson");
        xmlDataBase.getRequestMetaInfos().add(requestMetaInfo);
        marshaller.marshal(xmlDataBase,stringWriter);
        System.out.println(stringWriter.getBuffer().toString());

        Unmarshaller unmarshaller=context.createUnmarshaller();
        XMLDataBase origin= (XMLDataBase) unmarshaller.unmarshal(new StringReader(stringWriter.getBuffer().toString()));
        System.out.println(origin.getRequestMetaInfos().size());


    }

}
