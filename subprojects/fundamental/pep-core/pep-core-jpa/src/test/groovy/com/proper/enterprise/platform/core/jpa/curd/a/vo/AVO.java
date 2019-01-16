package com.proper.enterprise.platform.core.jpa.curd.a.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.convert.annotation.DeclareType;
import com.proper.enterprise.platform.core.jpa.curd.a.api.A;
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

    @JsonView(value = {Single.class})
    private Integer test;

    @JsonView(value = {Single.class})
    private String voStr;

    @JsonView(value = {WithB.class})
    private Collection<BVO> bvos;

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
        return getCentity();
    }

    public void setCvo(CVO cvo) {
        this.cvo = cvo;
    }

    @Override
    @JsonIgnore
    public String getDoStr() {
        return voStr;
    }

    @Override
    public void setDoStr(String doStr) {
        this.setVoStr(doStr);
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
    @DeclareType(classType = BVO.class)
    public List<BVO> setBs(List<? extends B> bs) {
        this.setBvos((Collection<BVO>) bs);
        if (null == this.bvos) {
            return null;
        }
        return new ArrayList<>(this.bvos);
    }

    @Override
    @JsonIgnore
    public CVO getCentity() {
        return cvo;
    }

    @Override
    public void setCentity(C c) {
        this.setCvo((CVO) c);
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
