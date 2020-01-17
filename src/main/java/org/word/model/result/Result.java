package org.word.model.result;

import lombok.Data;
import org.word.model.Info;

import java.io.Serializable;
import java.util.List;

/**
 * @author gengkeke
 * @date 2020/1/17 13:42
 * @Description 页面返回值
 */
@Data
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;
    private Info info;
    private List<Content> contents;
}
