package com.proper.enterprise.platform.streamline.client;

import com.proper.enterprise.platform.streamline.client.result.Result;

public class StreamlineClient {

    public StreamlineClient(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    private String serviceKey;

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    /**
     * 根据用户名和密码注册标记
     *
     * @param userName 用户名
     * @param password 密码
     */
    public Result addSign(String userName, String password) {
        return null;
    }


    /**
     * 根据用户名和密码
     *
     * @param userName 用户名
     * @param password 密码
     */
    public Result deleteSign(String userName, String password) {
        return null;
    }
}
