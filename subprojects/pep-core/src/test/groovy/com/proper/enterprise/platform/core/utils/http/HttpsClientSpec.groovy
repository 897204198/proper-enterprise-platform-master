package com.proper.enterprise.platform.core.utils.http

import com.proper.enterprise.platform.test.utils.TestResource
import org.springframework.http.HttpStatus
import spock.lang.Specification

import java.security.KeyStore

class HttpsClientSpec extends Specification {

    def basePath = 'com/proper/enterprise/platform/core/utils/http'

    def "Request with certificate"() {
        def is = TestResource.inputStream("$basePath/certificates")
        HttpsClient hc = HttpsClient.withCertificates(is, KeyStore.getDefaultType(), 'pwd')
        def res = hc.get('https://publicobject.com/helloworld.txt')

        expect:
        res.getStatusCode() == HttpStatus.OK
        println res.getBody()
    }

    def "Request with key store"() {
        HttpsClient hc = HttpsClient.withKeyStore(TestResource.inputStream("$basePath/cert.p12"), 'PKCS12', '1379027502')
        def res = hc.get('https://api.mch.weixin.qq.com/secapi/pay/refund')

        expect:
        res.getStatusCode() == HttpStatus.OK
        println res.getBody()
    }

}
