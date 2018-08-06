package com.proper.enterprise.platform.notice.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.view.BaseView;

public class TemplateVO extends BaseVO {

    public interface Detail extends BaseView {

    }

    /**
     * 标识
     */
    @JsonView(value = {Detail.class})
    private String code;

    /**
     * 名称
     */
    @JsonView(value = {Detail.class})
    private String name;

    /**
     * 标题
     */
    @JsonView(value = {Detail.class})
    private String title;

    /**
     * 模板
     */
    @JsonView(value = {Detail.class})
    private String template;

    /**
     * 目录
     */
    @JsonView(value = {Detail.class})
    private String catelog;

    /**
     * 类型
     */
    @JsonView(value = {Detail.class})
    private String type;

    /**
     * 解释
     */
    @JsonView(value = {Detail.class})
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getCatelog() {
        return catelog;
    }

    public void setCatelog(String catelog) {
        this.catelog = catelog;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return "id:" + id;
    }
}
