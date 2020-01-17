package org.word.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author gengkeke
 * @date 2020/1/17 10:00
 * @Description
 */
@Data
public class Schema implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("$ref")
    private String ref;
    private String type;
    //type 为 array 有
    private Items items;
}
