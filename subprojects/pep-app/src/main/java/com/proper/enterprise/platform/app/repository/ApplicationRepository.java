package com.proper.enterprise.platform.app.repository;

import com.proper.enterprise.platform.app.entity.ApplicationEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicationRepository extends BaseJpaRepository<ApplicationEntity, String> {

    /**
     * 根据类别的code获取应用的集合
     * @param code code
     * @return 应用集合
     */
    List<ApplicationEntity> findAllByCode(String code);

    /**
     * 获取默认应用
     * @return 应用的集合
     */
    List<ApplicationEntity> findByDefaultValueTrue();

    /**
     * 根据name 和page 查询
     * @param code code
     * @param applicationName 应用名
     * @param applicationPage 应用页
     * @param pageable 分页参数
     * @return 应用集合并分页
     */
    @Query("select t from ApplicationEntity t where t.code like %?1% "
            + "and t.name like %?2% "
            + "and t.page like %?3% "
           )
    Page<ApplicationEntity> findPagination(String code, String applicationName, String applicationPage, Pageable pageable);
}
