package com.proper.enterprise.platform.auth.jwt.model;

import com.proper.enterprise.platform.core.PEPVersion;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Header part of JSON Web Token (http://jwt.io/)
 */
public class JWTHeader implements Serializable {

    private static final long serialVersionUID = PEPVersion.VERSION;

    @ApiModelProperty(name = "‍用户id", required = true)
    private String id;

    @ApiModelProperty(name = "‍用户名", required = true)
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
