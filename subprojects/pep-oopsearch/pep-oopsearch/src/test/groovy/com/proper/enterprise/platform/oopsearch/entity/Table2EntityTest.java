package com.proper.enterprise.platform.oopsearch.entity;

import com.proper.enterprise.platform.core.annotation.CacheEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "TEST_TABLE2")
@CacheEntity
public class Table2EntityTest {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    protected String id;

    @Column(nullable = false)
    private String deptId;

    @Column(nullable = false)
    private String deptName;

    @Column(nullable = false)
    private String deptDesc;

    @Column(nullable = false)
    private String createTime;

    @Column(nullable = false)
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
