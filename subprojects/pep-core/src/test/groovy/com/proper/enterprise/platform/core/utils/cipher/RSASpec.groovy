package com.proper.enterprise.platform.core.utils.cipher

import spock.lang.Specification


class RSASpec extends Specification {

    def signAlgo = 'SHA1WithRSA'
    def keySize = 1024
    def content = '<REQ><HOS_ID><![CDATA[1001]]></HOS_ID></REQ>'

    def "RSA encrypt and decrypt"() {
        def c = new RSA(signAlgo)
        Map<String, String> map = c.generateKeyPair(keySize)

        def publicKey = map.publicKey
        def privateKey = map.privateKey

        def encrypted = c.encrypt(content, publicKey)
        def decrypted = c.decrypt(encrypted, privateKey)

        expect:
        assert content == decrypted
    }

    def "RSA sign and verify"() {
        def c = new RSA(signAlgo)
        Map<String, String> map = c.generateKeyPair(keySize)

        def publicKey = map.publicKey
        def privateKey = map.privateKey

        def sign = c.sign(content, privateKey)

        expect:
        c.verifySign(content, sign, publicKey)
    }

}
