package com.proper.enterprise.platform.sys.datadic;

public class DataDicLiteBean implements DataDicLite {

    private String catalog;
    private String code;

    public DataDicLiteBean(String catalog, String code) {
        this.catalog = catalog;
        this.code = code;
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
