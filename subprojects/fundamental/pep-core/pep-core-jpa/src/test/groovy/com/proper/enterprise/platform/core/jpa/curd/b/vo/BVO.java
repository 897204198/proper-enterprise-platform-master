package com.proper.enterprise.platform.core.jpa.curd.b.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.jpa.curd.a.vo.AVO;
import com.proper.enterprise.platform.core.jpa.curd.b.api.B;
import com.proper.enterprise.platform.core.jpa.curd.c.vo.CVO;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.JSONUtil;

import java.util.List;

public class BVO extends BaseVO implements B {

    public String toString() {
        String pepBVOStr = "";
        pepBVOStr = JSONUtil.toJSONIgnoreException(this);
        return pepBVOStr;
    }

    public BVO() {

    }


    @JsonView(value = {AVO.WithB.class})
    private Integer test;

    @JsonView(value = {AVO.WithB.class})
    private String voStrB;

    private List<AVO> avos;

    @JsonView(value = {AVO.WithB.class})
    private CVO cvo;

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

    public void setVoStrB(String voStrB) {
        this.voStrB = voStrB;
    }

    public BVO addB(String voStrB) {
        this.voStrB = voStrB;
        return this;
    }

    public List<AVO> getAvos() {
        return avos;
    }

    public void setAvos(List<AVO> avos) {
        this.avos = avos;
    }

    public CVO getCvo() {
        return cvo;
    }

    public void setCvo(CVO cvo) {
        this.cvo = cvo;
    }

    @Override
    public String getStrB() {
        return voStrB;
    }

    @Override
    public void setStrB(String strB) {
        this.setVoStrB(strB);
    }
}
