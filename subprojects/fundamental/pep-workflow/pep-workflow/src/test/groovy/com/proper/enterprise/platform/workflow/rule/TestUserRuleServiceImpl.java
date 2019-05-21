package com.proper.enterprise.platform.workflow.rule;

import com.proper.enterprise.platform.workflow.service.impl.PEPCandidateUserExtQueryImpl;
import com.proper.enterprise.platform.workflow.util.CandidateIdUtil;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("testUserCandidateRule")
public class TestUserRuleServiceImpl {

    public List<String> setCandidateRule(ExecutionEntity execution) {
        List<String> candidateUserIds = new ArrayList<>();
        candidateUserIds.add("user1");
        candidateUserIds.add("user2");
        return CandidateIdUtil.encode(candidateUserIds, PEPCandidateUserExtQueryImpl.USER_CONF_CODE);
    }
}
