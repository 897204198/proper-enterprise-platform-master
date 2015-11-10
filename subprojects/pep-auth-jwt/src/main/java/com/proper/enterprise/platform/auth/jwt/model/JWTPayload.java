package com.proper.enterprise.platform.auth.jwt.model;

import java.io.Serializable;

public class JWTPayload implements Serializable {

    private static final long serialVersionUID = 3983783683569921L;
    
    private String apikey;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

}
