package com.proper.enterprise.platform.push.api.openapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PushMessage {
    String id;
    String title;
    String content;
    Map<String, Object> customs = new LinkedHashMap<String, Object>();

    public PushMessage(String title, String content, Map<String, Object> customs) {
        super();
        this.title = title;
        this.content = content;
        this.customs = customs;
    }

    public PushMessage(String title, String content) {
        super();
        this.title = title;
        this.content = content;
    }

    public PushMessage() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getCustoms() {
        return customs;
    }

    public void setCustoms(Map<String, Object> customs) {
        this.customs = customs;
    }

    public void addCustomData(String key, Object value) {
        this.customs.put(key, value);
    }

    public void addCustomDatas(Map<String, Object> datas) {
        this.customs.putAll(datas);
    }

}
