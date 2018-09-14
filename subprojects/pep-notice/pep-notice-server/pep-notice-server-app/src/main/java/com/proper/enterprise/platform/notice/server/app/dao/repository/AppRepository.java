package com.proper.enterprise.platform.notice.server.app.dao.repository;

import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import com.proper.enterprise.platform.notice.server.app.dao.entity.AppEntity;

public interface AppRepository extends BaseJpaRepository<AppEntity, String> {

    /**
     * 根据appKey获取应用
     *
     * @param appKey 应用唯一标识
     * @return 应用
     */
    AppEntity findByAppKey(String appKey);
}
