package com.proper.enterprise.platform.core.utils.http;

import com.proper.enterprise.platform.core.utils.StringUtil;
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
        Response response = client.newCall(request).execute();
        return converter(response);
    }

    protected static ResponseEntity<byte[]> converter(Response response) throws IOException {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.putAll(response.headers().toMultimap());
        return new ResponseEntity<>(response.body().bytes(), headers, HttpStatus.valueOf(response.code()));
    }

}
