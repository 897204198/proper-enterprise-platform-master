package com.proper.enterprise.platform.core.converter

import spock.lang.Specification

import javax.persistence.AttributeConverter


class BooleanTFConverterSpec extends Specification {

    def "Check converter for boolean attribute in JPA entity"() {
        given:
        AttributeConverter<Boolean, String> converter = new BooleanTFConverter()

        expect:
        converter.convertToDatabaseColumn(attr) == dbData
        converter.convertToEntityAttribute(dbData) == attr

        where:
        attr            | dbData
        Boolean.TRUE    | "T"
        Boolean.FALSE   | "F"
    }

}
