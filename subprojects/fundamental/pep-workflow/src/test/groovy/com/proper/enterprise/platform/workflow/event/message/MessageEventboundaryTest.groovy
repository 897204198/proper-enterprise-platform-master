package com.proper.enterprise.platform.workflow.event.message

import com.proper.enterprise.platform.test.AbstractJPATest
import org.flowable.engine.HistoryService
import org.flowable.engine.RuntimeService
import org.flowable.engine.TaskService
import org.flowable.engine.runtime.Execution
import org.flowable.engine.runtime.ProcessInstance
import org.flowable.task.api.Task
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class MessageEventboundaryTest extends AbstractJPATest {

    public static final String MESSAGE_PROCESS_KEY = "messageEventboundary"

    @Autowired
    private RuntimeService runtimeService

    @Autowired
    private TaskService taskService

    @Autowired
    private HistoryService historyService

    @Test
    public void test() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(MESSAGE_PROCESS_KEY)
        //启动流程 到达任务1
        List<Task> tasks = taskService.createTaskQuery().list()
        assert tasks.size() == 1
        assert "任务1".equals(tasks.get(0).getName())

        //触发边界事件
        Execution execution = runtimeService.createExecutionQuery().messageEventSubscriptionName("边界测试").singleResult()
        runtimeService.messageEventReceived("边界测试", execution.getId())

        //到达边界事件
        List<Task> eventTasks = taskService.createTaskQuery().list()
        assert eventTasks.size() == 1
        assert "边界事件".equals(eventTasks.get(0).getName())

        //完成边界事件
        taskService.complete(eventTasks.get(0).getId())

        //流程结束
        assert null != historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult().getEndTime()
        historyService.createHistoricTaskInstanceQuery().list()
    }
}
