package org.word.model.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author gengkeke
 * @date 2020/1/17 13:42
 * @Description
 */
@Data
public class ContentItem implements Serializable {
    private static final long serialVersionUID = 1L;
    //title
    private String title;
    // 基本信息
    private Detail detail;
    // 请求参数
    private List<DefinitionParam> requestParams;
    //状态码
    private List<HttpStatus> httpStatuses;
    //响应参数
    private List<DefinitionParam> responseParams;
    //请求示例
    private String requestExample = "{\t\n}";
    //响应示例
    private String responseExample = "{\t\n}";

}
