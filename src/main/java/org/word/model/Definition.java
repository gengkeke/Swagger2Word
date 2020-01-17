package org.word.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author gengkeke
 * @date 2020/1/16 18:36
 * @Description definitions
 */
@Data
public class Definition implements Serializable {
    private static final long serialVersionUID = 1L;
    private String type;
    private Map<String, Property> properties;
    private Property additionalProperties;
    private List<String> required;
    private String description;
}
