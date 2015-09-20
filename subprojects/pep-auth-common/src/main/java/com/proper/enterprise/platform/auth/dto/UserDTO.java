package com.proper.enterprise.platform.auth.dto;

import com.proper.enterprise.platform.api.auth.User;
import com.proper.enterprise.platform.auth.entity.UserEntity;
import com.proper.enterprise.platform.core.dto.BaseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDTO extends BaseDTO implements User {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDTO.class);
    
    private static final long serialVersionUID = 5471358115263090637L;
    
    private String username;
    private String password;

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

}
