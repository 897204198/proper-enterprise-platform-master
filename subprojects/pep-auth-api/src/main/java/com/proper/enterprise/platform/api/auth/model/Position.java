package com.proper.enterprise.platform.api.auth.model;

import com.proper.enterprise.platform.core.api.IBase;

public interface Position extends IBase {

    Organization getBelongOrg();

    void setBelongOrg(Organization organization);

    String getCode();

    void setCode(String code);

    String getName();

    void setName(String name);

    Position getParent();

    void setParent(Position parent);
}
