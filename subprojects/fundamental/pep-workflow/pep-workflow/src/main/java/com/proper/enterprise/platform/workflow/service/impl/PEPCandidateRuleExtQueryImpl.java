package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.workflow.entity.WFIdmRuleConfEntity;
import com.proper.enterprise.platform.workflow.model.PEPCandidateModel;
import com.proper.enterprise.platform.workflow.service.PEPCandidateExtQuery;
import com.proper.enterprise.platform.workflow.service.WFIdmRuleConfService;
import com.proper.enterprise.platform.workflow.util.WFIdmQueryConfUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("pepCandidaterRuleExtQuery")
public class PEPCandidateRuleExtQueryImpl implements PEPCandidateExtQuery {

    private WFIdmRuleConfService wfIdmRuleConfService;

    public PEPCandidateRuleExtQueryImpl(WFIdmRuleConfService wfIdmRuleConfService) {
        this.wfIdmRuleConfService = wfIdmRuleConfService;
    }

    public static final String RULE_CONF_CODE = "RULE";

    @Override
    public String getType() {
        return RULE_CONF_CODE;
    }

    @Override
    public List<PEPCandidateModel> findCandidatesByUser(String userId) {
        return null;
    }

    @Override
    public PEPCandidateModel findCandidateById(String id) {
        return convert(wfIdmRuleConfService.findByRule(id));
    }

    @Override
    public List<PEPCandidateModel> findAllCandidates() {
        return convert(wfIdmRuleConfService.findAllByOrderBySort());
    }

    @Override
    public List<PEPCandidateModel> findCandidatesByNameLike(String name) {
        return null;
    }

    private List<PEPCandidateModel> convert(Collection<WFIdmRuleConfEntity> rules) {
        List<PEPCandidateModel> pepCandidateModels = new ArrayList<>();
        if (CollectionUtil.isEmpty(rules)) {
            return pepCandidateModels;
        }
        for (WFIdmRuleConfEntity rule : rules) {
            pepCandidateModels.add(convert(rule));
        }
        return pepCandidateModels;
    }

    private PEPCandidateModel convert(WFIdmRuleConfEntity wfIdmRuleConfEntity) {
        PEPCandidateModel pepCandidateModel = new PEPCandidateModel();
        pepCandidateModel.setId(wfIdmRuleConfEntity.getRule());
        pepCandidateModel.setType(getType());
        pepCandidateModel.setTypeName(WFIdmQueryConfUtil.findByType(getType()).getName());
        pepCandidateModel.setName(wfIdmRuleConfEntity.getName());
        return pepCandidateModel;
    }

}
