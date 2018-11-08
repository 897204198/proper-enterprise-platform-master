package com.proper.enterprise.platform.core.annotation.transform.pojo.bo;

import com.proper.enterprise.platform.core.annotation.transform.pojo.domain.BaseEntity;
import com.proper.enterprise.platform.core.utils.JSONUtil;

import java.util.List;

public class ABO extends BaseEntity {

    public String toString() {
        String pepABOStr = "";
        pepABOStr = JSONUtil.toJSONIgnoreException(this);
        return pepABOStr;
    }

    private Integer test;

    private String dtoStr;

    private BBO bbo;

    private List<BBO> bbos;

    private Integer testInitalizerBO;

    public Integer getTest() {
        return test;
    }

    public ABO setTest(Integer test) {
        this.test = test;
        return this;
    }

    public String getDtoStr() {
        return dtoStr;
    }

    public ABO setDtoStr(String dtoStr) {
        this.dtoStr = dtoStr;
        return this;
    }

    public List<BBO> getBbos() {
        return bbos;
    }

    public ABO setBbos(List<BBO> bbos) {
        this.bbos = bbos;
        return this;
    }

    public BBO getBbo() {
        return bbo;
    }

    public ABO setBbo(BBO bbo) {
        this.bbo = bbo;
        return this;
    }

    public Integer getTestInitalizerBO() {
        return testInitalizerBO;
    }

    public void setTestInitalizerBO(Integer testInitalizerBO) {
        this.testInitalizerBO = testInitalizerBO;
    }
}
