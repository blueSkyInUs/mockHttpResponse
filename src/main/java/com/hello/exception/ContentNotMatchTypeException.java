package com.hello.exception;

public class ContentNotMatchTypeException extends BaseException {
    public ContentNotMatchTypeException(String msg){
        super("501",msg);
    }
}
