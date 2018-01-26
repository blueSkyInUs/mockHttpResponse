package com.hello.exception;

/**
 * @author lesson
 * @date 2018/1/25 20:09
 */
public class URLNotFoundException  extends  BaseException{

    public URLNotFoundException(){
        super("404","页面不存在");
    }

}
