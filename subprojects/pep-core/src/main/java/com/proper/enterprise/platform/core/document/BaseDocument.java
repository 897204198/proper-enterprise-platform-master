package com.proper.enterprise.platform.core.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.core.utils.DateUtil;
import org.springframework.data.annotation.Id;

public class BaseDocument implements IBase {

    private static final long serialVersionUID = PEPConstants.VERSION;

    @Id
    protected String id;

    @JsonIgnore
    protected String createUserId;

    @JsonIgnore
    protected String createTime = DateUtil.getTimestamp(true);

    @JsonIgnore
    protected String lastModifyUserId;

    @JsonIgnore
    protected String lastModifyTime = DateUtil.getTimestamp(true);

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
