package com.proper.enterprise.platform.app.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "PEP_APPLICATIONS_CATALOG")
public class AppCatalogEntity extends BaseEntity implements Serializable {

    /**
     * 应用类别编码
     */
    @Column(unique = true, nullable = false)
    private String code;
    /**
     * 应用类别名称
     */
    private String typeName;

    /**
     * 应用顺序
     */
    private String sort;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

}
