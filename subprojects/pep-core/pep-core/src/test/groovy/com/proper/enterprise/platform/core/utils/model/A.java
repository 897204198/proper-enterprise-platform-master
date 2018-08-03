package com.proper.enterprise.platform.core.utils.model;

import com.proper.enterprise.platform.core.utils.JSONUtil;

import java.util.Collection;

public class A {

    private String name;

    private Collection<String> strs;

    private Collection<CV> cs;

    private CV cv;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<String> getStrs() {
        return strs;
    }

    public void setStrs(Collection<String> strs) {
        this.strs = strs;
    }

    public Collection<CV> getCs() {
        return cs;
    }

    public void setCs(Collection<CV> cs) {
        this.cs = cs;
    }

    public CV getCv() {
        return cv;
    }

    public void setCv(CV cv) {
        this.cv = cv;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
