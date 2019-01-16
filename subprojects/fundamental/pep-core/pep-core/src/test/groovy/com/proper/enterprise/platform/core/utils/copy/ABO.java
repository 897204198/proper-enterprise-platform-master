package com.proper.enterprise.platform.core.utils.copy;

import com.proper.enterprise.platform.core.utils.copy.api.A;
import com.proper.enterprise.platform.core.utils.copy.api.B;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;

import java.util.ArrayList;
import java.util.List;

public class ABO implements A {

    public String toString() {
        String pepABOStr = "";
        pepABOStr = JSONUtil.toJSONIgnoreException(this);
        return pepABOStr;
    }

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
        if (null == bs) {
            return;
        }
        this.bs = new ArrayList<>(CollectionUtil.convert(bs));
    }
}
