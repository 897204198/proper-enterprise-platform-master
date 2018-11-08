package com.proper.enterprise.platform.core.annotation.transform.pojo.bo;

import com.proper.enterprise.platform.core.annotation.transform.pojo.domain.BaseEntity;
import com.proper.enterprise.platform.core.utils.JSONUtil;

import java.util.ArrayList;
import java.util.List;

public class BBO extends BaseEntity {

    public String toString() {
        String pepBBOStr = "";
        pepBBOStr = JSONUtil.toJSONIgnoreException(this);
        return pepBBOStr;
    }

    private Integer test;

    private String dtoStrB;

    private List<ABO> as;

    public Integer getTest() {
        return test;
    }

    public BBO setTest(Integer test) {
        this.test = test;
        return this;
    }

    public String getDtoStrB() {
        return dtoStrB;
    }

    public BBO setDtoStrB(String dtoStrB) {
        this.dtoStrB = dtoStrB;
        return this;
    }

    public List<ABO> getAs() {
        return as;
    }

    public void setAs(List<ABO> as) {
        this.as = as;
    }

    public BBO add(ABO abo) {
        if (null == this.as) {
            this.as = new ArrayList<>();
        }
        this.as.add(abo);
        return this;
    }
}
