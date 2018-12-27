package com.proper.enterprise.platform.core.utils.http;

import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.PEPPropertiesLoader;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * HTTP 客户端工具类
 */
public class HttpClient extends ClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);

    private static OkHttpClient client = new OkHttpClient();

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    protected HttpClient() { }

    /*
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new HttpClient();
    }

    public static ResponseEntity<byte[]> get(String url) throws IOException {
        return perform(client, url, GET, null, null, null);
    }

    public static void get(String url, Callback callback) {
        perform(client, url, GET, null, null, null, callback);
    }

    public static void get(String url, long connectionTimeout, Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS).build();
        perform(client, url, GET, null, null, null, callback);
    }

    public static ResponseEntity<byte[]> get(String url, int timeout) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(timeout, TimeUnit.MILLISECONDS).build();
        return perform(client, url, GET, null, null, null);
    }

    public static ResponseEntity<byte[]> get(String url, Map<String, String> headers) throws IOException {
        return perform(client, url, GET, headers, null, null);
    }

    public static ResponseEntity<byte[]> get(String url, Map<String, String> headers, int executionCount) throws IOException {
        OkHttpClient client = initClient(executionCount);
        return perform(client, url, GET, headers, null, null);
    }

    public static ResponseEntity<byte[]> post(String url, MediaType type, String data) throws IOException {
        return perform(client, url, POST, null, type, data);
    }

    public static ResponseEntity<byte[]> post(String url, MediaType type, String data, int timeout) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(timeout, TimeUnit.MILLISECONDS).build();
        return perform(client, url, POST, null, type, data);
    }

    public static void post(String url, MediaType type, String data, Callback callback) {
        perform(client, url, POST, null, type, data, callback);
    }

    public static void post(String url, MediaType type, String data, long connectionTimeout, Callback callback) {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS).build();
        perform(client, url, POST, null, type, data, callback);
    }

    public static ResponseEntity<byte[]> post(String url, Map<String, String> headers, MediaType type, String data) throws IOException {
        return perform(client, url, POST, headers, type, data);
    }

    public static ResponseEntity<byte[]> post(String url, Map<String, String> headers, MediaType type, String data,
                                              int executionCount) throws IOException {
        OkHttpClient client = initClient(executionCount);
        return perform(client, url, POST, headers, type, data);
    }

    public static ResponseEntity<byte[]> put(String url, MediaType type, String data) throws IOException {
        return perform(client, url, PUT, null, type, data);
    }

    public static ResponseEntity<byte[]> put(String url, Map<String, String> headers, MediaType type, String data) throws IOException {
        return perform(client, url, PUT, headers, type, data);
    }

    public static ResponseEntity<byte[]> put(String url, Map<String, String> headers, MediaType type, String data,
                                             int executionCount) throws IOException {
        OkHttpClient client = initClient(executionCount);
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

    public static ResponseEntity<byte[]> delete(String url, Map<String, String> headers, MediaType type, String data,
                                                int executionCount) throws IOException {
        OkHttpClient client = initClient(executionCount);
        return perform(client, url, DELETE, headers, type, data);
    }

    /**
     * 使用平台默认的字符编码，将请求参数map转换成form表单APPLICATION_FORM_URLENCODED字符串
     *
     * @param params 参数 map
     * @return 字符串表示
     */
    public static String getFormUrlEncodedData(Map<String, Object> params) {
        try {
            return getFormUrlEncodedData(params, PEPPropertiesLoader.load(CoreProperties.class).getCharset());
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return "";
        }
    }

    /**
     * 将请求参数map转换成form表单APPLICATION_FORM_URLENCODED字符串
     *
     * @param params 请求参数map
     * @param encode 字符串编码格式
     * @return 存储APPLICATION_FORM_URLENCODED的字符串
     * @throws UnsupportedEncodingException 不支持的字符集
     */
    public static String getFormUrlEncodedData(Map<String, Object> params, String encode) throws UnsupportedEncodingException {
        // 存储封装好的请求体信息
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            builder.append(entry.getKey()).append("=")
                .append(URLEncoder.encode(entry.getValue().toString(), encode)).append("&");
        }
        if (builder.length() > 0) {
            // 删除最后的一个"&"
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    private static OkHttpClient initClient(int executionCount) {
        HttpClientInterceptor httpClientInterceptor = new HttpClientInterceptor();
        httpClientInterceptor.setExecutionCount(executionCount);
        return new OkHttpClient.Builder()
                               .addInterceptor(httpClientInterceptor)
                               .build();
    }

}
