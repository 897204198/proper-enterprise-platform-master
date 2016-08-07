package com.proper.enterprise.platform.workflow

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class EditorSourceSpec extends Specification {

    def "Checking documentation of initial source"() {
        def result = EditorSource.initialSource('procId', 'name', description)

        ObjectMapper mapper = new ObjectMapper()
        JsonNode node = mapper.readValue(result, JsonNode.class)

        expect:
        node.findValue('properties').get('documentation').textValue() == doc

        where:
        description | doc
        null        | ''
        ''          | ''
        ' '         | ''
        '中\r文'     | '中\r文'
        '123'       | '123'
    }

}
