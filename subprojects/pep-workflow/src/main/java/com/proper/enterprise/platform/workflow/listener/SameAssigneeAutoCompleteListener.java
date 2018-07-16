package com.proper.enterprise.platform.workflow.listener;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.constants.WorkFlowConstants;
import com.proper.enterprise.platform.workflow.service.PEPTaskService;
import com.proper.enterprise.platform.workflow.vo.PEPTaskVO;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

import java.util.List;

public class SameAssigneeAutoCompleteListener implements TaskListener {

    private PEPTaskVO findLastTask(String procInsId) {
        List<PEPTaskVO> hisTasks = PEPApplicationContext.getBean(PEPTaskService.class).findHisTasks(procInsId);
        if (CollectionUtil.isEmpty(hisTasks)) {
            return null;
        }
        return hisTasks.get(hisTasks.size());
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        String assignee = delegateTask.getAssignee();
        String initiator = (String) delegateTask.getVariable(WorkFlowConstants.INITIATOR);
        if (StringUtil.isEmpty(assignee)) {
            return;
        }
        PEPTaskVO lastTask = findLastTask(delegateTask.getProcessInstanceId());
        if (null == lastTask) {
            if (StringUtil.isEmpty(initiator)) {
                return;
            }
            if (initiator.equals(assignee)) {
                autoComplete(delegateTask.getId());
                return;
            }
            return;
        }
        if (StringUtil.isEmpty(lastTask.getAssignee())) {
            return;
        }
        if (!lastTask.getAssignee().equals(delegateTask.getAssignee())) {
            return;
        }
        autoComplete(delegateTask.getId());
    }

    private void autoComplete(String taskId) {
        PEPApplicationContext.getBean(TaskService.class).complete(taskId);
    }
}
