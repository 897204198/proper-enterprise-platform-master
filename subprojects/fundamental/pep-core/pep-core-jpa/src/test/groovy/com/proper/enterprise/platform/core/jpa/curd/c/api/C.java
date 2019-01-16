package com.proper.enterprise.platform.core.jpa.curd.c.api;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.core.jpa.curd.b.api.B;

public interface C extends IBase {
    /**
     * 获得B
     *
     * @return B
     */
    B getB();

    /**
     * 设置B
     *
     * @param b b
     */
    void setB(B b);
}
