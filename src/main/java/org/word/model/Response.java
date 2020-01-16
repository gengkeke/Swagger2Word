package org.word.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Response implements Serializable{

    /**
     * 返回参数
     */
    private String description;

    /**
     * 参数名
     */
    private String name;

    /**
     * 备注
     */
    private String remark;
}
