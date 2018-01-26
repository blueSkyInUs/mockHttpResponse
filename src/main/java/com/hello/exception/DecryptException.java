package com.hello.exception;


/**
 * @author lesson
 * @date 2018/1/26 14:45
 */
public class DecryptException  extends  BaseException{
    public DecryptException(){
        super("400","加密错误");
    }
}
