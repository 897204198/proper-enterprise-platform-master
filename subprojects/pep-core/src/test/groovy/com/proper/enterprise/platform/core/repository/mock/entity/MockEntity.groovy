package com.proper.enterprise.platform.core.repository.mock.entity

import com.proper.enterprise.platform.core.PEPConstants
import com.proper.enterprise.platform.core.converter.AESConverter
import org.hibernate.annotations.GenericGenerator

import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.GeneratedValue
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
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    String id;

    String name;

    @Convert(converter = AESConverter.class)
    String attr1;

}
