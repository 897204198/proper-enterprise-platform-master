package com.proper.enterprise.platform.core.utils.http;

import okhttp3.OkHttpClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;

/**
 * HTTP 客户端工具类
 */
public class HttpClient extends ClientUtil {

    private static OkHttpClient client = new OkHttpClient();

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    protected HttpClient() { }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new HttpClient();
    }

    public static ResponseEntity<byte[]> get(String url) throws IOException {
        return perform(client, url, GET, null, null, null);
    }

    public static ResponseEntity<byte[]> get(String url, Map<String, String> headers) throws IOException {
        return perform(client, url, GET, headers, null, null);
    }

    public static ResponseEntity<byte[]> post(String url, MediaType type, String data) throws IOException {
        return perform(client, url, POST, null, type, data);
    }

    public static void post(String url, MediaType type, String data, Callback callback) {
        perform(client, url, POST, null, type, data, callback);
    }

    public static ResponseEntity<byte[]> post(String url, Map<String, String> headers, MediaType type, String data) throws IOException {
        return perform(client, url, POST, headers, type, data);
    }

    public static ResponseEntity<byte[]> put(String url, MediaType type, String data) throws IOException {
        return perform(client, url, PUT, null, type, data);
    }

    public static ResponseEntity<byte[]> put(String url, Map<String, String> headers, MediaType type, String data) throws IOException {
        return perform(client, url, PUT, headers, type, data);
    }

    public static ResponseEntity<byte[]> delete(String url) throws IOException {
        return perform(client, url, DELETE, null, null, null);
    }

    public static ResponseEntity<byte[]> delete(String url, Map<String, String> headers) throws IOException {
        return perform(client, url, DELETE, headers, null, null);
    }

    public static ResponseEntity<byte[]> delete(String url, MediaType type, String data) throws IOException {
        return perform(client, url, DELETE, null, type, data);
    }

    public static ResponseEntity<byte[]> delete(String url, Map<String, String> headers, MediaType type, String data) throws IOException {
        return perform(client, url, DELETE, headers, type, data);
    }

}
