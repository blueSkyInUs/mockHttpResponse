package com.hello.result;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result<T> {
    private ResponseType responseType;
    private String status;
    private String errorMsg;
    private T data;
    public Result(ResponseType responseType,T data){
        this(responseType,"200","",data);
    }
    public String toString(){
        return JSONObject.toJSONString(this);
    }
}
