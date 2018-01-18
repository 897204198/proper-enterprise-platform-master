package com.proper.enterprise.platform.auth.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Permission;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "PEP_AUTH_ROLES")
@CacheEntity
public class RoleEntity extends BaseEntity implements Role {

    public RoleEntity() { }

    /**
     * 名称
     */
    private String name;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 用户状态
     */
    @Type(type = "yes_no")
    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
    private boolean enable = true;

    @JsonIgnore
    @ManyToMany(mappedBy = "roleEntities")
    private Collection<UserEntity> userEntities = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "PEP_AUTH_ROLES_MENUS",
            joinColumns = @JoinColumn(name = "ROLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "MENU_ID"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"ROLE_ID", "MENU_ID"}))
    private Collection<MenuEntity> menuEntities = new ArrayList<>();

    @Override
    public String toString() {
        return "name: " + name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && (obj instanceof RoleEntity) && id.equals(((RoleEntity) obj).id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public Role getParent() {
        // TODO
        return null;
    }

    @Override
    public void setParent(Role role) {
        // TODO
    }

    @Override
    public Set<Permission> getPermissions() {
        // TODO
        return null;
    }

    @Override
    public void setPermission(Set<Permission> permissions) {
        // TODO
    }

    public Collection<? extends User> getUsers() {
        return userEntities;
    }

    @Override
    public Collection<? extends Menu> getMenus() {
        return menuEntities;
    }

}
