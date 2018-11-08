package com.proper.enterprise.platform.core.jpa.converter

import com.proper.enterprise.platform.core.CoreProperties
import com.proper.enterprise.platform.core.jpa.repository.NativeRepository
import com.proper.enterprise.platform.core.jpa.repository.mock.entity.MockEntity
import com.proper.enterprise.platform.core.jpa.repository.mock.repository.MockRepository
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class AESStringConverterTest extends AbstractJPATest {

    @Autowired
    MockRepository repository

    @Autowired
    NativeRepository nativeRepository

    @Autowired
    CoreProperties coreProperties


    @Test
    void secretData() {
        def plainData = 'plain text data'

        def entity = new MockEntity(plainData)
        entity.setAttr1(plainData)

        entity = repository.save(entity)
        assert entity.getName() == plainData
        assert entity.getAttr1() == plainData

        def obj = nativeRepository.executeQuery("SELECT name, attr1 FROM pep_test_mock WHERE id = '${entity.getId()}'").first()
        assert obj[0] == plainData
        assert obj[1] > '' && obj[1] != plainData

        def converter = new AESStringConverter()
        converter.convertToDatabaseColumn(plainData) == obj[1]
        converter.convertToEntityAttribute(obj[1]) == plainData
    }

    @Test
    void errKey() {
        coreProperties.setSecretAesKey('err_key_length')
        def converter = new AESStringConverter()
        assert converter.convertToDatabaseColumn('err') == ''
        assert converter.convertToEntityAttribute('err') == ''
    }

    @Test
    void handleNull() {
        def converter = new AESStringConverter()
        assert converter.convertToDatabaseColumn(null) == null
        assert converter.convertToDatabaseColumn('') == ''
        assert converter.convertToEntityAttribute(null) == null
        assert converter.convertToEntityAttribute('') == ''
    }

}
