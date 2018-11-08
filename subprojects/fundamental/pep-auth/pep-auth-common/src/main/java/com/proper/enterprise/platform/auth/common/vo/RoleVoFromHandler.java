package com.proper.enterprise.platform.auth.common.vo;

import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.core.convert.handler.FromHandler;

public class RoleVoFromHandler implements FromHandler<RoleVO, Role> {

    @Override
    public void from(RoleVO roleVO, Role role) {
        if (null != role.getParent()) {
            roleVO.setParentId(role.getParent().getId());
            roleVO.setParentName(role.getParent().getName());
        }
    }
}
