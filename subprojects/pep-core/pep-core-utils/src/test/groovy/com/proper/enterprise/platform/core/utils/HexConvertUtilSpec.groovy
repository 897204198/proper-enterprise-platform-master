package com.proper.enterprise.platform.core.utils


import spock.lang.Specification
import spock.lang.Unroll


class HexConvertUtilSpec extends Specification {

    @Unroll
    def "#decimal to 64 digit is #digit64"() {
        expect:
        HexConvertUtil.convert10To64(decimal) == digit64
        HexConvertUtil.convert64To10(digit64) == decimal

        where:
        decimal     | digit64
        10          | 'K'
        62          | '+'
        63          | '/'
        64          | 'BA'
        20180124    | 'BM+yc'
    }

}
