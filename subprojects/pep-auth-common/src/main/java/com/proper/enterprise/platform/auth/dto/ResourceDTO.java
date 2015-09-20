package com.proper.enterprise.platform.auth.dto;

import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.auth.entity.ResourceEntity;
import com.proper.enterprise.platform.core.dto.BaseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

public class ResourceDTO extends BaseDTO implements Resource {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceDTO.class);

    private static final long serialVersionUID = -8467254578546259161L;
    
    private String url;
    private RequestMethod method;
    
    public ResourceDTO(ResourceEntity entity) {
        super(entity);
        if (entity == null) {
            LOGGER.error("Entity SHOULD NOT NULL!");
        } else {
            this.url = entity.getUrl();
            this.method = entity.getMethod();
        }
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof Resource)) {
            return false;
        } else if (id == null || id.isEmpty()) {
            return false;
        } else {
            return id.equals(((Resource)obj).getId());
        }
    }
    
    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public RequestMethod getMethod() {
        return method;
    }

    @Override
    public void setMethod(RequestMethod method) {
        this.method = method;
    }
}
