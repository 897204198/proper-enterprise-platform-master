package com.proper.enterprise.platform.core.dto;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.core.entity.BaseEntity;

public class BaseDTO implements IBase {

    protected String id;
    protected String createUserId;
    protected String createTime;
    protected String lastModifyUserId;
    protected String lastModifyTime;

    public BaseDTO() { }

    public BaseDTO(BaseEntity entity) {
        if (entity == null) {
            return;
        }
        this.id = entity.getId();
        this.createUserId = entity.getCreateUserId();
        this.createTime = entity.getCreateTime();
        this.lastModifyUserId = entity.getLastModifyUserId();
        this.lastModifyTime = entity.getLastModifyTime();
    }

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

}
