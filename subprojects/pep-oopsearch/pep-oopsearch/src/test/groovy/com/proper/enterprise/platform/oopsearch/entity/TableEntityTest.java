package com.proper.enterprise.platform.oopsearch.entity;

import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TEST_TABLE")
@CacheEntity
public class TableEntityTest extends BaseEntity {

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String deptId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

}
