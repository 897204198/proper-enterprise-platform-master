package com.proper.enterprise.platform.core.jpa.curd.a.api;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.core.jpa.curd.b.api.B;
import com.proper.enterprise.platform.core.jpa.curd.c.api.C;
import java.util.List;

public interface A extends IBase {

    /**
     * 获取test
     * @return 数量
     */
    public Integer getTest();

    /**
     * 赋值test
     * @param test test
     * @return A
     */
    public A setTest(Integer test);

    /**
     * 获取doStr
     * @return 返回结果
     */
    public String getDoStr();

    /**
     * 赋值doStr
     * @param doStr doStr
     */
    public void setDoStr(String doStr);

    /**
     * 获取Bs
     * @return 集合
     */
    public List<? extends B> getBs();

    /**
     * 赋值Bs
     * @param bs bs
     * @return A
     */
    public A setBs(List<? extends B> bs);

    /**
     * 获取centity
     * @return C
     */
    public C getCentity();

    /**
     * 赋值centity
     * @param c c
     * @return A
     */
    public A setCentity(C c);

    /**
     * add()方法
     * @param b b
     * @return A
     */
    public A add(B b);
}
