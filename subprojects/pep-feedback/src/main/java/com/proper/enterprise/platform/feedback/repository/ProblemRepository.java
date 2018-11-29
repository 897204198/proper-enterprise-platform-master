package com.proper.enterprise.platform.feedback.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.feedback.entity.ProblemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProblemRepository extends BaseRepository<ProblemEntity, String> {

    /**
     * 问题列表
     * @param enable 是否可用
     * @param categoryId 分类id
     * @return
     */
    List<ProblemEntity> findByEnableAndCategoryIdOrderByCreateTimeDesc(boolean enable, String categoryId);

    /**
     * 问题列表
     * @param enable 是否可用
     * @return 问题列表
     */
    List<ProblemEntity> findByEnableOrderByCreateTimeDesc(boolean enable);

    /**
     * 修改
     * @param id 问题id
     */
    @Modifying
    @Query("update ProblemEntity c set c.enable = false where c.id = :id")
    void deleteProblem(@Param("id") String id);


    /**
     * 查询热门问题
     * @param pageable 分页实体
     * @return 问题列表
     */
    @Query("select p from  ProblemEntity p where p.awesome >= p.tread and p.enable = true order by p.views desc ")
    Page<ProblemEntity> findPopulars(Pageable pageable);

}
