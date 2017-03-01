package com.proper.enterprise.platform.api.auth.service;

import java.util.Map;

public interface AuthcService {

    boolean authenticate(String username, String pwd);

    String getUsername(Map<String, String> map);

    String getPassword(Map<String, String> map);

}
