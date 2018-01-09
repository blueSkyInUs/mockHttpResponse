package com.hello.constant;

public enum HttpMethod {
    POST("post"),GET("get"),PUT("put"),DELETE("delete"),PATCH("patch");
    private String method;
    HttpMethod(String method){
        this.method=method;
    }
    public String getMethod(){
        return method;
    }
}
