package com.proper.enterprise.platform.integration.webapp.dal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.core.enums.ActiveStatus;
import com.proper.enterprise.platform.core.enums.UseStatus;

@Entity
@Table(name = "pep_test_dal")
public class TestEntity extends BaseEntity {

    private static final long serialVersionUID = -1491308886515558807L;
    
    /**
     * 登录名，唯一
     */
    @Column(unique = true, nullable = false)
    private String loginName;
    
    /**
     * 账号，唯一
     */
    @Column(unique = true, nullable = false)
    private String account;

    /**
     * 密码
     */
    @Column(nullable = false)
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 编号
     */
    private String code;
    
    /**
     * 启用状态
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActiveStatus activeStatus = ActiveStatus.INACTIVE;

    /**
     * 锁定状态
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UseStatus useStatus = UseStatus.STOP;
    
    /**
     * 永不过期
     */
    @Column(nullable = false)
    @org.hibernate.annotations.Type(type = "yes_no")
    private boolean neverExpired;

    /**
     * 到期的时间
     */
    private String dueDate;
    
    /**
     * 邮箱，用于找回密码
     */
    private String email;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ActiveStatus getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(ActiveStatus activeStatus) {
        this.activeStatus = activeStatus;
    }

    public UseStatus getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(UseStatus useStatus) {
        this.useStatus = useStatus;
    }

    public boolean isNeverExpired() {
        return neverExpired;
    }

    public void setNeverExpired(boolean neverExpired) {
        this.neverExpired = neverExpired;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
