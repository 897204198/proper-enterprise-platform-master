package com.proper.enterprise.platform.sys.datadic;

import com.proper.enterprise.platform.core.utils.StringUtil;

public class DataDicLiteBean implements DataDicLite {

    private String catalog;
    private String code;

    public DataDicLiteBean(){}

    public DataDicLiteBean(String catalog, String code) {
        this.catalog = catalog;
        this.code = code;
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

}
