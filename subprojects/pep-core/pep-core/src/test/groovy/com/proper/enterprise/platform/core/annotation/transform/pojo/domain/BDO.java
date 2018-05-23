package com.proper.enterprise.platform.core.annotation.transform.pojo.domain;

import java.util.List;


public class BDO extends BaseEntity {

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
