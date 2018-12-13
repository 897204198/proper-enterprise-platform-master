package com.proper.enterprise.platform.workflow.listener;

import com.proper.enterprise.platform.workflow.util.CandidateIdUtil;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 自定义选人监听
 * 配置在流程的前处理
 * 当流程变量中包含customCandidate时根据规则解析
 * <p>
 * 解析规则
 * key:v assignee经办人  candidateUsers候选人     {Code}候选其它
 */
public class CustomCandidateListener implements TaskListener {

    private static final String CUSTOM_HANDLER_KEY = "customCandidate";

    @Override
    public void notify(DelegateTask delegateTask) {
        Object customHandler = delegateTask.getVariable(CUSTOM_HANDLER_KEY);
        if (null != customHandler) {
            cleanHandler(delegateTask);
            addHandler((Map<String, Object>) customHandler, delegateTask);
            delegateTask.removeVariable(CUSTOM_HANDLER_KEY);
        }
    }

    private void addHandler(Map<String, Object> customCandidateMap, DelegateTask delegateTask) {
        for (Map.Entry<String, Object> entry : customCandidateMap.entrySet()) {
            if ("assignee".equals(entry.getKey()) && null != entry.getValue()) {
                delegateTask.setAssignee((String) entry.getValue());
                continue;
            }
            if ("candidateUsers".equals(entry.getKey()) && null != entry.getValue()) {
                delegateTask.addCandidateUsers((Collection<String>) entry.getValue());
                continue;
            }
            delegateTask.addCandidateGroups(CandidateIdUtil.encode((List<String>) entry.getValue(), entry.getKey()));
        }
    }

    private void cleanHandler(DelegateTask delegateTask) {
        delegateTask.setAssignee(null);
        delegateTask.addCandidateGroups(new ArrayList<>());
        delegateTask.addCandidateUsers(new ArrayList<>());
    }
}
