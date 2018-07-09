package com.proper.enterprise.platform.sys.datadic;

import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;

public class DataDicVO extends BaseVO implements DataDic {

    private String catalog;
    private String code;
    private String name;
    private DataDicTypeEnum dataDicType;
    private Integer order;
    private Boolean deft;

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
    public Integer getOrder() {
        return order;
    }

    @Override
    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public Boolean getDeft() {
        return deft;
    }

    @Override
    public void setDeft(Boolean deft) {
        this.deft = deft;
    }

}
