package com.proper.enterprise.platform.auth.common.vo;

import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;

import java.util.ArrayList;
import java.util.Collection;

public class UserGroupVO extends BaseVO implements UserGroup {

    /**
     * 用户组名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 顺序
     */
    private int seq;

    /**
     * 用户组内用户列信息列表
     */
    private Collection<? extends User> users = new ArrayList<>();

    /**
     * 用户组内角色列信息列表
     */
    private Collection<? extends Role> roles = new ArrayList<>();

    /**
     * 用户组状态
     */
    private boolean enable = true;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getSeq() {
        return seq;
    }

    @Override
    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public void add(User user) {

    }

    @Override
    public void add(User... users) {
    }

    @Override
    public void add(Role role) {
    }

    @Override
    public void remove(User user) {
    }

    @Override
    public void remove(Role role) {
    }

    @Override
    public void removeAllUsers() {

    }

    @Override
    public boolean isEnable() {
        return enable;
    }

    @Override
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public Collection<? extends User> getUsers() {
        return new ArrayList<>();
    }

    @Override
    public Collection<? extends Role> getRoles() {
        return new ArrayList<>();
    }
}
