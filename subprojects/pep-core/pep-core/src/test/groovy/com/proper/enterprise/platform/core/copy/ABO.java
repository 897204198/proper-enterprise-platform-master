package com.proper.enterprise.platform.core.copy;

import com.proper.enterprise.platform.core.copy.api.A;
import com.proper.enterprise.platform.core.copy.api.B;
import com.proper.enterprise.platform.core.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

public class ABO implements A {

    private String name;

    private BBO bv;

    private List<BBO> bs;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public BBO getBv() {
        return bv;
    }

    @Override
    public void setBv(B b) {
        this.bv = (BBO) b;
    }

    @Override
    public List<BBO> getBs() {
        return bs;
    }


    public void setBs(List<B> bs) {
        this.bs = new ArrayList<>(CollectionUtil.convert(bs));
    }
}
