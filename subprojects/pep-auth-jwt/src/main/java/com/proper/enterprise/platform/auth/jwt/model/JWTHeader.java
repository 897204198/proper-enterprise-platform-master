package com.proper.enterprise.platform.auth.jwt.model;

import java.io.Serializable;

public class JWTHeader implements Serializable {

    private static final long serialVersionUID = 388046039690163330L;
    
    public static final String DEFAULT_TYP = "JWT";
    
    public static final String DEFAULT_ALG = "HS256";
    
    private String typ;
    
    private String alg;
    
    public String getTyp() {
        return typ;
    }
    
    public void setTyp(String typ) {
        this.typ = typ;
    }
    
    public String getAlg() {
        return alg;
    }
    
    public void setAlg(String alg) {
        this.alg = alg;
    }

}
