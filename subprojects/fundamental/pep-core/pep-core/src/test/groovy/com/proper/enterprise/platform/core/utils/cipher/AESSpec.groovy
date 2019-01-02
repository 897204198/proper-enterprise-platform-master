package com.proper.enterprise.platform.core.utils.cipher

import com.proper.enterprise.platform.core.CoreProperties
import com.proper.enterprise.platform.core.PEPPropertiesLoader
import spock.lang.Specification

class AESSpec extends Specification {

    def key = '7AB0B2F6316C2921'
    def content = '<REQ><HOS_ID><![CDATA[1001]]></HOS_ID></REQ>'

    def "AES encrypt and decrypt (String)"() {
        def c = new AES('ECB', 'PKCS5Padding', key)
        def encrypted = c.encrypt(content)
        def decrypted = c.decrypt(encrypted)

        expect:
        assert content == decrypted
    }

    def "AES encrypt and decrypt (byte[])"() {
        def c = new AES('ECB', 'PKCS5Padding', key)
        def encrypted = c.encrypt(content.getBytes(PEPPropertiesLoader.load(CoreProperties.class).getCharset()))
        def decrypted = c.decrypt(encrypted)

        expect:
        assert content.getBytes(PEPPropertiesLoader.load(CoreProperties.class).getCharset()) == decrypted
    }

}
