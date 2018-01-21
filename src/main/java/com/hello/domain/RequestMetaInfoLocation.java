package com.hello.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestMetaInfoLocation {
    private int location;
    private RequestMetaInfo requestMetaInfo;
}
