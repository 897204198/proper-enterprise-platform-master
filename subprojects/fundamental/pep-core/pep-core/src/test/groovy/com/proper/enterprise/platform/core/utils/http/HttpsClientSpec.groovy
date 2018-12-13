package com.proper.enterprise.platform.core.utils.http

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
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

    def "test connectException"() {
        def url = "http://localhost:8080/"
        def data = '{"user":"123"}'
        def headers = ['h1': 'header1', 'h2': 'header2']
        HttpsClient hc = HttpsClient.initClient(2)
        when:
        hc.get(url, headers)
        then:
        thrown(ConnectException)

        when:
        hc.post(url, headers, MediaType.APPLICATION_FORM_URLENCODED, data)
        then:
        thrown(ConnectException)

        when:
        hc.put(url, headers, MediaType.APPLICATION_FORM_URLENCODED, data)
        then:
        thrown(ConnectException)

        when:
        hc.delete(url, headers, MediaType.APPLICATION_FORM_URLENCODED, data)
        then:
        thrown(ConnectException)

    }

}
