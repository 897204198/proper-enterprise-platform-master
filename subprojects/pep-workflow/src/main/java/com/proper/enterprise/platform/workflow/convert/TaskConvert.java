package com.proper.enterprise.platform.workflow.convert;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.workflow.vo.PEPTaskVO;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.ArrayList;
import java.util.List;

import static com.proper.enterprise.platform.core.PEPConstants.DEFAULT_DATETIME_FORMAT;

public class TaskConvert {

    public static PEPTaskVO convert(Task task) {
        PEPTaskVO pepTaskVO = new PEPTaskVO();
        pepTaskVO.setProcInstId(task.getProcessInstanceId());
        pepTaskVO.setTaskId(task.getId());
        pepTaskVO.setAssignee(task.getAssignee());
        pepTaskVO.setVariables(task.getProcessVariables());
        pepTaskVO.setName(task.getName());
        pepTaskVO.setFormKey(task.getFormKey());
        pepTaskVO.setCreateTime(DateUtil.toString(task.getCreateTime(), DEFAULT_DATETIME_FORMAT));
        return pepTaskVO;
    }

    public static PEPTaskVO convert(HistoricTaskInstance historicTaskInstance) {
        PEPTaskVO pepTaskVO = new PEPTaskVO();
        pepTaskVO.setProcInstId(historicTaskInstance.getProcessInstanceId());
        pepTaskVO.setTaskId(historicTaskInstance.getId());
        pepTaskVO.setAssignee(historicTaskInstance.getAssignee());
        pepTaskVO.setVariables(historicTaskInstance.getTaskLocalVariables());
        pepTaskVO.setName(historicTaskInstance.getName());
        pepTaskVO.setFormKey(historicTaskInstance.getFormKey());
        pepTaskVO.setEndTime(null != historicTaskInstance.getEndTime()
            ? DateUtil.toString(historicTaskInstance.getEndTime(), DEFAULT_DATETIME_FORMAT)
            : null);
        pepTaskVO.setCreateTime(DateUtil.toString(historicTaskInstance.getCreateTime(), DEFAULT_DATETIME_FORMAT));
        return pepTaskVO;
    }


    public static List<PEPTaskVO> convert(List<Task> tasks) {
        List<PEPTaskVO> pepTaskVOs = new ArrayList<>();
        if (CollectionUtil.isEmpty(tasks)) {
            return pepTaskVOs;
        }
        for (Task task : tasks) {
            pepTaskVOs.add(convert(task));
        }
        return pepTaskVOs;
    }

    public static List<PEPTaskVO> convertHisTasks(List<HistoricTaskInstance> tasks) {
        List<PEPTaskVO> pepTaskVOs = new ArrayList<>();
        if (CollectionUtil.isEmpty(tasks)) {
            return pepTaskVOs;
        }
        for (HistoricTaskInstance historicTaskInstance : tasks) {
            pepTaskVOs.add(convert(historicTaskInstance));
        }
        return pepTaskVOs;
    }
}
