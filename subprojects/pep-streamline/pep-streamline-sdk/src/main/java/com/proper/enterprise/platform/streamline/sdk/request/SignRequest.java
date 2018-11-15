package com.proper.enterprise.platform.streamline.sdk.request;

import com.proper.enterprise.platform.core.utils.JSONUtil;

/**
 * 签名参数
 */
public class SignRequest {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 业务Id
     */
    private String businessId;

    /**
     * 密码
     */
    private String password;

    /**
     * 服务端唯一标识
     */
    private String serviceKey;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    @Override
    public String toString() {
        return JSONUtil.toJSONIgnoreException(this);
    }
}
