package com.proper.enterprise.platform.core.jpa.curd.a.api;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.core.jpa.curd.b.api.B;
import com.proper.enterprise.platform.core.jpa.curd.c.api.C;
import java.util.List;

public interface A extends IBase {

    public Integer getTest();

    public A setTest(Integer test);

    public String getDoStr();

    public void setDoStr(String doStr);

    public List<? extends B> getBs();

    public A setBs(List<? extends B> bs);

    public C getCentity();

    public A setCentity(C c);

    public A add(B b);
}
