package com.proper.enterprise.platform.core.utils.sort

import spock.lang.Specification
import spock.lang.Unroll

class CNStrComparatorSpec extends Specification {

    @Unroll
    def "#input after sort is #output"() {
        Collections.sort(input, new CNStrComparator())

        expect:
        input == output

        where:
        input                                               | output
        ['李晨', '王宝强', '王叔叔', '何炅' ,'沈凌' , '单田芳']   | ['单田芳', '何炅', '李晨', '沈凌', '王宝强', '王叔叔']
        ['李晨', '王宝强', '汪叔叔', '何炅' ,'沈凌' , '单田芳']   | ['单田芳', '何炅', '李晨', '沈凌', '汪叔叔', '王宝强']
        ['网', '王', '汪', '忘']                              | ['汪', '王', '网', '忘']
    }

}
