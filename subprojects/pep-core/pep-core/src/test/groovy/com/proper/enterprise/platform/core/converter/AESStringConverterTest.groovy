package com.proper.enterprise.platform.core.converter
import com.proper.enterprise.platform.core.repository.NativeRepository
import com.proper.enterprise.platform.core.repository.mock.entity.MockEntity
import com.proper.enterprise.platform.core.repository.mock.repository.MockRepository
import com.proper.enterprise.platform.core.utils.ConfCenter
import com.proper.enterprise.platform.test.AbstractTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class AESStringConverterTest extends AbstractTest {

    @Autowired
    MockRepository repository

    @Autowired
    NativeRepository nativeRepository

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
        System.setProperty('core.secret.aes.key', 'err_key_length')
        ConfCenter.reload()

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
