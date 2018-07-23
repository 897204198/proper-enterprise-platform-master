package com.proper.enterprise.platform.auth.common.mock

import com.proper.enterprise.platform.api.auth.dao.AccessTokenDAO
import com.proper.enterprise.platform.api.auth.model.AccessToken
import org.springframework.stereotype.Repository

@Repository
class MockAccessTokenDAOImpl implements AccessTokenDAO {

    def tokenMap = [:]

    @Override
    Optional<AccessToken> getByToken(String token) {
        findOptional({ key, value ->
            key == token
        })
    }

    @Override
    Optional<AccessToken> getByUserId(String userId) {
        findOptional({ key, value ->
            value.userId == userId
        })
    }

    @Override
    Optional<AccessToken> get(String id) {
        findOptional({ key, value ->
            value.id == id
        })
    }

    def findOptional(closure) {
        Optional.of(tokenMap.find(closure).value)
    }

    @Override
    AccessToken saveOrUpdate(AccessToken accessToken) {
        if (accessToken.id == null) {
            accessToken.id = UUID.randomUUID().toString()
        }
        tokenMap.put(accessToken.token, accessToken)
        accessToken
    }

    @Override
    void deleteByToken(String token) {
        tokenMap.remove(token)
    }

}
