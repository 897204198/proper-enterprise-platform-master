package com.proper.enterprise.platform.auth.rule.vo;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;

public class RuleVO extends BaseVO {

    public RuleVO() {
    }

    private String code;

    private String name;

    private DataDicLiteBean type;

    private Integer sort;

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

    public DataDicLiteBean getType() {
        return type;
    }

    public void setType(DataDicLiteBean type) {
        this.type = type;
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
