package com.proper.enterprise.platform.integration.webapp.dal.entity;

import com.proper.enterprise.platform.core.entity.BaseEntity;
import com.proper.enterprise.platform.core.enums.UseStatus;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "pep_test_a")
@Cacheable
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

    @ManyToMany
    private Collection<BEntity> bs;

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

    public Collection<BEntity> getBs() {
        return bs;
    }

    public void setBs(Collection<BEntity> bs) {
        this.bs = bs;
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
}
