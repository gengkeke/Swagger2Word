package org.word.model.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gengkeke
 * @date 2020/1/17 14:22
 * @Description
 */
@Data
public class DefinitionParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String type;
    private String format;
    private Object example;
    private String description;
    private Boolean required = false;
}
