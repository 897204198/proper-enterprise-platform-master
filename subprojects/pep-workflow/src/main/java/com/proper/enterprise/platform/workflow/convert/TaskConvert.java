package com.proper.enterprise.platform.workflow.convert;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.workflow.model.PEPTask;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.ArrayList;
import java.util.List;

public class TaskConvert {

    public static List<PEPTask> convert(List<Task> tasks) {
        List<PEPTask> pepTasks = new ArrayList<>();
        if (CollectionUtil.isEmpty(tasks)) {
            return pepTasks;
        }
        for (Task task : tasks) {
            pepTasks.add(new PEPTask(task));
        }
        return pepTasks;
    }


    public static List<PEPTask> convertHisTasks(List<HistoricTaskInstance> tasks) {
        List<PEPTask> pepTaskVOs = new ArrayList<>();
        if (CollectionUtil.isEmpty(tasks)) {
            return pepTaskVOs;
        }
        for (HistoricTaskInstance historicTaskInstance : tasks) {
            pepTaskVOs.add(new PEPTask(historicTaskInstance));
        }
        return pepTaskVOs;
    }
}
