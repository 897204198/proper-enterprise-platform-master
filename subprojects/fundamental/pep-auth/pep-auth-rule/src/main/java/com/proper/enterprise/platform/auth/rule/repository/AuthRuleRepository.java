package com.proper.enterprise.platform.auth.rule.repository;

import com.proper.enterprise.platform.auth.rule.entity.AuthRuleEntity;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthRuleRepository extends BaseJpaRepository<AuthRuleEntity, String> {

    /**
     * 根据修改时间倒叙查询列表
     * 不传参数 spring-data风格的查询会返回空
     *
     * @param code 编码
     * @param name 名称
     * @param type 类型
     * @return 实体集合
     */
    @Query("select t from AuthRuleEntity t where (t.code=:code or :code is null )"
        + " and (t.name=:name or :name is null )"
        + " and (t.type=:type or :type is null ) order by t.sort")
    List<AuthRuleEntity> findAll(@Param("code") String code, @Param("name") String name, @Param("type") String type);

    /**
     * 根据修改时间倒叙分页查询
     * 建议优先使用 spring-data风格的查询 若查询的条件过多 spring-data风格写起来过于复杂 考虑使用@Query实现查询
     *
     * @param code     编码
     * @param name     名称
     * @param type     类型
     * @param pageable 分页参数
     * @return 分页实体集合
     */
    @Query("select t from AuthRuleEntity t where (t.code=:code or :code is null )"
        + " and (t.name=:name or :name is null )"
        + " and (t.type=:type or :type is null ) order by t.sort")
    Page<AuthRuleEntity> findAll(@Param("code") String code, @Param("name") String name, @Param("type") String type, Pageable pageable);


    /**
     * 根据编码获取规则
     *
     * @param code 编码
     * @return 规则实体
     */
    AuthRuleEntity findByCode(String code);

}
