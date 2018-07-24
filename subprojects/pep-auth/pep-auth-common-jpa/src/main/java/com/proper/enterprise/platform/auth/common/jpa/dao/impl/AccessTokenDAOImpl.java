package com.proper.enterprise.platform.auth.common.jpa.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.AccessTokenDAO;
import com.proper.enterprise.platform.api.auth.model.AccessToken;
import com.proper.enterprise.platform.auth.common.jpa.entity.AccessTokenEntity;
import com.proper.enterprise.platform.auth.common.jpa.repository.AccessTokenRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AccessTokenDAOImpl implements AccessTokenDAO {

    private AccessTokenRepository repository;

    @Autowired
    public AccessTokenDAOImpl(AccessTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<AccessToken> getByToken(String token) {
        AccessToken tokenModel = repository.getByToken(token);
        return Optional.ofNullable(tokenModel);
    }

    @Override
    public Optional<AccessToken> getByUserId(String userId) {
        AccessToken tokenModel = repository.getByUserId(userId);
        return Optional.ofNullable(tokenModel);
    }

    @Override
    public Optional<AccessToken> get(String id) {
        return repository.findById(id).map(entity -> entity);
    }

    @Override
    public AccessToken saveOrUpdate(AccessToken accessToken) {
        AccessTokenEntity entity = repository.findById(accessToken.getId()).orElse(new AccessTokenEntity());
        BeanUtils.copyProperties(accessToken, entity);
        return repository.save(entity);
    }

    @Override
    public void deleteByToken(String token) {
        repository.deleteByToken(token);
    }

}
