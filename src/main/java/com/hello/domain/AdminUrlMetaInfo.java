package com.hello.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lesson
 * @date 2018/1/12 11:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminUrlMetaInfo {
    private String prefix;
    private String resource;
    private Integer id;

}
