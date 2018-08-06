package com.proper.enterprise.platform.core.utils.model;

import com.proper.enterprise.platform.core.utils.JSONUtil;

public class C implements CI {

    private Integer sort;

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
