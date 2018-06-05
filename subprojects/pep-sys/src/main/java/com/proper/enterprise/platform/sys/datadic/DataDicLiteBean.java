package com.proper.enterprise.platform.sys.datadic;

import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;

public class DataDicLiteBean implements DataDicLite {

    private String catalog;
    private String code;
    private String name;
    private DataDicTypeEnum dataDicType;
    private Boolean enable;

    public DataDicLiteBean(String catalog, String code) {
        this.catalog = catalog;
        this.code = code;
        DataDic dataDic = DataDicUtil.get(catalog, code);
        if (null == dataDic || StringUtil.isNull(dataDic.getName())) {
            return;
        }
        this.name = dataDic.getName();
        this.enable = dataDic.getEnable();
    }

    @Override
    public String toString() {
        return catalog + DD_CATALOG_CODE_SEPARATOR + code;
    }

    @Override
    public int hashCode() {
        return StringUtil.defaultString(catalog).hashCode() + StringUtil.defaultString(code).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DataDicLite) {
            DataDicLite lite = (DataDicLite) obj;
            return StringUtil.equals(catalog, lite.getCatalog()) && StringUtil.equals(code, lite.getCode());
        }
        return false;
    }

    @Override
    public String getCatalog() {
        return catalog;
    }

    @Override
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }


    public String getName() {
        return name;
    }

    @Override
    public DataDicTypeEnum getDataDicType() {
        return dataDicType;
    }

    @Override
    public void setDataDicType(DataDicTypeEnum dataDicType) {
        this.dataDicType = dataDicType;
    }

    @Override
    public Boolean getEnable() {
        return enable;
    }

    @Override
    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
