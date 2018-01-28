package com.hello.exception;

public class ResourceFobiddenAccessException extends BaseException {
    public ResourceFobiddenAccessException(){
        super("403","资源不允许创建或者访问");
    }
}
