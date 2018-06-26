package com.proper.enterprise.platform.core.copy;

import com.proper.enterprise.platform.core.copy.api.B;

public class BBO implements B {

    private String sex;

    @Override
    public String getSex() {
        return sex;
    }

    @Override
    public void setSex(String sex) {
        this.sex = sex;
    }
}
