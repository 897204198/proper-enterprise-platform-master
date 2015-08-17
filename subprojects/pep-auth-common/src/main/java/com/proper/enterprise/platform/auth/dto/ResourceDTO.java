package com.proper.enterprise.platform.auth.dto;

import com.proper.enterprise.platform.api.auth.Resource;
import com.proper.enterprise.platform.auth.entity.ResourceEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class ResourceDTO implements Serializable, Resource {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceDTO.class);

    private static final long serialVersionUID = -8467254578546259161L;
    
    private String id;

    private String url;
    
    public ResourceDTO(ResourceEntity entity) {
        if (entity == null) {
            LOGGER.error("Entity SHOULD NOT NULL!");
        } else {
            this.id = entity.getId();
            this.url = entity.getUrl();
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
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

}
