package com.proper.enterprise.platform.template.entity;

import com.proper.enterprise.platform.core.jpa.converter.TemplateConverter;
import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.template.vo.TemplateDetailVO;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PEP_TEMPLATE")
public class TemplateEntity extends BaseEntity {

    /**
     * 标识
     */
    @Column(unique = true)
    private String code;

    /**
     * 名称
     */
    @Column
    private String name;

    /**
     * 目录（对应个人设置）
     */
    @Column
    private String catalog;

    /**
     * 解释
     */
    @Column
    private String description;

    /**
     * 是否多文案
     */
    @Column
    private Boolean muti;

    /**
     * 模板
     */
    @Column(length = 2000)
    @Convert(converter = TemplateConverter.class)
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
