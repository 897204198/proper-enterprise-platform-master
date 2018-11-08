package com.proper.enterprise.platform.core.utils.http

import org.springframework.http.HttpStatus
import spock.lang.Specification

class HttpsClientSpec extends Specification {

    def basePath = 'com/proper/enterprise/platform/core/utils/http'

//    def "Request with certificate"() {
//        def is = this.getClass().getClassLoader().getResourceAsStream("$basePath/certificates")
//        HttpsClient hc = HttpsClient.withCertificates(is, KeyStore.getDefaultType(), 'pwd')
//        def res = hc.get('https://publicobject.com/helloworld.txt')
//
//        expect:
//        res.getStatusCode() == HttpStatus.OK
//        println res.getBody()
//    }

    def "Request with key store"() {
        HttpsClient hc = HttpsClient.withKeyStore(this.getClass().getClassLoader().getResourceAsStream("$basePath/cert.p12"), 'PKCS12', '1379027502')
        def res = hc.get('https://api.mch.weixin.qq.com/secapi/pay/refund')

        expect:
        res.getStatusCode() == HttpStatus.OK
        println res.getBody()
    }

}
