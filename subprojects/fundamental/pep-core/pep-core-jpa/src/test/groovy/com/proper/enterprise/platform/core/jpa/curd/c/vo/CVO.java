package com.proper.enterprise.platform.core.jpa.curd.c.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.jpa.curd.a.vo.AVO;
import com.proper.enterprise.platform.core.jpa.curd.b.api.B;
import com.proper.enterprise.platform.core.jpa.curd.b.vo.BVO;
import com.proper.enterprise.platform.core.jpa.curd.c.api.C;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;

public class CVO extends BaseVO implements C {

    public String toString() {
        String pepCVOStr = "";
        pepCVOStr = JSONUtil.toJSONIgnoreException(this);
        return pepCVOStr;
    }

    public CVO() {
        super.setEnable(true);
    }

    @Override
    @JsonIgnore
    public BVO getB() {
        return bvo;
    }

    @Override
    public void setB(B b) {
        this.bvo = BeanUtil.convert(b, BVO.class);
    }

    public interface Single {

    }

    @JsonView(value = {AVO.WithB.class, Single.class})
    private Integer test;

    private BVO bvo;

    public Integer getTest() {
        return test;
    }

    public CVO setTest(Integer test) {
        this.test = test;
        return this;
    }

    public BVO getBvo() {
        return bvo;
    }

    public void setBvo(BVO bvo) {
        this.bvo = bvo;
    }
}
