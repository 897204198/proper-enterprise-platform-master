package com.proper.enterprise.platform.core.jpa.curd.a.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.convert.annotation.POJOConverter;
import com.proper.enterprise.platform.core.convert.annotation.POJORelevance;
import com.proper.enterprise.platform.core.jpa.curd.a.api.A;
import com.proper.enterprise.platform.core.jpa.curd.a.entity.AEntity;
import com.proper.enterprise.platform.core.jpa.curd.b.api.B;
import com.proper.enterprise.platform.core.jpa.curd.b.vo.BVO;
import com.proper.enterprise.platform.core.jpa.curd.c.api.C;
import com.proper.enterprise.platform.core.jpa.curd.c.vo.CVO;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.view.BaseView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@POJORelevance(relevanceDO = AEntity.class)
public class AVO extends BaseVO implements A {

    public String toString() {
        String pepAVOStr = "";
        pepAVOStr = JSONUtil.toJSONIgnoreException(this);
        return pepAVOStr;
    }

    public AVO() {
        super.setEnable(true);
    }

    public interface Single extends BaseView {

    }

    public interface WithB extends Single {

    }

    @POJOConverter(fromBy = AEntity.class, fieldName = "test", targetBy = AEntity.class)
    @JsonView(value = {Single.class})
    private Integer test;

    @POJOConverter(fromBy = AEntity.class, fieldName = "doStr", targetBy = AEntity.class)
    @JsonView(value = {Single.class})
    private String voStr;

    @POJOConverter(fromBy = AEntity.class, fieldName = "bentities", targetBy = AEntity.class)
    @JsonView(value = {WithB.class})
    private Collection<BVO> bvos;

    @POJOConverter(fromBy = AEntity.class, fieldName = "centity", targetBy = AEntity.class)
    private CVO cvo;

    @Override
    public Integer getTest() {
        return test;
    }

    @Override
    public AVO setTest(Integer test) {
        this.test = test;
        return this;
    }

    public String getVoStr() {
        return voStr;
    }

    public AVO setVoStr(String voStr) {
        this.voStr = voStr;
        return this;
    }

    public Collection<BVO> getBvos() {
        return bvos;
    }

    public void setBvos(Collection<BVO> bvos) {
        this.bvos = bvos;
    }

    public CVO getCvo() {
        return cvo;
    }

    public void setCvo(CVO cvo) {
        this.cvo = cvo;
    }

    @Override
    @JsonIgnore
    public String getDoStr() {
        return null;
    }

    @Override
    public void setDoStr(String doStr) {

    }

    @Override
    @JsonIgnore
    public List<BVO> getBs() {
        if (null == bvos) {
            return null;
        }
        return new ArrayList<>(bvos);
    }

    @Override
    @JsonIgnore
    public AVO setBs(List<? extends B> bs) {
        this.setBvos((Collection<BVO>) bs);
        return this;
    }

    @Override
    @JsonIgnore
    public CVO getCentity() {
        return cvo;
    }

    @Override
    public AVO setCentity(C c) {
        this.setCvo((CVO) c);
        return this;
    }

    @Override
    @JsonIgnore
    public AVO add(B b) {
        if (null == bvos) {
            bvos = new ArrayList<>();
        }
        bvos.add((BVO) b);
        return this;
    }
}
