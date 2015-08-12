package com.proper.enterprise.platform.auth;

import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.auth.entity.RoleResourceEntity;

public class ResourceConverter {
    
    private ResourceConverter() { }
    
    public static Resource toResource(RoleResourceEntity entity) {
        Resource res = new Resource();
        res.setId(entity.getResourceId());
        return res;
    }

}
