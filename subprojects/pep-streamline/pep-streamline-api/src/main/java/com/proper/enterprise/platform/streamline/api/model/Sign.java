package com.proper.enterprise.platform.streamline.api.model;

import com.proper.enterprise.platform.core.api.IBase;

/**
 * StreamLine模型
 */
public interface Sign extends IBase {

    /**
     * 获取业务Id(即用户Id)
     *
     * @return 业务Id
     */
    String getBusinessId();

    /**
     * 设置业务Id
     *
     * @param businessId 业务Id
     */
    void setBusinessId(String businessId);

    /**
     * 获取签名
     *
     * @return 签名
     */
    String getSignature();

    /**
     * 设置签名
     *
     * @param signature 签名
     */
    void setSignature(String signature);

    /**
     * 获取服务端标识
     *
     * @return 服务端标识
     */
    String getServiceKey();

    /**
     * 设置服务端标识
     *
     * @param serviceKey 服务端标识
     */
    void setServiceKey(String serviceKey);

}
