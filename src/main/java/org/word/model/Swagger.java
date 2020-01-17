package org.word.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author gengkeke
 * @date 2020/1/17 9:46
 * @Description
 */
@Data
public class Swagger implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("swagger")
    private String version;
    private Info info;
    private String host;
    private String basePath;
    private List<Tag> tags;
    private Map<String, Map<String, PathInfo>> paths;
    private Map<String, Definition> definitions;


}
