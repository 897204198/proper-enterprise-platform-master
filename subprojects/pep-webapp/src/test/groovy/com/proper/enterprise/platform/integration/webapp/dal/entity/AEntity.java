package com.proper.enterprise.platform.integration.webapp.dal.entity;

import com.proper.enterprise.platform.core.annotation.CacheEntity;
import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.core.enums.UseStatus;

import javax.persistence.*;

@Entity
@Table(name = "pep_test_a")
@CacheEntity
public class AEntity extends BaseEntity {

    private static final long serialVersionUID = -1491308886515558807L;

    public AEntity() { }

    public AEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;

    @OneToOne
    private BEntity b;

    private UseStatus useStatus = UseStatus.STOP;

    private String description;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UseStatus getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(UseStatus useStatus) {
        this.useStatus = useStatus;
    }

    public BEntity getB() {
        return b;
    }

    public void setB(BEntity b) {
        this.b = b;
    }
}
