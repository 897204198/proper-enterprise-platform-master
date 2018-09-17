package com.proper.enterprise.platform.template.document;

import com.proper.enterprise.platform.core.mongo.document.BaseDocument;
import com.proper.enterprise.platform.template.vo.TemplateDetailVO;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "PEP_TEMPLATE")
public class TemplateDocument extends BaseDocument {

    public TemplateDocument() {
    }

    /**
     * 标识
     */
    @Indexed(unique = true)
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 目录（对应个人设置）
     */
    private String catalog;

    /**
     * 解释
     */
    private String description;

    /**
     * 是否多文案
     */
    private Boolean muti;

    /**
     * 模板
     */
    private List<TemplateDetailVO> details;

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

    public List<TemplateDetailVO> getDetails() {
        return details;
    }

    public void setDetails(List<TemplateDetailVO> details) {
        this.details = details;
    }

    public void setDetails(TemplateDetailVO detail) {
        if (this.details == null) {
            this.details = new ArrayList<>();
        }
        details.add(detail);
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getMuti() {
        return muti;
    }

    public void setMuti(Boolean muti) {
        this.muti = muti;
    }

}
