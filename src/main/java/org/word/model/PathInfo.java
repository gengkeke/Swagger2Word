package org.word.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author gengkeke
 * @date 2020/1/17 9:57
 * @Description
 */
@Data
public class PathInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<String> tags;
    private String summary;
    private String operationId;
    private List<String> consumes;
    private List<String> produces;
    private List<Parameter> parameters;
    private Map<String, ResponseInfo> responses;
}
