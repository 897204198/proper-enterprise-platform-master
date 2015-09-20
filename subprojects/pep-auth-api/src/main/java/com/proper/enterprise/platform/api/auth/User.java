package com.proper.enterprise.platform.api.auth;

import com.proper.enterprise.platform.core.api.IBase;

public interface User extends IBase {

    String getId();

    void setId(String id);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

}
