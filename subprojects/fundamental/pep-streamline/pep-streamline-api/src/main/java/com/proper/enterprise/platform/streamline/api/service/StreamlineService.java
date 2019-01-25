package com.proper.enterprise.platform.streamline.api.service;

import com.proper.enterprise.platform.streamline.sdk.request.SignRequest;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

/**
 * StreamlineService通用接口
 * 根据用户名和密码采用md5方式生成客户端token
 */
@Validated
public interface StreamlineService {

    /**
     * 根据用户名和密码注册标记
     *
     * @param businessId 用户Id
     * @param userName   用户名
     * @param password   密码
     * @param serviceKey 服务端唯一标识
     */
    void addSign(@NotEmpty(message = "{streamline.addSign.businessId.notEmpty}") String businessId,
                 @NotEmpty(message = "{streamline.addSign.userName.notEmpty}") String userName,
                 @NotEmpty(message = "{streamline.addSign.password.notEmpty}") String password,
                 @NotEmpty(message = "{streamline.addSign.serviceKey.notEmpty}") String serviceKey);

    /**
     * 批量注册标记
     *
     * @param signRequests 注册信息集合
     */
    void addSigns(@Valid Collection<SignRequest> signRequests);

    /**
     * 根据用户名和密码删除注册信息
     *
     * @param businessIds 用户名集合(,分隔)
     */
    void deleteSigns(String businessIds);


    /**
     * 根据用户名和密码更新标记
     *
     * @param userName   用户名
     * @param password   密码
     * @param businessId 用户Id
     */
    void updateSign(String userName, String password, String businessId);

    /**
     * 批量更新标记
     *
     * @param signRequests 注册信息集合
     */
    void updateSigns(Collection<SignRequest> signRequests);

    /**
     * 根据用户名和密码获得服务端标识
     *
     * @param userName 用户名
     * @param password 密码
     * @return 服务端标识
     */
    String getSign(String userName, String password);


    /**
     * 根据签名获得服务端标识
     *
     * @param signature 签名
     * @return 服务端标识
     */
    String getSign(String signature);

}
