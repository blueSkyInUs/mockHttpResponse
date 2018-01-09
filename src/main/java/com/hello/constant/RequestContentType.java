package com.hello.constant;

/**
 * @author lesson
 * @date 2018/1/9 18:31
 */
public enum  RequestContentType {
    BODY("body"),FORM("form"),URL_ATTACH("url_attach");
    private String type;
    RequestContentType(String type){
        this.type=type;
    }
    public String getType(){
        return type;
    }
}
