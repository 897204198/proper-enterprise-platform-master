package com.proper.enterprise.platform.api.auth;

import java.io.Serializable;

public class Resource implements Serializable {

    private static final long serialVersionUID = 653004015133021695L;
    
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

}
