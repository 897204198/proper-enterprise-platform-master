package com.proper.enterprise.platform.template.repository;

import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import com.proper.enterprise.platform.template.entity.TemplateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TemplateRepository extends BaseJpaRepository<TemplateEntity, String> {

    /**
     * 查询指定业务的模板
     *
     * @param code 关键字
     * @return 模板
     */
    TemplateEntity findByCode(String code);

    /**
     * 查询启用状态的模板列表
     *
     * @return 启用状态的模板列表
     */
    List<TemplateEntity> findByEnableTrue();

    /**
     * 分页
     *
     * @param query    查询内容
     * @param pageable 分页信息
     * @return 分页
     */
    @Query("select t from TemplateEntity t where t.code like %:query% "
        + "or t.name like %:query% "
        + "or t.description like %:query% "
    )
    Page<TemplateEntity> findPagination(@Param("query")String query, Pageable pageable);

}
