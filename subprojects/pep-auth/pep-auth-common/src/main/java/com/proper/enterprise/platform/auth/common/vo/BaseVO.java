package com.proper.enterprise.platform.auth.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.core.utils.DateUtil;

public class BaseVO implements IBase {

    /**
     * 唯一标识
     */
    protected String id;

    /**
     * 创建用户 id
     */
    @JsonIgnore
    protected String createUserId;

    /**
     * 创建时间
     */
    @JsonIgnore
    protected String createTime = DateUtil.getTimestamp(true);

    /**
     * 最后修改用户 id
     */
    @JsonIgnore
    protected String lastModifyUserId;

    /**
     * 最后修改时间
     */
    @JsonIgnore
    protected String lastModifyTime = DateUtil.getTimestamp(true);

    private boolean enable = true;

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
    public boolean isEnable() {
        return enable;
    }

    @Override
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
