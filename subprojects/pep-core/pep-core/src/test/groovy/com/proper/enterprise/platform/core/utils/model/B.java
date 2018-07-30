package com.proper.enterprise.platform.core.utils.model;

import com.proper.enterprise.platform.core.utils.JSONUtil;

import java.util.Set;

public class B {

    private String name;

    private Set<String> strs;

    private Set<C> cs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getStrs() {
        return strs;
    }

    public void setStrs(Set<String> strs) {
        this.strs = strs;
    }

    public Set<C> getCs() {
        return cs;
    }

    public void setCs(Set<C> cs) {
        this.cs = cs;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
