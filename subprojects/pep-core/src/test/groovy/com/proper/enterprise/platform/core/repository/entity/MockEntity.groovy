package com.proper.enterprise.platform.core.repository.entity

import com.proper.enterprise.platform.core.entity.BaseEntity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "pep_test_mock")
class MockEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    String name;

    String attr1;

}
