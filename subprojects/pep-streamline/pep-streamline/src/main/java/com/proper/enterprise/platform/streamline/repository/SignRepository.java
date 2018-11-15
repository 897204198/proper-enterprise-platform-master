package com.proper.enterprise.platform.streamline.repository;

import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import com.proper.enterprise.platform.streamline.entity.SignEntity;

public interface SignRepository extends BaseJpaRepository<SignEntity, String> {

    /**
     * 通过签名获取注册信息
     *
     * @param signature 签名
     * @return 注册信息
     */
    SignEntity findBySignature(String signature);

    /**
     * 通过业务Id获取注册信息
     *
     * @param businessId 业务Id
     * @return 注册信息
     */
    SignEntity findByBusinessId(String businessId);

    /**
     * 通过业务Id删除注册信息
     *
     * @param businessId 业务Id
     * @return Boolean
     */
    Integer deleteByBusinessId(String businessId);
}
