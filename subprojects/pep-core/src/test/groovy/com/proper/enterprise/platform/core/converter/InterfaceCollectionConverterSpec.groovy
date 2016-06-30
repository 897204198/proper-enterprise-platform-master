package com.proper.enterprise.platform.core.converter

import spock.lang.Specification

class InterfaceCollectionConverterSpec extends Specification {

    def "Extract to interface collection"() {
        Collection<HashMap<String, String>> collection = [['a': '1'], ['b': '2'], ['c': '3']]
        Collection<Map<String, String>> c = InterfaceCollectionConverter.convert(collection)

        expect:
        c.size() == collection.size()
        c == collection
    }

}
