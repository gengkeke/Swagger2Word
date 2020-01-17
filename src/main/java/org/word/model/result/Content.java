package org.word.model.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author gengkeke
 * @date 2020/1/17 13:42
 * @Description Tag
 */
@Data
public class Content implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private List<ContentItem> contentItems;
}
