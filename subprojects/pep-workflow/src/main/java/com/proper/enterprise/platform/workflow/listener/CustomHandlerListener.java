package com.proper.enterprise.platform.workflow.listener;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.workflow.vo.CustomHandlerVO;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomHandlerListener implements TaskListener {

    private static final String CUSTOM_HANDLER_KEY = "customHandler";

    @Override
    public void notify(DelegateTask delegateTask) {
        Object customHandler = delegateTask.getVariable(CUSTOM_HANDLER_KEY);
        if (null != customHandler) {
            CustomHandlerVO customHandlerVO = convert((Map<String, Object>) customHandler);
            cleanHandler(delegateTask);
            addHandler(customHandlerVO, delegateTask);
            delegateTask.removeVariable(CUSTOM_HANDLER_KEY);
        }
    }

    private void addHandler(CustomHandlerVO customHandlerVO, DelegateTask delegateTask) {
        delegateTask.setAssignee(customHandlerVO.getAssignee());
        delegateTask.addCandidateUsers(CollectionUtil.isEmpty(customHandlerVO.getCandidateUsers())
            ? new ArrayList<>()
            : customHandlerVO.getCandidateUsers());
        delegateTask.addCandidateGroups(CollectionUtil.isEmpty(customHandlerVO.getCandidateGroups())
            ? new ArrayList<>()
            : customHandlerVO.getCandidateGroups());
        delegateTask.addCandidateRoles(CollectionUtil.isEmpty(customHandlerVO.getCandidateRoles())
            ? new ArrayList<>()
            : customHandlerVO.getCandidateRoles());
    }

    private void cleanHandler(DelegateTask delegateTask) {
        delegateTask.setAssignee(null);
        delegateTask.addCandidateGroups(new ArrayList<>());
        delegateTask.addCandidateUsers(new ArrayList<>());
        delegateTask.addCandidateRoles(new ArrayList<>());
    }

    private CustomHandlerVO convert(Map<String, Object> customMap) {
        CustomHandlerVO customHandlerVO = new CustomHandlerVO();
        Object assignee = customMap.get("assignee");
        if (null != assignee) {
            customHandlerVO.setAssignee(assignee.toString());
        }
        Object candidateUsers = customMap.get("candidateUsers");
        if (null != candidateUsers) {
            customHandlerVO.setCandidateUsers((List<String>) candidateUsers);
        }
        Object candidateGroups = customMap.get("candidateGroups");
        if (null != candidateGroups) {
            customHandlerVO.setCandidateGroups((List<String>) candidateGroups);
        }
        Object candidateRoles = customMap.get("candidateRoles");
        if (null != candidateRoles) {
            customHandlerVO.setCandidateRoles((List<String>) candidateRoles);
        }
        return customHandlerVO;
    }
}
