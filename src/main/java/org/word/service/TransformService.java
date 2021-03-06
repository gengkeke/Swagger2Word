package org.word.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.word.model.*;
import org.word.model.result.*;
import org.word.utils.JsonUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gengkeke
 * @date 2020/1/17 10:04
 * @Description
 */
@Slf4j
@Service
@SuppressWarnings({"unchecked"})
public class TransformService {
    @Autowired
    private RestTemplate restTemplate;

    public Result transform(String url) throws IOException {
        Result result = new Result();
        String jsonStr = restTemplate.getForObject(url, String.class);
        Swagger swagger = JsonUtils.readValue(jsonStr, Swagger.class);
        String basePath = swagger.getBasePath();
        result.setInfo(swagger.getInfo());
        List<Tag> tags = swagger.getTags();
        Map<String, Map<String, PathInfo>> paths = swagger.getPaths();
        Map<String, Definition> definitions = swagger.getDefinitions();
        List<Content> contents = tags.stream().map(x -> {
            Content content = new Content();
            content.setTitle(x.getName());
            Set<String> includePath = new HashSet<>();
            paths.forEach((k, v) ->
                    v.forEach((m, n) -> {
                        if (n.getTags().contains(x.getName())) {
                            includePath.add(k);
                        }
                    })
            );

            List<ContentItem> contentItems = includePath.stream().map(y -> {
                ContentItem contentItem = new ContentItem();
                Detail detail = new Detail();
                List<String> methods = new ArrayList<>();
                Map<String, PathInfo> pathInfoMap = paths.get(y);
                pathInfoMap.forEach((k, v) -> methods.add(k.toUpperCase()));
                Map.Entry<String, PathInfo> entry = pathInfoMap.entrySet().iterator().next();
                PathInfo pathInfo = entry.getValue();
                contentItem.setTitle(pathInfo.getSummary());
                detail.setTitle(pathInfo.getSummary());
                detail.setUrl(basePath + y);
                detail.setSummary(pathInfo.getSummary());
                if (null != pathInfo.getConsumes()) {
                    detail.setConsume(String.join(",", pathInfo.getConsumes()));
                }
                if (null != pathInfo.getProduces()) {
                    detail.setProduce(String.join(",", pathInfo.getProduces()));
                }
                detail.setRequestMethod(String.join(",", methods));
                contentItem.setDetail(detail);

                List<Parameter> parameters = pathInfo.getParameters();
                Map<String, Object> requestMap = new HashMap<>(8);
                if (null != parameters) {
                    List<DefinitionParam> requestParams = parameters.stream().map(p -> {
                        List<DefinitionParam> params = new ArrayList<>();
                        if ("body".equalsIgnoreCase(p.getIn()) && null != p.getSchema() && null != p.getSchema().getRef()) {
                            Schema schema = p.getSchema();
                            params.addAll(getDefinitionParamsByRef(schema.getRef(), definitions));
                            try {
                                contentItem.setRequestExample(JsonUtils.writeJsonStr(toJsonStr(schema.getRef(), definitions)));
                            } catch (JsonProcessingException e) {
                                log.error(e.getMessage(), e);
                            }
                        } else {
                            DefinitionParam definitionParam = new DefinitionParam();
                            definitionParam.setType(p.getType());
                            definitionParam.setFormat(p.getFormat());
                            definitionParam.setExample(p.getExample());
                            definitionParam.setDescription(p.getDescription());
                            definitionParam.setRequired(p.getRequired() != null ? p.getRequired() : false);
                            definitionParam.setName(p.getName());
                            if (null != p.getSchema() && "array".equals(p.getSchema().getType())) {
                                Schema schema = p.getSchema();
                                definitionParam.setType(schema.getType());
                                definitionParam.setFormat(schema.getItems().getFormat());

                            }
                            params.add(definitionParam);
                            requestMap.put(p.getName(), p.getExample() != null ? p.getExample() : getDefaultValue(p.getType()));
                        }
                        return params;
                    }).flatMap(Collection::stream).collect(Collectors.toList());
                    contentItem.setRequestParams(requestParams);
                    if (!requestMap.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        requestMap.forEach((k, v) -> {
                            if (v == null || "".equals(v)) {
                                v = "\"\"";
                            }
                            sb.append(k);
                            sb.append("=");
                            sb.append(v);
                            sb.append("&");
                        });
                        String s = sb.toString();
                        if (s.endsWith("&")) {
                            s = s.substring(0, s.length() - 1);
                        }
                        contentItem.setRequestExample1(s);
                    }
                }
                Map<String, ResponseInfo> responses = pathInfo.getResponses();
                ResponseInfo responseInfo = responses.get("200");
                if (null != responseInfo) {
                    Schema schema = responseInfo.getSchema();
                    if (null != schema && StringUtils.isNotBlank(schema.getRef())) {
                        contentItem.setResponseParams(getDefinitionParamsByRef(schema.getRef(), definitions));
                        try {
                            contentItem.setResponseExample(JsonUtils.writeJsonStr(toJsonStr(schema.getRef(), definitions)));
                        } catch (JsonProcessingException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
                return contentItem;
            }).collect(Collectors.toList());
            content.setContentItems(contentItems);
            return content;
        }).collect(Collectors.toList());
        result.setContents(contents);
        return result;
    }

    private List<DefinitionParam> getDefinitionParamsByRef(String ref, Map<String, Definition> definitions) {
        if (ref.startsWith("#")) {
            ref = ref.substring(ref.lastIndexOf("/") + 1);
        }
        Definition definition = definitions.get(ref);
        List<DefinitionParam> list = new ArrayList<>();
        Map<String, Property> properties = definition.getProperties();
        if ("object".equalsIgnoreCase(definition.getType()) && null != properties && !properties.isEmpty()) {
            String finalRef = ref;
            properties.forEach((k, v) -> {
                if ("array".equals(v.getType())) {
                    Items items = v.getItems();
                    if (StringUtils.isNotBlank(items.getRef())) {
                        String itemsRef = items.getRef();
                        itemsRef = itemsRef.substring(itemsRef.lastIndexOf("/") + 1);
                        if (!itemsRef.equals(finalRef)) {
                            list.addAll(getDefinitionParamsByRef(items.getRef(), definitions));
                            return;
                        }
                    }
                } else if (StringUtils.isNotBlank(v.getRef())) {
                    list.addAll(getDefinitionParamsByRef(v.getRef(), definitions));
                    return;
                }
                DefinitionParam definitionParam = new DefinitionParam();
                definitionParam.setType(v.getType());
                definitionParam.setFormat(v.getFormat());
                definitionParam.setExample(v.getExample());//TODO
                definitionParam.setDescription(v.getDescription());
                definitionParam.setName(k);
                if (null != v.getItems()) {
                    String itemsRef = v.getItems().getRef();
                    if (null != itemsRef) {
                        definitionParam.setName(itemsRef.substring(itemsRef.lastIndexOf("/") + 1));
                    }
                }

                if (null != definition.getRequired() && definition.getRequired().contains(k)) {
                    definitionParam.setRequired(true);
                }
                list.add(definitionParam);
            });
        }
        return list;
    }


    public Map<String, Object> toJsonStr(String ref, Map<String, Definition> definitions) {
        Map<String, Object> responseMap = new HashMap<>(8);
        if (ref.startsWith("#")) {
            ref = ref.substring(ref.lastIndexOf("/") + 1);
        }
        Definition definition = definitions.get(ref);
        Map<String, Property> properties = definition.getProperties();
        if ("object".equalsIgnoreCase(definition.getType()) && null != properties && !properties.isEmpty()) {
            String finalRef = ref;
            properties.forEach((k, v) -> {
                if ("array".equals(v.getType())) {
                    Items items = v.getItems();
                    if (StringUtils.isNotBlank(items.getRef())) {
                        String itemsRef = items.getRef();
                        itemsRef = itemsRef.substring(itemsRef.lastIndexOf("/") + 1);
                        if (!itemsRef.equals(finalRef)) {
                            List<Object> list = new ArrayList<>();
                            list.add(toJsonStr(itemsRef, definitions));
                            responseMap.put(k, list);
                            return;
                        }
                    }
                } else if (StringUtils.isNotBlank(v.getRef())) {
                    responseMap.put(k, toJsonStr(v.getRef(), definitions));
                    return;
                }
                String key = k;

                if (null != v.getItems()) {
                    String itemsRef = v.getItems().getRef();
                    if (null != itemsRef) {
                        key = itemsRef.substring(itemsRef.lastIndexOf("/") + 1);
                    }
                }
                responseMap.put(key, v.getExample() != null ? v.getExample() : getDefaultValue(v.getType()));
            });
        }
        return responseMap;
    }

    public Object getDefaultValue(String type) {
        Object value;
        if (null == type) {
            type = "";
        }
        switch (type) {
            case "integer":
                value = 0;
                break;
            case "number":
                value = 0.0;
                break;
            case "boolean":
                value = true;
                break;
            default:
                value = "";
        }
        return value;
    }
}
