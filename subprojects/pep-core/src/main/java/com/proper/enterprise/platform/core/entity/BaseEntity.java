package com.proper.enterprise.platform.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.core.utils.DateUtil;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 数据模型及实体基类，包含数据表公共字段的 getter 和 setter 方法
 *
 * 公共字段包括：
 *  - id                唯一标识
 *  - createUserId      创建用户 id
 *  - createTime        创建时间
 *  - lastModifyUserId  最后修改用户 id
 *  - lastModifyTime    最后修改时间
 */
@MappedSuperclass
public class BaseEntity implements IBase {

    private static final long serialVersionUID = PEPConstants.VERSION;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    protected String id;

    @Column(updatable = false, nullable = false)
    @JsonIgnore
    protected String createUserId;

    @Column(updatable = false, nullable = false)
    @JsonIgnore
    protected String createTime = DateUtil.getTimestamp(true);

    @Column(nullable = false)
    @JsonIgnore
    protected String lastModifyUserId;

    @Column(nullable = false)
    @JsonIgnore
    protected String lastModifyTime = DateUtil.getTimestamp(true);

    @Column(nullable = false)
    @JsonIgnore
    @Type(type = "yes_no")
    protected boolean valid = true;

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

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

}
