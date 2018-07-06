package com.proper.enterprise.platform.workflow.convert;

import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import org.flowable.idm.engine.impl.persistence.entity.RoleEntityImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RoleConvert {

    public static org.flowable.idm.api.Role convert(Role pepRole) {
        if (null == pepRole) {
            return null;
        }
        org.flowable.idm.api.Role role = new RoleEntityImpl();
        role.setId(pepRole.getId());
        role.setName(pepRole.getName());
        return role;
    }

    public static List<org.flowable.idm.api.Role> convertCollection(Collection<? extends Role> pepRoles) {
        if (CollectionUtil.isEmpty(pepRoles)) {
            return new ArrayList<>();
        }
        List<org.flowable.idm.api.Role> roles = new ArrayList<>();
        for (Role pepRole : pepRoles) {
            org.flowable.idm.api.Role role = convert(pepRole);
            if (null == role) {
                continue;
            }
            roles.add(role);
        }
        return roles;
    }
}
