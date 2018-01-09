package com.hello.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author lesson
 * @date 2018/1/9 19:07
 */
@XmlRootElement
@Data
public class XMLDataBase {
    List<RequestMetaInfo> requestMetaInfos;
}
