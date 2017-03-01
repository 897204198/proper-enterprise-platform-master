package com.proper.enterprise.platform.core.utils

import spock.lang.Specification
import spock.lang.Unroll


class PinyinUtilSpec extends Specification {

    @Unroll
    def "Pinyin of #input is #result"() {
        expect:
        result == PinyinUtil.quanpin(input)

        where:
        input       | result
        ''          | ''
        null        | ''
        '普日软件'    | 'puriruanjian'
        '好似'       | 'haosi'
        '似的'       | 'side'
        '混1a搭'     | 'hun1ada'
    }

}
