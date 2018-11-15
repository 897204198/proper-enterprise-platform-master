package com.proper.enterprise.platform.streamline.api.service;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

/**
 * StreamlineService通用接口
 * 根据用户名和密码采用md5方式生成客户端token
 */
@Validated
public interface StreamlineService {

    /**
     * 根据用户名和密码注册标记
     *
     * @param businessId   用户Id
     * @param userName     用户名
     * @param password     密码
     * @param serviceKey   服务端唯一标识
     */
    void addSign(@NotEmpty(message = "{streamline.addSign.businessId.notEmpty}") String businessId,
                 @NotEmpty(message = "{streamline.addSign.userName.notEmpty}") String userName,
                 @NotEmpty(message = "{streamline.addSign.password.notEmpty}") String password,
                 @NotEmpty(message = "{streamline.addSign.serviceKey.notEmpty}") String serviceKey);

    /**
     * 根据用户名和密码
     *
     * @param businessId 用户名
     */
    void deleteSign(String businessId);


    /**
     * 根据用户名和密码更新标记
     *
     * @param userName    用户名
     * @param password    密码
     * @param businessId  用户Id
     */
    void updateSign(String userName, String password, String businessId);

    /**
     * 根据用户名和密码获得服务端标识
     *
     * @param userName 用户名
     * @param password 密码
     * @return 服务端标识
     */
    String getSign(String userName, String password);

}
