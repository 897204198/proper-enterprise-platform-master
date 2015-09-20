package com.proper.enterprise.platform.auth.dto;

import com.proper.enterprise.platform.api.auth.User;
import com.proper.enterprise.platform.auth.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class UserDTO implements Serializable, User {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDTO.class);
    
    private static final long serialVersionUID = 5471358115263090637L;
    
    private String id;
    private String username;
    private String password;
    private String createUserId;
    private String lastModifyUserId;
    
    public UserDTO(UserEntity entity) {
        if (entity == null) {
            LOGGER.debug("Entity SHOULD NOT NULL!");
        } else {
            this.id = entity.getId();
            this.username = entity.getLoginName();
            this.password = entity.getPassword();
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getCreateUserId() {
        return createUserId;
    }

    @Override
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    @Override
    public String getLastModifyUserId() {
        return lastModifyUserId;
    }

    @Override
    public void setLastModifyUserId(String lastModifyUserId) {
        this.lastModifyUserId = lastModifyUserId;
    }

}
