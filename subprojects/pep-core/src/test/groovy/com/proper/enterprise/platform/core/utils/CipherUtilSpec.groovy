package com.proper.enterprise.platform.core.utils

import spock.lang.Specification


class CipherUtilSpec extends Specification {

    def key = '7AB0B2F6316C2921'
    def content = '<REQ><HOS_ID><![CDATA[1001]]></HOS_ID></REQ>'

    def "AES encrypt and decrypt"() {
        def c = CipherUtil.getInstance('AES', 'ECB', 'PKCS5Padding', key, 128)
        def encrypted = c.encrypt(content)
        def decrypted = c.decrypt(encrypted)

        expect:
        assert content == decrypted
    }

}
