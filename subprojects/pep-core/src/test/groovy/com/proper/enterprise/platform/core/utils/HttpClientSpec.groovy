package com.proper.enterprise.platform.core.utils

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification


class HttpClientSpec extends Specification {

    def static url = 'https://server.propersoft.cn'

    def "Using all http methods"() {
        // TODO clean code
        def r1 = HttpClient.post(url, MediaType.APPLICATION_FORM_URLENCODED, '{"user":"123"}')
        def r2 = HttpClient.put(url, MediaType.APPLICATION_JSON, '{"user":"123"}')
        def r3 = HttpClient.get(url)
        def r4 = HttpClient.delete(url, MediaType.APPLICATION_FORM_URLENCODED, '{"user":"123"}')
        def r5 = HttpClient.delete(url)

        expect:
        r1.statusCode == HttpStatus.NOT_FOUND
        r2.statusCode == HttpStatus.METHOD_NOT_ALLOWED
        r3.statusCode == HttpStatus.NOT_FOUND
        r4.statusCode == HttpStatus.METHOD_NOT_ALLOWED
        r5.statusCode == HttpStatus.METHOD_NOT_ALLOWED
    }

}
