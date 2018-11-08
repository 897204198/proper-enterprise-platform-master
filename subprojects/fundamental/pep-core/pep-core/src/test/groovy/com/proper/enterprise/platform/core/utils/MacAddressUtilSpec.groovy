package com.proper.enterprise.platform.core.utils

import spock.lang.Specification

class MacAddressUtilSpec extends Specification {

    def "MAC address converter"() {
        def full = MacAddressUtil.getFullMacAddress()
        def compressed = MacAddressUtil.getCompressedMacAddress()

        expect:
        MacAddressUtil.compress(full) == compressed
        MacAddressUtil.decompress(compressed) == full
    }

}
