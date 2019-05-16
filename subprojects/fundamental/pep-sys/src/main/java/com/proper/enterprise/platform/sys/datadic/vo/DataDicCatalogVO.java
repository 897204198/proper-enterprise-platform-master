package com.proper.enterprise.platform.sys.datadic.vo;

import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

public class DataDicCatalogVO extends BaseVO {

    @NotEmpty(message = "{pep.sys.datadic.catalog.empty}")
    private String catalogCode;

    @NotEmpty(message = "{pep.sys.datadic.catalog.name.empty}")
    private String catalogName;

    @Enumerated(EnumType.STRING)
    private DataDicTypeEnum catalogType;

    private DataDicCatalogVO parent;

    private String parentId;

    private Integer sort;

    private Collection<? extends DataDic> dataDics;

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

    public String getParentId() {
        if (StringUtil.isNotEmpty(this.parentId)) {
            return this.parentId;
        }
        if (null == this.getParent()) {
            return null;
        }
        return this.getParent().getId();
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public DataDicCatalogVO getParent() {
        return parent;
    }

    public void setParent(DataDicCatalogVO parent) {
        this.parent = parent;
    }

    public Collection<? extends DataDic> getDataDics() {
        return dataDics;
    }

    public void setDataDics(Collection<? extends DataDic> dataDics) {
        this.dataDics = dataDics;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
