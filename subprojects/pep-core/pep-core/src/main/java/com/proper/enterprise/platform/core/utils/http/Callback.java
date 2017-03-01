package com.proper.enterprise.platform.core.utils.http;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface Callback {

    void onSuccess(ResponseEntity<byte[]> responseEntity);

    void onError(IOException ioe);

}
