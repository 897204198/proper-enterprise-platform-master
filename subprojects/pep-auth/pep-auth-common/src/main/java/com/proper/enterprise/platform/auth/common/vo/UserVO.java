package com.proper.enterprise.platform.auth.common.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;

import com.proper.enterprise.platform.core.convert.annotation.POJOConverter;
import com.proper.enterprise.platform.core.convert.annotation.POJORelevance;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.view.BaseView;

import java.util.Collection;

@POJORelevance(relevanceDOClassName = "com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity")
public class UserVO extends BaseVO implements User {

    public UserVO() {

    }

    public UserVO(String username, String password) {
        this.username = username;
        this.password = password;
        this.superuser = false;
        super.setEnable(true);
    }

    private static final String USER_ENTITY_PATH = "com.proper.enterprise.platform.auth.common.jpa.entity.UserEntity";

    public interface Single extends BaseView {

    }

    /**
     * 用户名，唯一
     */
    @JsonView(value = {Single.class})
    private String username;

    /**
     * 密码
     */
    @JsonView(value = {Single.class})
    private String password;

    /**
     * 邮箱，用于找回密码
     */
    @JsonView(value = {Single.class})
    private String email;

    /**
     * 是否为超级用户
     */
    @JsonView(value = {Single.class})
    private Boolean superuser;

    /**
     * 用户手机号
     */
    @JsonView(value = {Single.class})
    private String phone;

    /**
     * 用户显示名称
     */
    @JsonView(value = {Single.class})
    private String name;

    protected String pepDtype;

    @POJOConverter(fromClassName = USER_ENTITY_PATH,
        fieldName = "roleEntities",
        targetClassName = USER_ENTITY_PATH)
    private Collection<RoleVO> roles;

    @POJOConverter(fromClassName = USER_ENTITY_PATH,
        fieldName = "userGroupEntities",
        targetClassName = USER_ENTITY_PATH)
    private Collection<UserGroupVO> userGroups;

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
    public Boolean getSuperuser() {
        return superuser;
    }

    public void setSuperuser(Boolean superuser) {
        this.superuser = superuser;
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
    public void add(Role role) {

    }

    @Override
    public void add(UserGroup userGroup) {

    }

    @Override
    public void remove(Role role) {

    }

    @Override
    public void remove(UserGroup userGroup) {

    }

    public String getPepDtype() {
        return pepDtype;
    }

    public void setPepDtype(String pepDtype) {
        this.pepDtype = pepDtype;
    }

    @Override
    public Collection<RoleVO> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleVO> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<UserGroupVO> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(Collection<UserGroupVO> userGroups) {
        this.userGroups = userGroups;
    }
}
