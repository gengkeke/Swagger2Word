package org.word.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gengkeke
 * @date 2020/1/17 11:00
 * @Description
 */
@Data
public class RequestType implements Serializable {
    private static final long serialVersionUID = 1L;
    private PathInfo get;
    private PathInfo head;
    private PathInfo post;
    private PathInfo put;
    private PathInfo delete;
    private PathInfo options;
    private PathInfo patch;
}
