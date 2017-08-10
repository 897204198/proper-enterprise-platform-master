package com.proper.enterprise.platform.auth.jwt.service;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.auth.jwt.model.JWTHeader;
import com.proper.enterprise.platform.auth.jwt.model.JWTPayload;

import java.io.IOException;

public interface JWTAuthcService {

    String getUserToken(String username) throws IOException;

    void clearUserToken(String username);

    JWTHeader composeJWTHeader(User user);

    JWTPayload composeJWTPayload(User user);

}
