package com.proper.enterprise.platform.api.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.proper.enterprise.platform.core.entity.BaseEntity;

@Entity
@Table(name = "pep_auth_resource")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "com.proper.enterprise.platform.api.auth.entity.ResourceEntity")
public class ResourceEntity extends BaseEntity {

    private static final long serialVersionUID = 5499576398861944356L;
    
    /**
     * 编号
     */
    private String code;
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 简写名称
     */
    private String simpleName;
    
    /**
     * 父资源
     */
    private String parent;
    
    /**
     * 父资源Id
     */
    private String parentId;
    
    /**
     * 类型
     */
    private String moc;
    
    /**
     * 权限对应的Url地址
     */
    private String url;
    
    /**
     * 图标
     */
    private String icon;
    
    /**
     * 显示顺序
     */
    @Column(nullable = false)
    private int sequenceNumber = 0;
    
    /**
     * 安全控制属性
     */
    @org.hibernate.annotations.Type(type="yes_no")
    private boolean anonymously;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getMoc() {
        return moc;
    }

    public void setMoc(String moc) {
        this.moc = moc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public boolean isAnonymously() {
        return anonymously;
    }

    public void setAnonymously(boolean anonymously) {
        this.anonymously = anonymously;
    }

}
