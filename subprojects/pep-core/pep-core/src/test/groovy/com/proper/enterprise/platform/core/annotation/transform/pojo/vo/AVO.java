package com.proper.enterprise.platform.core.annotation.transform.pojo.vo;

import com.proper.enterprise.platform.core.convert.annotation.POJOConverter;
import com.proper.enterprise.platform.core.annotation.transform.pojo.bo.ABO;
import com.proper.enterprise.platform.core.pojo.BaseVO;

import java.util.Collection;

public class AVO extends BaseVO {

    @POJOConverter(fromBy = ABO.class, fieldName = "test", targetBy = ABO.class)
    private Integer test;

    @POJOConverter(fromBy = ABO.class, fieldName = "dtoStr")
    @POJOConverter(targetBy = ABO.class, fieldName = "dtoStr")
    private String voStr;

    @POJOConverter(fromClassName = "com.proper.enterprise.platform.core.annotation.transform.pojo.bo.ABO", fieldName = "bbo", targetBy = ABO.class)
    private BVO bvo;

    @POJOConverter(fromBy = ABO.class, fieldName = "bbos", targetBy = ABO.class)
    private Collection<BVO> bvos;

    @POJOConverter(fromBy = ABO.class, fieldName = "testInitalizerBO", fromHandleBy = AVOTestInitalizerBOInitializer.class,
        targetBy = ABO.class, targetHandleBy = AVOTestInitalizerBOConvertor.class)
    private String testInitalizerVO;

    public Integer getTest() {
        return test;
    }

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

    public BVO getBvo() {
        return bvo;
    }

    public void setBvo(BVO bvo) {
        this.bvo = bvo;
    }

    public String getTestInitalizerVO() {
        return testInitalizerVO;
    }

    public void setTestInitalizerVO(String testInitalizerVO) {
        this.testInitalizerVO = testInitalizerVO;
    }
}
