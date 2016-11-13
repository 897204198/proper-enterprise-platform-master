package com.proper.enterprise.platform.core.utils.http

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

class HttpClientSpec extends Specification {

    def "Using all http methods"() {
        def url = 'https://server.propersoft.cn/teamcity/login.html'
        def data = '{"user":"123"}'
        def headers = ['h1': 'header1', 'h2': 'header2']

        expect:
        HttpClient.post(url, MediaType.APPLICATION_FORM_URLENCODED, data).statusCode == HttpStatus.OK
        HttpClient.post(url, headers, MediaType.APPLICATION_FORM_URLENCODED, data).statusCode == HttpStatus.OK
        HttpClient.put(url, MediaType.APPLICATION_JSON, data).statusCode == HttpStatus.METHOD_NOT_ALLOWED
        HttpClient.put(url, headers, MediaType.APPLICATION_JSON, data).statusCode == HttpStatus.METHOD_NOT_ALLOWED
        HttpClient.get(url).statusCode == HttpStatus.OK
        HttpClient.get(url, headers).statusCode == HttpStatus.OK
        HttpClient.delete(url, MediaType.APPLICATION_FORM_URLENCODED, data).statusCode == HttpStatus.METHOD_NOT_ALLOWED
        HttpClient.delete(url, headers, MediaType.APPLICATION_FORM_URLENCODED, data).statusCode == HttpStatus.METHOD_NOT_ALLOWED
        HttpClient.delete(url).statusCode == HttpStatus.METHOD_NOT_ALLOWED
        HttpClient.delete(url, headers).statusCode == HttpStatus.METHOD_NOT_ALLOWED
    }

    def "Could get stream"() {
        def r = HttpClient.get('https://server.propersoft.cn/teamcity/img/collapse.png')

        expect:
        r.getBody() != null
        r.getHeaders().getContentType() == MediaType.IMAGE_PNG
    }

}
