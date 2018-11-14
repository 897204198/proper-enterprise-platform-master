package com.proper.enterprise.platform.streamline.client.result;

import com.proper.enterprise.platform.streamline.sdk.status.SignStatus;

public class Result {

    private SignStatus status;

    private String message;

    public SignStatus getStatus() {
        return status;
    }

    public void setStatus(SignStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
