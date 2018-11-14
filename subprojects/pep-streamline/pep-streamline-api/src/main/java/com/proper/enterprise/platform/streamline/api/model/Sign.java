package com.proper.enterprise.platform.streamline.api.model;

import com.proper.enterprise.platform.core.api.IBase;

/**
 * StreamLine模型
 */
public interface Sign extends IBase {

    /**
     * 获取客户端token
     *
     * @return 客户端token
     */
    String getClientToken();

    /**
     * 设置客户端token
     *
     * @param clientToken 客户端token
     */
    void setClientToken(String clientToken);

    /**
     * 获取服务端标识
     *
     * @return 服务端标识
     */
    String getServiceKey();

    /**
     * 设置服务端标识
     */
    void setServiceKey();

}
