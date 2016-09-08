package com.proper.enterprise.platform.core.utils.http;

import okhttp3.OkHttpClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

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

    public static ResponseEntity<String> get(String url) throws IOException {
        return perform(client, url, GET, null, null);
    }

    public static ResponseEntity<String> post(String url, MediaType type, String data) throws IOException {
        return perform(client, url, POST, type, data);
    }

    public static ResponseEntity<String> put(String url, MediaType type, String data) throws IOException {
        return perform(client, url, PUT, type, data);
    }

    public static ResponseEntity<String> delete(String url, MediaType type, String data) throws IOException {
        return perform(client, url, DELETE, type, data);
    }

    public static ResponseEntity<String> delete(String url) throws IOException {
        return perform(client, url, DELETE, null, null);
    }

}
