package org.word.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author gengkeke
 * @date 2020/1/17 11:00
 * @Description
 */
@Data
public class Response implements Serializable {
    private static final long serialVersionUID = 1L;
    private ResponseInfo ok;
    @JsonProperty("201")
    private ResponseInfo created;
    @JsonProperty("401")
    private ResponseInfo unauthorized;
    @JsonProperty("403")
    private ResponseInfo forbidden;
    @JsonProperty("404")
    private ResponseInfo notFound;
}
