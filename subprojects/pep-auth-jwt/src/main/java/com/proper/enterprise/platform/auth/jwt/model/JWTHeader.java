package com.proper.enterprise.platform.auth.jwt.model;

import java.io.Serializable;

public class JWTHeader implements Serializable {

    private static final long serialVersionUID = 388046039690163330L;

    // User ID
    private String uid;

    // User name
    private String uname;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
