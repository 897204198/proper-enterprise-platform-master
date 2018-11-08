package com.proper.enterprise.platform.oopsearch.sync.mysql.entity;

import java.io.Serializable;

public class DemoTestPK implements Serializable {

    private String id;

    private String pri2;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPri2() {
        return pri2;
    }

    public void setPri2(String pri2) {
        this.pri2 = pri2;
    }
}
