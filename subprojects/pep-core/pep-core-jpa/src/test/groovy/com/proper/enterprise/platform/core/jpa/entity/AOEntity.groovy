package com.proper.enterprise.platform.core.jpa.entity

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.jpa.annotation.CacheEntity

import javax.persistence.*

@Entity
@Table(name = "pep_test_a")
@CacheEntity
public class AOEntity extends BaseEntity {

    private static final long serialVersionUID = PEPConstants.VERSION;

    public AOEntity() { }

    public AOEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Column(unique = true, nullable = false)
    String username;

    @Column(nullable = false)
    String password;

    @OneToOne
    BOEntity b;

    @ManyToMany
    Collection<COEntity> cEntities;

    String description;

}
