package org.word.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gengkeke
 * @date 2020/1/17 9:51
 * @Description
 */
@Data
public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String url;
}
