package com.hello.constant;

public enum ResponseContentType {
    HTML("html"),STRING("string"),JSON_STRING("json_string"),XML("xml");
    private String responseType;
    ResponseContentType(String responseType){
        this.responseType=responseType;
    }
    public String getResponseType(){
        return responseType;
    }
}
