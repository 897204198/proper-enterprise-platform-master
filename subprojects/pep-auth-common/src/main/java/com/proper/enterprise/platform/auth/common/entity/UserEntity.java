package com.proper.enterprise.platform.auth.common.entity;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "PEP_AUTH_USERS")
@CacheEntity
public class UserEntity extends BaseEntity implements User {

    private static final long serialVersionUID = PEPConstants.VERSION;

    public UserEntity() { }
    
    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    /**
     * 用户名，唯一
     */
    @Column(unique = true, nullable = false)
    private String username;
    
    /**
     * 密码
     */
    @Column(nullable = false)
    private String password;

    /**
     * 邮箱，用于找回密码
     */
    private String email;

    /**
     * override property in super class
     */
    private String extendProperties = "";

    @ManyToMany
    private Collection<RoleEntity> roles;

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    private PersonEntity personEntity;
    
    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", extendProperties=" + extendProperties + "]";
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleEntity> roles) {
        this.roles = roles;
    }

    public PersonEntity getPersonEntity() {
        return personEntity;
    }

    public void setPersonEntity(PersonEntity personEntity) {
        this.personEntity = personEntity;
    }

}
