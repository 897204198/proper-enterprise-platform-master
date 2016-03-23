package com.proper.enterprise.platform.api.auth.model;

import com.proper.enterprise.platform.core.api.IBase;

public interface User extends IBase {

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    String getTenantId();

}
