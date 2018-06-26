package com.proper.enterprise.platform.core.copy.api;

import java.util.List;

public interface A {

    String getName();

    void setName(String name);

    B getBv();

    void setBv(B b);

    List<? extends B> getBs();

    void setBs(List<B> bs);

}
