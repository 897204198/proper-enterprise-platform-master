package com.proper.enterprise.platform.auth.dto;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proper.enterprise.platform.api.auth.Role;
import com.proper.enterprise.platform.auth.entity.RoleEntity;

public class RoleDTO implements Serializable, Role {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleDTO.class);

    private static final long serialVersionUID = 5288209482528086680L;
    
    private String id;
    
    public RoleDTO(RoleEntity entity) {
        if (entity == null) {
            LOGGER.error("Entity SHOULD NOT NULL!");
        } else {
            this.id = entity.getId();
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

}
