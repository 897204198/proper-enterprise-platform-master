package com.proper.enterprise.platform.auth.common.vo;

import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;

import java.util.ArrayList;
import java.util.Collection;

public class UserVO extends BaseVO implements User {

    /**
     * 用户名，唯一
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱，用于找回密码
     */
    private String email;

    /**
     * 是否为超级用户
     */
    private boolean superuser;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户显示名称
     */
    private String name = " ";

    /**
     * 用户状态
     */
    private boolean enable = true;
    protected String pepDtype;
    private Collection<? extends Role> roles = new ArrayList<>();
    private Collection<? extends UserGroup> userGroups = new ArrayList<>();

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && (obj instanceof UserVO) && id.equals(((UserVO) obj).id);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + "]";
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
    public void add(Role role) {
    }

    @Override
    public void remove(Role role) {
    }

    @Override
    public boolean isSuperuser() {
        return superuser;
    }

    public void setSuperuser(boolean superuser) {
        this.superuser = superuser;
    }

    public String getPepDtype() {
        return pepDtype;
    }

    public void setPepDtype(String pepDtype) {
        this.pepDtype = pepDtype;
    }

    @Override
    public Collection<? extends UserGroup> getUserGroups() {
        return new ArrayList<>();
    }

    @Override
    public Collection<? extends Role> getRoles() {
        return new ArrayList<>();
    }
}
