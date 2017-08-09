package com.proper.enterprise.platform.auth.jwt.service;

import java.io.IOException;

public interface JWTAuthcService {

    String getUserToken(String username) throws IOException;

    void clearUserToken(String username);

}
