package com.proper.enterprise.platform.core.utils.http

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import spock.lang.Specification

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class HttpClientSpec extends Specification {

    def static final TEAMCITY = 'https://cloud.propersoft.cn/teamcities'

    def "Using all http methods"() {
        def url = "$TEAMCITY/login.html"
        def data = '{"user":"123"}'
        def headers = ['h1': 'header1', 'h2': 'header2']

        expect:
        HttpClient.post(url, MediaType.APPLICATION_FORM_URLENCODED, data).statusCode == HttpStatus.OK
        HttpClient.post(url, headers, MediaType.APPLICATION_FORM_URLENCODED, data).statusCode == HttpStatus.OK
        HttpClient.put(url, MediaType.APPLICATION_JSON, data).statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        HttpClient.put(url, headers, MediaType.APPLICATION_JSON, data).statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        HttpClient.get(url).statusCode == HttpStatus.OK
        HttpClient.get(url, headers).statusCode == HttpStatus.OK
        HttpClient.delete(url, MediaType.APPLICATION_FORM_URLENCODED, data).statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        HttpClient.delete(url, headers, MediaType.APPLICATION_FORM_URLENCODED, data).statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        HttpClient.delete(url).statusCode == HttpStatus.INTERNAL_SERVER_ERROR
        HttpClient.delete(url, headers).statusCode == HttpStatus.INTERNAL_SERVER_ERROR
    }

    def "Could get stream"() {
        def r = HttpClient.get("$TEAMCITY/img/collapse.png")

        expect:
        r.getBody() != null
        r.getHeaders().getContentType() == MediaType.IMAGE_PNG
    }

    def "Async request with callback"() {
        CountDownLatch latch = new CountDownLatch(3)

        def cb = new Callback() {
            @Override
            void onSuccess(ResponseEntity<byte[]> responseEntity) {
                println 'success'
                println responseEntity
                latch.countDown()
            }

            @Override
            void onError(IOException ioe) {
                println 'error'
                println ioe
                latch.countDown()
            }
        }

        expect:
        HttpClient.post("$TEAMCITY/login.html", MediaType.APPLICATION_FORM_URLENCODED, '{"user":"123"}', cb)
        HttpClient.post('https://www.google.com', MediaType.APPLICATION_FORM_URLENCODED, '{"user":"123"}', 200, cb)
        HttpClient.get('http://localhost:9090/pep', 200, cb)
        latch.await(1, TimeUnit.SECONDS)
    }

    def "test getFormUrlEncodedData"() {
        expect:
        result.toLowerCase() == HttpClient.getFormUrlEncodedData(input).toLowerCase()
        where:
        input                                                    | result
        ['key1': 'aaa']                                          | 'key1=aaa'
        ['key1': 'aaa', 'zongwen': '这个是中文']                      | 'key1=aaa&zongwen=%e8%bf%99%e4%b8%aa%e6%98%af%e4%b8%ad%e6%96%87'
        ['key1': 'aaa', 'url': 'http://www.baidu.com?a1=1&a2=2'] | 'key1=aaa&url=http%3a%2f%2fwww.baidu.com%3fa1%3d1%26a2%3d2'
    }

    def "test connectException"() {
        def url = "http://localhost:8080/"
        def data = '{"user":"123"}'
        def headers = ['h1': 'header1', 'h2': 'header2']

        when:
        def getStart = new Date().getTime()
        HttpClient.get(url, headers)
        then:
        thrown(ConnectException)
        def getSpendTime = new Date().getTime() - getStart

        when:
        getStart = new Date().getTime()
        HttpClient.get(url, headers, 2)
        then:
        thrown(ConnectException)
        assert getSpendTime * 2 < new Date().getTime() - getStart

        when:
        def postStart = new Date().getTime()
        HttpClient.post(url, headers, MediaType.APPLICATION_FORM_URLENCODED, data)
        then:
        thrown(ConnectException)
        def postSpendTime = new Date().getTime() - postStart

        when:
        postStart = new Date().getTime()
        HttpClient.post(url, headers, MediaType.APPLICATION_FORM_URLENCODED, data, 2)
        then:
        thrown(ConnectException)
        assert postSpendTime * 2 < new Date().getTime() - postStart

        when:
        def putStart = new Date().getTime()
        HttpClient.put(url, headers, MediaType.APPLICATION_FORM_URLENCODED, data)
        then:
        thrown(ConnectException)
        def putSpendTime = new Date().getTime() - putStart

        when:
        putStart = new Date().getTime()
        HttpClient.put(url, headers, MediaType.APPLICATION_FORM_URLENCODED, data, 2)
        then:
        thrown(ConnectException)
        assert putSpendTime * 2 < new Date().getTime() - putStart

        when:
        def deleteStart = new Date().getTime()
        HttpClient.delete(url, headers, MediaType.APPLICATION_FORM_URLENCODED, data)
        then:
        thrown(ConnectException)
        def deleteSpendTime = new Date().getTime() - deleteStart

        when:
        deleteStart = new Date().getTime()
        HttpClient.delete(url, headers, MediaType.APPLICATION_FORM_URLENCODED, data, 2)
        then:
        thrown(ConnectException)
        assert deleteSpendTime * 2 < new Date().getTime() - deleteStart

    }

}
