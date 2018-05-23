package com.proper.enterprise.platform.oopsearch.entity;

import com.proper.enterprise.platform.core.jpa.entity.BaseEntity;
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TEST_TABLE2")
@CacheEntity
public class Table2EntityTest extends BaseEntity {

    @Column(nullable = false)
    private String deptId;

    @Column(nullable = false)
    private String deptName;

    @Column(nullable = false)
    private String deptDesc;

    @Column(nullable = false)
    private int deptMemberCount;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptDesc() {
        return deptDesc;
    }

    public void setDeptDesc(String deptDesc) {
        this.deptDesc = deptDesc;
    }

    public int getDeptMemberCount() {
        return deptMemberCount;
    }

    public void setDeptMemberCount(int deptMemberCount) {
        this.deptMemberCount = deptMemberCount;
    }
}
