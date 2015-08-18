package com.proper.enterprise.platform.auth.entity;

import com.proper.enterprise.platform.core.entity.BaseEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pep_auth_resource")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "com.proper.enterprise.platform.auth.entity.ResourceEntity")
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
     * 父资源
     */
    private String parent;
    
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
    private int sequenceNumber;
    
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

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
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

}
