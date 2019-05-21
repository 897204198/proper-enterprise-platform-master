package com.proper.enterprise.platform.workflow.rule;

import com.proper.enterprise.platform.workflow.service.impl.PEPCandidateGroupExtQueryImpl;
import com.proper.enterprise.platform.workflow.util.CandidateIdUtil;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("testGroupCandidateRule")
public class TestGroupRuleServiceImpl {

    public List<String> setCandidateRule(ExecutionEntity execution) {
        List<String> candidateGroupIds = new ArrayList<>();
        candidateGroupIds.add("group2");
        return CandidateIdUtil.encode(candidateGroupIds, PEPCandidateGroupExtQueryImpl.GROUP_CONF_CODE);
    }
}
