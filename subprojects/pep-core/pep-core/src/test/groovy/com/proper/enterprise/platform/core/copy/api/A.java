package com.proper.enterprise.platform.core.copy.api;

import java.util.List;

public interface A {

    /**
     * 获取name
     * @return 字符串
     */
    String getName();

    /**
     * 赋值name
     * @param name name
     */
    void setName(String name);

    /**
     * 获取Bv
     * @return B
     */
    B getBv();

    /**
     * 赋值Bv
     * @param b b
     */
    void setBv(B b);

    /**
     * 获取Bs
     * @return 集合
     */
    List<? extends B> getBs();

    /**
     * 赋值bs
     * @param bs bs
     */
    void setBs(List<B> bs);

}
