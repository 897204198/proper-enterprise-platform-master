package com.proper.enterprise.platform.oopsearch.sync.h2.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "DEMO_DEPT")
public class DemoDeptEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    protected String id;

    @Column
    private String deptId;

    @Column
    private String deptName;

    @Column
    private String deptDesc;

    @Column
    private String createTime;

    @Column
    private int deptMemberCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getDeptMemberCount() {
        return deptMemberCount;
    }

    public void setDeptMemberCount(int deptMemberCount) {
        this.deptMemberCount = deptMemberCount;
    }
}
