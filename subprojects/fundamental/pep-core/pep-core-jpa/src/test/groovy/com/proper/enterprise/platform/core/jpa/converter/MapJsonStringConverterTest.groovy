package com.proper.enterprise.platform.core.jpa.converter

import com.proper.enterprise.platform.core.jpa.repository.NativeRepository
import com.proper.enterprise.platform.core.jpa.repository.mock.entity.MockEntity
import com.proper.enterprise.platform.core.jpa.repository.mock.repository.MockRepository
import com.proper.enterprise.platform.test.AbstractJPATest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class MapJsonStringConverterTest extends AbstractJPATest {

    @Autowired
    MockRepository repository

    @Autowired
    NativeRepository nativeRepository

    @Test
    void testConvertString(){
        def plainData = 'plain text data'
        Map<String, String> map = new HashMap()
        map.put("key", "value")
        def entity = new MockEntity(plainData)
        entity.setAttr2(map)
        repository.save(entity)

        MapJsonStringConverter mapJsonString = new MapJsonStringConverter()
        assert mapJsonString.convertToDatabaseColumn(map).equals("{\"key\":\"value\"}")
    }

    @Test
    void testConvertMap(){
        def str = '{"questionnaireNo":"qnnre1"}'
        def entity = new MockEntity(str)
        entity.setAttr1(str)
        repository.save(entity)

        MapJsonStringConverter mapJsonString = new MapJsonStringConverter()
        assert mapJsonString.convertToEntityAttribute(str).get('questionnaireNo') == 'qnnre1'

    }
}
