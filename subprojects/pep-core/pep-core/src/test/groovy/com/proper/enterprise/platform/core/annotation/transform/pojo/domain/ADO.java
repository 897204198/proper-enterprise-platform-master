package com.proper.enterprise.platform.core.annotation.transform.pojo.domain;

import com.proper.enterprise.platform.core.utils.JSONUtil;

import java.util.List;


public class ADO extends BaseEntity {

    public String toString() {
        String pepADOStr = "";
        pepADOStr = JSONUtil.toJSONIgnoreException(this);
        return pepADOStr;
    }

    public ADO() {
    }

    private Integer test;

    private String doStr;

    private List<BDO> bdos;

    public Integer getTest() {
        return test;
    }

    public ADO setTest(Integer test) {
        this.test = test;
        return this;
    }

    public String getDoStr() {
        return doStr;
    }

    public void setDoStr(String doStr) {
        this.doStr = doStr;
    }

    public List<BDO> getBdos() {
        return bdos;
    }

    public ADO setBdos(List<BDO> bdos) {
        this.bdos = bdos;
        return this;
    }
}
