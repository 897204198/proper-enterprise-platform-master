package com.proper.enterprise.platform.streamline.sdk.request;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 签名参数
 */
public class SignRequest {

    /**
     * 业务Id
     */
    @NotEmpty(message = "{streamline.addSign.businessId.notEmpty}")
    private String businessId;

    /**
     * 用户名
     */
    @NotEmpty(message = "{streamline.addSign.userName.notEmpty}")
    private String userName;

    /**
     * 密码
     */
    @NotEmpty(message = "{streamline.addSign.password.notEmpty}")
    private String password;

    /**
     * 服务端唯一标识
     */
    @NotEmpty(message = "{streamline.addSign.serviceKey.notEmpty}")
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
