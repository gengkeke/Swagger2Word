package org.word.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gengkeke
 * @date 2020/1/17 11:03
 * @Description
 */
@Data
public class ResponseInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String description;
    private Schema schema;
}
