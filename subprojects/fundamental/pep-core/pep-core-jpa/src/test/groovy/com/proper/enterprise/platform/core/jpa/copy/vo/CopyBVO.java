package com.proper.enterprise.platform.core.jpa.copy.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.jpa.copy.api.CopyA;
import com.proper.enterprise.platform.core.jpa.copy.api.CopyB;
import com.proper.enterprise.platform.core.utils.JSONUtil;

public class CopyBVO implements CopyB {

    public String toString() {
        String pepCopyBVO = "";
        pepCopyBVO = JSONUtil.toJSONIgnoreException(this);
        return pepCopyBVO;
    }

    @JsonView(CopyAVO.AwithB.class)
    private String bname;

    private CopyAVO ca;

    @Override
    public String getBname() {
        return bname;
    }

    @Override
    public void setBname(String bname) {
        this.bname = bname;
    }

    @Override
    public CopyAVO getCa() {
        return ca;
    }

    public void setCa(CopyA a) {
        this.ca = (CopyAVO) a;
    }
}
