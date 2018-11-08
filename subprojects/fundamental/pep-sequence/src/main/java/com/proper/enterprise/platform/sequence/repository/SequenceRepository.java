package com.proper.enterprise.platform.sequence.repository;

import com.proper.enterprise.platform.sequence.entity.SequenceEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SequenceRepository extends BaseJpaRepository<SequenceEntity, String> {

    /**
     * 根据修改时间倒叙查询列表
     *
     * @param sequenceCode 查询参数
     * @return 实体集合
     */
    @Query("select t from SequenceEntity t where (t.sequenceCode=?1 or ?1 is null )")
    List<SequenceEntity> findAll(String sequenceCode);

    /**
     * 根据修改时间倒叙分页查询
     *
     * @param sequenceCode     查询参数
     * @param pageable       分页参数
     * @return 分页实体集合
     */
    @Query("select t from SequenceEntity t where (t.sequenceCode=?1 or ?1 is null )")
    Page<SequenceEntity> findAll(String sequenceCode, Pageable pageable);

    /**
     * 根据流水Code查询
     *
     * @param sequenceCode 流水Code
     * @return 实体
     */
    SequenceEntity findBySequenceCode(String sequenceCode);

}
