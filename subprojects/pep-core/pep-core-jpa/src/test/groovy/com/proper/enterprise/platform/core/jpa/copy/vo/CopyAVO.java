package com.proper.enterprise.platform.core.jpa.copy.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.jpa.copy.api.CopyA;
import com.proper.enterprise.platform.core.jpa.copy.api.CopyB;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.view.BaseView;

public class CopyAVO implements CopyA {

    public String toString() {
        String pepCopyAVO = "";
        pepCopyAVO = JSONUtil.toJSONIgnoreException(this);
        return pepCopyAVO;
    }

    public interface AwithB extends BaseView {

    }

    private String name;

    @JsonView(AwithB.class)
    private CopyBVO cb;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public CopyBVO getCb() {
        return cb;
    }

    @Override
    public void setCb(CopyB b) {
        this.cb = (CopyBVO) b;
    }

}
