package com.proper.enterprise.platform.core.jpa.curd.b.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.convert.annotation.POJOConverter;
import com.proper.enterprise.platform.core.convert.annotation.POJORelevance;
import com.proper.enterprise.platform.core.jpa.curd.a.vo.AVO;
import com.proper.enterprise.platform.core.jpa.curd.b.api.B;
import com.proper.enterprise.platform.core.jpa.curd.b.entity.BEntity;
import com.proper.enterprise.platform.core.jpa.curd.c.vo.CVO;
import com.proper.enterprise.platform.core.pojo.BaseVO;

import java.util.List;

@POJORelevance(relevanceDO = BEntity.class)
public class BVO extends BaseVO implements B {

    public BVO() {
        super.setEnable(true);
    }


    @JsonView(value = {AVO.WithB.class})
    private Integer test;

    @POJOConverter(fromBy = BEntity.class, fieldName = "doStrB", targetBy = BEntity.class)
    @JsonView(value = {AVO.WithB.class})
    private String voStrB;

    @POJOConverter(fromBy = BEntity.class, fieldName = "aentities", targetBy = BEntity.class)
    private List<AVO> avos;

    @POJOConverter(fromBy = BEntity.class, fieldName = "centity", targetBy = BEntity.class)
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

    public BVO setVoStrB(String voStrB) {
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
}
