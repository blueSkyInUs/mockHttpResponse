package com.hello.parse;

import com.hello.domain.AdminUrlMetaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lesson
 * @date 2018/1/12 11:11
 */

@Component
public class AdminUrlParser {

    public static AdminUrlMetaInfo parse(String url){
       String[] urlElement=url.trim().split("/");
       if (urlElement.length==3){
           return new AdminUrlMetaInfo(urlElement[1],urlElement[2],null);
       }else{
           return new AdminUrlMetaInfo(urlElement[1],urlElement[2],Integer.parseInt(urlElement[3]));
       }
    }

    public static void main(String[] args) {
        System.out.println(parse("/admin/requestMetaInfo/1"));
    }
}
