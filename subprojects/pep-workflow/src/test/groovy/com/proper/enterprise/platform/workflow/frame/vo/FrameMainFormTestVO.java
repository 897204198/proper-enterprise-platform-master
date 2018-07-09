package com.proper.enterprise.platform.workflow.frame.vo;

public class FrameMainFormTestVO {

    private String sex;

    private String name;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FrameMainFormTestVO{sex='" + sex + '\'' + ", name='" + name + '\'' + '}';
    }
}
