package com.hello.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lesson
 * @date 2018/1/25 20:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseException extends  RuntimeException {
    private String errorCode;
    private String errorMsg;
}
