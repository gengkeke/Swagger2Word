package org.word.model.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gengkeke
 * @date 2020/1/17 14:18
 * @Description
 */
@Data
public class Detail implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String url;
    private String summary;
    private String consume;
    private String produce;
    private String requestMethod;
}
