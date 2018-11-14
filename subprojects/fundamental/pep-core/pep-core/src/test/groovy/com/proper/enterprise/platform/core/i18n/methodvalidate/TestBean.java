package com.proper.enterprise.platform.core.i18n.methodvalidate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TestBean {

    public interface MinValidGroup {
    }


    @NotNull(message = "{core.test.k3}")
    private String at;


    @Min(value = 100, message = "{min 100}", groups = MinValidGroup.class)
    @NotNull(message = "{core.test.k3}")
    private Integer bt;

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public Integer getBt() {
        return bt;
    }

    public void setBt(Integer bt) {
        this.bt = bt;
    }
}
