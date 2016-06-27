package com.proper.enterprise.platform.core.repository.entity

import com.proper.enterprise.platform.core.PEPConstants

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "pep_test_mock")
public class MockEntity {

    private static final long serialVersionUID = PEPConstants.VERSION;

    public MockEntity(String name) {
        this.name = name;
    }

    @Id
    String id;

    String name;

    String attr1;

}
