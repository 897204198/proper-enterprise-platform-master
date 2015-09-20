package com.proper.enterprise.platform.auth.dto;

import com.proper.enterprise.platform.api.auth.Role;
import com.proper.enterprise.platform.auth.entity.RoleEntity;
import com.proper.enterprise.platform.core.dto.BaseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoleDTO extends BaseDTO implements Role {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleDTO.class);

    private static final long serialVersionUID = 5288209482528086680L;
    
    public RoleDTO(RoleEntity entity) {
        if (entity == null) {
            LOGGER.error("Entity SHOULD NOT NULL!");
        } else {
            this.id = entity.getId();
        }
    }

}
