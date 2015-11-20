package com.proper.enterprise.platform.auth.jwt.model;

import java.io.Serializable;

/**
 * Header part of JSON Web Token (http://jwt.io/)
 */
public class JWTHeader implements Serializable {

    private static final long serialVersionUID = 388046039690163330L;

    // User ID
    private String id;

    // User name
    private String name;

    public JWTHeader() { }

    public JWTHeader(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
