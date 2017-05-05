package com.proper.enterprise.platform.core.utils.http

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class HttpClientSpec extends Specification {

    def TEAMCITY = 'https://cloud.propersoft.cn/teamcities'

    def "Using all http methods"() {
        def url = "$TEAMCITY/login.html"
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
        def r = HttpClient.get("$TEAMCITY/img/collapse.png")

        expect:
        r.getBody() != null
        r.getHeaders().getContentType() == MediaType.IMAGE_PNG
    }

    def "Async request with callback"() {
        def cb = new Callback() {
            @Override
            void onSuccess(ResponseEntity<byte[]> responseEntity) {
                println 'success'
                println responseEntity
            }

            @Override
            void onError(IOException ioe) {
                println 'error'
                println ioe
            }
        }

        expect:
        HttpClient.post("$TEAMCITY/login.html", MediaType.APPLICATION_FORM_URLENCODED, '{"user":"123"}', cb)
        HttpClient.post('https://www.google.com', MediaType.APPLICATION_FORM_URLENCODED, '{"user":"123"}', cb)
    }

}
