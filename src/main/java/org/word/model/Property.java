package org.word.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author gengkeke
 * @date 2020/1/16 18:38
 * @Description
 */
@Data
public class Property implements Serializable {
    private static final long serialVersionUID = 1L;
    // string  integer array
    private String type;
    // int32
    private String format;
    private Object example;
    // value
    private String description;
    //type 为 array 有 "$ref": "#/definitions/CatalogTreeDO"
    private Items items;
    //Result«Map«string,string»»
    private Property additionalProperties;

    private boolean readOnly;

    @JsonProperty("enum")
    private List<Object> enumValue;

    @JsonProperty("$ref")
    private String ref;


}
