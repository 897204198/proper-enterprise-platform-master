package com.proper.enterprise.platform.core.annotation.transform.pojo.domain;

import com.proper.enterprise.platform.core.utils.JSONUtil;

import java.util.List;


public class BDO extends BaseEntity {
    public String toString() {
        String pepBDOStr = "";
        pepBDOStr = JSONUtil.toJSONIgnoreException(this);
        return pepBDOStr;
    }

    public BDO() {
    }

    private Integer test;

    private List<ADO> ados;
    private String doStrB;

    public Integer getTest() {
        return test;
    }

    public BDO setTest(Integer test) {
        this.test = test;
        return this;
    }

    public List<ADO> getAdos() {
        return ados;
    }

    public BDO setAdos(List<ADO> ados) {
        this.ados = ados;
        return this;
    }

    public String getDoStrB() {
        return doStrB;
    }

    public BDO setDoStrB(String doStrB) {
        this.doStrB = doStrB;
        return this;
    }
}
