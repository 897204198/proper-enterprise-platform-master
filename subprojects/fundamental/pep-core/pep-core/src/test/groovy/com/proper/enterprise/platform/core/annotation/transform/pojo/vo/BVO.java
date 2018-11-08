package com.proper.enterprise.platform.core.annotation.transform.pojo.vo;

import com.proper.enterprise.platform.core.annotation.transform.pojo.bo.BBO;
import com.proper.enterprise.platform.core.convert.annotation.POJOConverter;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.JSONUtil;

import java.util.List;

public class BVO extends BaseVO {

    public String toString() {
        String pepBVOStr = "";
        pepBVOStr = JSONUtil.toJSONIgnoreException(this);
        return pepBVOStr;
    }

    private Integer test;

    @POJOConverter(fromBy = BBO.class, fieldName = "dtoStrB", targetBy = BBO.class)
    private String voStrB;

    @POJOConverter(fromBy = BBO.class, fieldName = "as", targetBy = BBO.class)
    private List<AVO> as;

    public Integer getTest() {
        return test;
    }

    public BVO setTest(Integer test) {
        this.test = test;
        return this;
    }

    public String getVoStrB() {
        return voStrB;
    }

    public BVO setVoStrB(String voStrB) {
        this.voStrB = voStrB;
        return this;
    }

    public List<AVO> getAs() {
        return as;
    }

    public void setAs(List<AVO> as) {
        this.as = as;
    }
}
