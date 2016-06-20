package com.proper.enterprise.platform.core.repository.entity

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.annotation.CacheEntity
import org.hibernate.annotations.GenericGenerator

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "pep_test_b")
@CacheEntity
public class BEntity {

    private static final long serialVersionUID = PEPConstants.VERSION;

    public BEntity(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    String id;

    @Column(nullable = false, unique = true)
    String name;

    String attr1;

}
