package com.proper.enterprise.platform.api.auth;

public interface User {

    String getId();

    void setId(String id);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);
    
}
