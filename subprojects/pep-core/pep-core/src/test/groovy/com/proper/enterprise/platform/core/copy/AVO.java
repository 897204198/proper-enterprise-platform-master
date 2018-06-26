package com.proper.enterprise.platform.core.copy;

import com.proper.enterprise.platform.core.copy.api.A;
import com.proper.enterprise.platform.core.copy.api.B;
import com.proper.enterprise.platform.core.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

public class AVO implements A {

    private String name;

    private BVO bv;

    private List<BVO> bs;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public BVO getBv() {
        return bv;
    }

    @Override
    public void setBv(B b) {
        this.bv = (BVO) b;
    }

    @Override
    public List<BVO> getBs() {
        return bs;
    }

    public void setBs(List<B> bs) {
        this.bs = new ArrayList<>(CollectionUtil.convert(bs));
    }
}
