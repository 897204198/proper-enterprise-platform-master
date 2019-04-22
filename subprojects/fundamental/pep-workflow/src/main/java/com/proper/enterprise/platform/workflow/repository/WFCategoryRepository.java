package com.proper.enterprise.platform.workflow.repository;

import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import com.proper.enterprise.platform.workflow.entity.WFCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface WFCategoryRepository extends BaseJpaRepository<WFCategoryEntity, String> {

    /**
     * 获取流程类别集合
     *
     * @param name         流程类别名称
     * @param code         流程类别编码
     * @param parent       流程父类别对象
     * @return 流程类别集合
     */
    @Query("SELECT t FROM WFCategoryEntity t "
        + " WHERE (t.name = :name or :name is null) "
        + " AND (t.code = :code or :code is null) "
        + " AND (t.parent = :parent or :parent is null) "
        + " ORDER BY t.sort ")
    Collection<WFCategoryEntity> findAll(@Param("name") String name,
                                         @Param("code") String code,
                                         @Param("parent") WFCategoryEntity parent);

    /**
     * 获取流程类别集合
     *
     * @param name         流程类别名称
     * @param code         流程类别编码
     * @param parent       流程父类别对象
     * @param pageable     分页信息
     * @return 流程类别集合
     */
    @Query("SELECT t FROM WFCategoryEntity t "
        + " WHERE (t.name = :name or :name is null) "
        + " AND (t.code = :code or :code is null) "
        + " AND (t.parent = :parent or :parent is null) "
        + " ORDER BY t.sort ")
    Page<WFCategoryEntity> findAll(@Param("name") String name,
                                   @Param("code") String code,
                                   @Param("parent") WFCategoryEntity parent,
                                   Pageable pageable);

    /**
     * 根据流程编码获取流程类别对象
     *
     * @param code 流程编码
     * @return 流程类别对象
     */
    Optional<WFCategoryEntity> findByCode(String code);
}
