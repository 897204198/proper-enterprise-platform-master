package com.proper.enterprise.platform.core.controller.mock;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.proper.enterprise.platform.core.pojo.BaseDO;
import com.proper.enterprise.platform.core.pojo.BaseVO;
import com.proper.enterprise.platform.core.view.BaseView;

public class BaseEntity extends BaseDO {

    /**
     * 唯一标识
     */
    @JsonView(value = BaseView.class)
    protected String id;

    /**
     * 创建用户 id
     */
    @JsonIgnore
    @JsonView(value = BaseVO.VOCommonView.class)
    protected String createUserId;

    /**
     * 创建时间
     */
    @JsonIgnore
    @JsonView(value = BaseVO.VOCommonView.class)
    protected String createTime;

    /**
     * 最后修改用户 id
     */
    @JsonIgnore
    @JsonView(value = BaseVO.VOCommonView.class)
    protected String lastModifyUserId;

    /**
     * 启用停用 true启用 false停用
     */
    private Boolean enable;

    /**
     * 最后修改时间
     */
    @JsonIgnore
    @JsonView(value = BaseVO.VOCommonView.class)
    protected String lastModifyTime;

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
    public Boolean getEnable() {
        return enable;
    }

    @Override
    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
