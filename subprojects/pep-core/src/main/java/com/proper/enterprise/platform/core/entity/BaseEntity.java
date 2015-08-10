package com.proper.enterprise.platform.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import com.proper.enterprise.platform.core.utils.DateUtil;

@MappedSuperclass
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 8769611929269353212L;
    
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    protected String id;
    
    @Column(updatable = false, nullable = false)
    protected String createUserId;
    
    @Column(updatable = false, nullable = false)
    protected String createTime = DateUtil.getCurrentDateString();
    
    @Column(nullable = false)
    protected String lastModifyUserId;
    
    @Column(nullable = false)
    protected String lastModifyTime = DateUtil.getCurrentDateString();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastModifyUserId() {
        return lastModifyUserId;
    }

    public void setLastModifyUserId(String lastModifyUserId) {
        this.lastModifyUserId = lastModifyUserId;
    }

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
    
}
