package com.proper.enterprise.platform.auth.common.vo;

import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.view.BaseView;

import java.util.Collection;

@POJORelevance(relevanceDOClassName = "com.proper.enterprise.platform.auth.common.jpa.entity.UserGroupEntity")
public class UserGroupVO extends BaseVO implements UserGroup {
    public interface Single extends BaseView {

    }

    public interface GroupWithRole extends Single {

    }

    /**
     * 用户组名称
     */
    @JsonView(value = {Single.class, UserVO.CurrentUser.class})
    private String name;

    /**
     * 描述
     */
    @JsonView(value = {Single.class})
    private String description;

    /**
     * 顺序
     */
    @JsonView(value = {Single.class})
    private Integer seq;

    /**
     * 用户组内用户列信息列表
     */
    private Collection<UserVO> users;

    /**
     * 用户组内角色列信息列表
     */
    @JsonView(value = GroupWithRole.class)
    private Collection<RoleVO> roles;

    /**
     * 用户组状态
     */
    @JsonView(value = {Single.class})
    private Boolean enable;

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
    public Boolean getEnable() {
        return enable;
    }

    @Override
    public void setEnable(Boolean enable) {
        this.enable = enable;
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
    public void removeAllUsers(Collection<? extends User> users) {
        users.removeAll(CollectionUtil.convert(users));
    }

    @Override
    public Collection<UserVO> getUsers() {
        return users;
    }

    @Override
    public Collection<RoleVO> getRoles() {
        return roles;
    }

    public void setUsers(Collection<UserVO> users) {
        this.users = users;
    }

    public void setRoles(Collection<RoleVO> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
