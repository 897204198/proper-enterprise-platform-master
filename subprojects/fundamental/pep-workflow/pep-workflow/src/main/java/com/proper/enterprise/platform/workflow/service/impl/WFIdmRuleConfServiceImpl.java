package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.workflow.entity.WFIdmRuleConfEntity;
import com.proper.enterprise.platform.workflow.repository.WFIdmRuleConfRepository;
import com.proper.enterprise.platform.workflow.service.WFIdmRuleConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WFIdmRuleConfServiceImpl implements WFIdmRuleConfService {

    private WFIdmRuleConfRepository wfIdmRuleConfRepository;

    @Autowired
    public WFIdmRuleConfServiceImpl(WFIdmRuleConfRepository wfIdmRuleConfRepository) {
        this.wfIdmRuleConfRepository = wfIdmRuleConfRepository;
    }

    @Override
    @CacheQuery
    public List<WFIdmRuleConfEntity> findAllByOrderBySort() {
        return wfIdmRuleConfRepository.findAllByOrderBySort();
    }

    @Override
    @CacheQuery
    public WFIdmRuleConfEntity findByRule(String rule) {
        return wfIdmRuleConfRepository.findByRule(rule);
    }
}
