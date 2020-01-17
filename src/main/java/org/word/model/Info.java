package org.word.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gengkeke
 * @date 2020/1/17 9:48
 * @Description
 */
@Data
public class Info implements Serializable {
    private static final long serialVersionUID = 1L;
    private String description;
    private String version;
    private String title;
    private String termsOfService;
    private Contact contact;




}
