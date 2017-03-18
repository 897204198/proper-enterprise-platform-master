package com.proper.enterprise.platform.core.utils.http;

import com.proper.enterprise.platform.core.utils.StringUtil;
import okhttp3.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Map;

public class ClientUtil {

    protected static final String PUT = "PUT";
    protected static final String GET = "GET";
    protected static final String POST = "POST";
    protected static final String DELETE = "DELETE";

    protected static ResponseEntity<byte[]> perform(OkHttpClient client,
                                                    String url, String method,
                                                    Map<String, String> headers, MediaType type,
                                                    String data) throws IOException {
        Response response = createCall(client, url, method, headers, type, data).execute();
        return converter(response);
    }

    private static Call createCall(OkHttpClient client,
                            String url, String method,
                            Map<String, String> headers, MediaType type,
                            String data) {
        Request.Builder builder = new Request.Builder();
        builder = builder.url(url);
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder = builder.addHeader(header.getKey(), header.getValue());
            }
        }
        RequestBody body = null;
        if (StringUtil.isNotNull(data)) {
            Assert.notNull(type);
            body = RequestBody.create(okhttp3.MediaType.parse(type.toString()), data);
        }
        builder = builder.method(method, body);
        Request request = builder.build();
        return client.newCall(request);
    }

    protected static ResponseEntity<byte[]> converter(Response response) throws IOException {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.putAll(response.headers().toMultimap());
        return new ResponseEntity<>(response.body().bytes(), headers, HttpStatus.valueOf(response.code()));
    }

    /**
     * 异步发送请求，需要回调方法
     *
     * @param client    http 客户端
     * @param url       请求 url
     * @param method    请求方法
     * @param headers   请求头
     * @param type      media type
     * @param data      数据
     * @param callback  回调类
     */
    protected static void perform(OkHttpClient client,
                                  String url, String method,
                                  Map<String, String> headers, MediaType type,
                                  String data, final Callback callback) {
        createCall(client, url, method, headers, type, data).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onSuccess(converter(response));
            }
        });
    }

}
