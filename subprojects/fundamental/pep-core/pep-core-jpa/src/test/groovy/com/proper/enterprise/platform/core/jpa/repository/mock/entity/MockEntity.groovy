package com.proper.enterprise.platform.core.jpa.repository.mock.entity

import com.proper.enterprise.platform.core.PEPVersion
import com.proper.enterprise.platform.core.jpa.converter.AESStringConverter
import com.proper.enterprise.platform.core.jpa.converter.MapJsonStringConverter
import org.hibernate.annotations.GenericGenerator

import javax.persistence.*

@Entity
@Table(name = "pep_test_mock")
class MockEntity {

    private static final long serialVersionUID = PEPVersion.VERSION

    MockEntity(String name) {
        this.name = name
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    String id

    String name

    @Convert(converter = AESStringConverter.class)
    String attr1

    @Convert(converter = MapJsonStringConverter.class)
    Map<String, String> attr2

}
