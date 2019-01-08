package com.proper.enterprise.platform.sys.datadic.vo;

import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;

public class DataDicCatalogVO extends BaseVO {

    @NotEmpty(message = "{pep.sys.datadic.catalog.empty}")
    private String catalogCode;

    @NotEmpty(message = "{pep.sys.datadic.catalog.name.empty}")
    private String catalogName;

    @Enumerated(EnumType.STRING)
    private DataDicTypeEnum catalogType;

    private Integer sort;

    public String getCatalogCode() {
        return catalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        this.catalogCode = catalogCode;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public DataDicTypeEnum getCatalogType() {
        return catalogType;
    }

    public void setCatalogType(DataDicTypeEnum catalogType) {
        this.catalogType = catalogType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
