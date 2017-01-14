package com.proper.enterprise.platform.core.api;

import java.io.Serializable;

public interface IBase extends Serializable {

    String getId();

    void setId(String id);

    String getCreateUserId();

    void setCreateUserId(String createUserId);

    String getCreateTime();

    void setCreateTime(String createTime);

    String getLastModifyUserId();

    void setLastModifyUserId(String lastModifyUserId);

    String getLastModifyTime();

    void setLastModifyTime(String lastModifyTime);

    boolean isValid();

    void setValid(boolean valid);

}
