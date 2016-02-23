package com.proper.enterprise.platform.api.auth;

import com.proper.enterprise.platform.core.api.IBase;

public interface Role extends IBase {

    String getCode();

    void setCode(String code);

    String getName();

    void setName(String name);

}
