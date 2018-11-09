package com.proper.enterprise.platform.template.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.view.BaseView;

import java.io.Serializable;

public class TemplateDetailVO implements Serializable {

    public interface VOCommonView extends BaseView {

    }

    public TemplateDetailVO() {

    }

    /**
     * 标题
     */
    @JsonView(value = BaseView.class)
    private String title;

    /**
     * 正文
     */
    @JsonView(value = BaseView.class)
    private String template;

    /**
     * 分类
     */
    @JsonView(value = BaseView.class)
    private String type;

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

    public String getType() {
        if (StringUtil.isNotNull(type)) {
            return type.toUpperCase();
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return this.title + this.template + type;
    }

}
