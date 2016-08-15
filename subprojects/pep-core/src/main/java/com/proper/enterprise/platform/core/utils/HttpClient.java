package com.proper.enterprise.platform.core.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

import java.io.IOException;

/**
 * HTTP 客户端工具类
 */
public class HttpClient {

    private static OkHttpClient client = new OkHttpClient();

    private static final String PUT = "PUT";
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private HttpClient() { }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new HttpClient();
    }

    public static ResponseEntity<String> get(String url) throws IOException {
        return perform(url, GET, null, null);
    }

    private static ResponseEntity<String> perform(String url, String method, MediaType type, String data) throws IOException {
        Request.Builder builder = new Request.Builder();
        builder = builder.url(url);
        RequestBody body = null;
        if (StringUtil.isNotNull(data)) {
            Assert.notNull(type);
            body = RequestBody.create(okhttp3.MediaType.parse(type.toString()), data);
        }
        builder = builder.method(method, body);
        Request request = builder.build();
        Response response = client.newCall(request).execute();
        return converter(response);
    }

    private static ResponseEntity<String> converter(Response response) throws IOException {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.putAll(response.headers().toMultimap());
        return new ResponseEntity<>(response.body().string(), headers, HttpStatus.valueOf(response.code()));
    }

    public static ResponseEntity<String> post(String url, MediaType type, String data) throws IOException {
        return perform(url, POST, type, data);
    }

    public static ResponseEntity<String> put(String url, MediaType type, String data) throws IOException {
        return perform(url, PUT, type, data);
    }

    public static ResponseEntity<String> delete(String url, MediaType type, String data) throws IOException {
        return perform(url, DELETE, type, data);
    }

    public static ResponseEntity<String> delete(String url) throws IOException {
        return perform(url, DELETE, null, null);
    }

}
