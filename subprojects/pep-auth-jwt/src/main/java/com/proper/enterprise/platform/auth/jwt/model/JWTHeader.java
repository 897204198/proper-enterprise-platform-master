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

    // Expire time (millisecond)
    private long expire;

    public JWTHeader() { }

    public JWTHeader(String id, String name, long expire) {
        this.id = id;
        this.name = name;
        this.expire = expire;
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

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }
}
