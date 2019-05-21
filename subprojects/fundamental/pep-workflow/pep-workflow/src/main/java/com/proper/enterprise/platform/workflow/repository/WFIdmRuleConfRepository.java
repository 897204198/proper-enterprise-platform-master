package com.proper.enterprise.platform.workflow.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.workflow.entity.WFIdmRuleConfEntity;

import java.util.List;

public interface WFIdmRuleConfRepository extends BaseRepository<WFIdmRuleConfEntity, String> {

    /**
     * 获取流程经办人规则集合
     *
     * @return 流程经办人规则集合
     */
    List<WFIdmRuleConfEntity> findAllByOrderBySort();


    /**
     * 根据规则内容查询规则配置
     *
     * @param rule 规则内容
     * @return 流程经办人规则配置
     */
    WFIdmRuleConfEntity findByRule(String rule);
}
