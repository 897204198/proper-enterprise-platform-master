package com.proper.enterprise.platform.core.utils.copy;

import com.proper.enterprise.platform.core.utils.copy.api.A;
import com.proper.enterprise.platform.core.utils.copy.api.B;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;

import java.util.ArrayList;
import java.util.List;

public class AVO implements A {

    public String toString() {
        String pepAVOStr = "";
        pepAVOStr = JSONUtil.toJSONIgnoreException(this);
        return pepAVOStr;
    }

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
