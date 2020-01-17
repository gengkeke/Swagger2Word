package org.word.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author gengkeke
 * @date 2020/1/17 9:59
 * @Description
 */
@Data
public class Parameter implements Serializable {
    private static final long serialVersionUID = 1L;
    // path 、body、query
    private String in;
    // string
    private String type;
    private String name;
    private String description;
    private Boolean required;
    private Schema schema;
    private String format;
    @JsonProperty("default")
    private String example;
}
