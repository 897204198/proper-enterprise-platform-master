package com.proper.enterprise.platform.core.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.core.PEPVersion;
import com.proper.enterprise.platform.core.jpa.listener.HistoricalEntityListener;
import com.proper.enterprise.platform.core.pojo.BaseDO;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * 数据模型及实体基类，包含数据表公共字段的 getter 和 setter 方法
 */
@MappedSuperclass
@EntityListeners({HistoricalEntityListener.class})
public class BaseEntity extends BaseDO {

    private static final long serialVersionUID = PEPVersion.VERSION;

    /**
     * 唯一标识
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    protected String id;

    /**
     * 创建用户 id
     */
    @JsonIgnore
    @Column(updatable = false, nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'PEP'")
    protected String createUserId;

    /**
     * 创建时间
     */
    @JsonIgnore
    @Column(updatable = false, nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '2017-02-20 00:00:00'")
    protected String createTime;

    /**
     * 最后修改用户 id
     */
    @JsonIgnore
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'PEP'")
    protected String lastModifyUserId;

    /**
     * 最后修改时间
     */
    @JsonIgnore
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '2017-02-20 00:00:00'")
    protected String lastModifyTime;

    @Type(type = "yes_no")
    @Column(nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
    private Boolean enable;


    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getCreateUserId() {
        return createUserId;
    }

    @Override
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    @Override
    public String getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getLastModifyUserId() {
        return lastModifyUserId;
    }

    @Override
    public void setLastModifyUserId(String lastModifyUserId) {
        this.lastModifyUserId = lastModifyUserId;
    }

    @Override
    public String getLastModifyTime() {
        return lastModifyTime;
    }

    @Override
    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
