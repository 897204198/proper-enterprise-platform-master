package com.proper.enterprise.platform.notice.server.app.dao.repository;

import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import com.proper.enterprise.platform.notice.server.app.dao.entity.AppEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppRepository extends BaseJpaRepository<AppEntity, String> {

    /**
     * 根据appKey获取应用
     *
     * @param appKey 应用唯一标识
     * @return 应用
     */
    AppEntity findByAppKey(String appKey);

    /**
     * 分页查询推送消息
     *
     * @param appKey   应用唯一标识
     * @param appName  应用名称
     * @param describe 应用描述
     * @param enable   启用停用
     * @param pageable 分页参数
     * @return 分页集合
     */
    @Query("SELECT a FROM AppEntity a WHERE (a.appKey=:appKey or :appKey is null)"
        + " and (a.appName=:appName or :appName is null)"
        + " and (a.appDesc=:describe or :describe is null)"
        + " and (a.enable=:enable or :enable is null)")
    Page<AppEntity> findAll(@Param("appKey") String appKey, @Param("appName") String appName,
                            @Param("describe") String describe, @Param("enable") Boolean enable, Pageable pageable);

    /**
     * 获取appKey对应的App
     * @param appKeys appkeys
     * @return list
     */
    @Query(" SELECT a FROM AppEntity a WHERE a.appKey in :appKeys ")
    List<AppEntity> findAppsByAppKey(@Param("appKeys") List<String> appKeys);

}
