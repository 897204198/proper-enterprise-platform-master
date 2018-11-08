package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.model.AccessToken;

import java.util.Optional;

public interface AccessTokenDAO {

    /**
     * 根据token获取与之匹配的AccessToken
     * @param token token
     * @return 匹配的AccessToken
     */
    Optional<AccessToken> getByToken(String token);

    /**
     * 通过userId获取与之匹配的AccessToken
     * @param userId userId
     * @return 匹配的AccessToken
     */
    Optional<AccessToken> getByUserId(String userId);

    /**
     * 通过id获取与之匹配的AccessToken
     * @param id id
     * @return 匹配的AccessToken
     */
    Optional<AccessToken> get(String id);

    /**
     * 保存更新
     * @param accessToken token
     * @return AccessToken
     */
    AccessToken saveOrUpdate(AccessToken accessToken);

    /**
     * 通过token删除
     * @param token token
     */
    void deleteByToken(String token);

}
