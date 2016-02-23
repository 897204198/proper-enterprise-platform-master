package com.proper.enterprise.platform.integration.webapp.dal.entity

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.annotation.CacheEntity
import com.proper.enterprise.platform.core.entity.BaseEntity
import com.proper.enterprise.platform.core.enums.UseStatus

import javax.persistence.*

@Entity
@Table(name = "pep_test_a")
@CacheEntity
public class AEntity extends BaseEntity {

    private static final long serialVersionUID = PEPConstants.VERSION;

    public AEntity() { }

    public AEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    @Column(unique = true, nullable = false)
    String username;
    
    @Column(nullable = false)
    String password;

    @OneToOne
    BEntity b;

    @ManyToMany
    Collection<CEntity> cEntities;

    UseStatus useStatus = UseStatus.STOP;

    String description;

}
