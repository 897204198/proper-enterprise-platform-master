package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.security.util.SecurityUtil;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.convert.ProcInstConvert;
import com.proper.enterprise.platform.workflow.convert.TaskConvert;
import com.proper.enterprise.platform.workflow.service.PEPTaskService;
import com.proper.enterprise.platform.workflow.vo.PEPFormVO;
import com.proper.enterprise.platform.workflow.vo.PEPTaskVO;
import org.apache.commons.collections.MapUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PEPTaskServiceImpl implements PEPTaskService {

    private static final String START_USER = "startUser";
    private static final String ASSIGNEE = "assignee";


    private TaskService taskService;

    private RuntimeService runtimeService;

    private HistoryService historyService;

    private UserDao userDao;

    @Autowired
    PEPTaskServiceImpl(TaskService taskService,
                       RuntimeService runtimeService,
                       UserDao userDao,
                       HistoryService historyService) {
        this.taskService = taskService;
        this.runtimeService = runtimeService;
        this.userDao = userDao;
        this.historyService = historyService;
    }


    @Override
    public void complete(String taskId) {
        complete(taskId, null);
    }

    @Override
    public void complete(String taskId, Map<String, Object> variables) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (MapUtils.isEmpty(variables)) {
            if (null == task.getAssignee()) {
                taskService.claim(taskId, SecurityUtil.getCurrentUserId());
            }
            taskService.complete(taskId);
            return;
        }
        taskService.setVariablesLocal(taskId, variables);
        String formKey = task.getFormKey();
        Map<String, Object> globalVariables = taskService.getVariables(taskId);
        if (StringUtil.isNotEmpty(formKey)) {
            globalVariables.put(formKey, new PEPFormVO(formKey, variables));
        }
        if (null == task.getAssignee()) {
            taskService.claim(taskId, SecurityUtil.getCurrentUserId());
        }
        taskService.complete(taskId, globalVariables);
    }

    @Override
    public DataTrunk<PEPTaskVO> findPagination(Map<String, Object> searchParam, PageRequest pageRequest) {
        TaskQuery taskQuery = taskService.createTaskQuery()
            .includeProcessVariables()
            .taskCandidateOrAssigned(SecurityUtil.getCurrentUserId())
            .orderByTaskCreateTime()
            .desc();
        List<Task> tasks = taskQuery.list();
        List<PEPTaskVO> taskVOs = TaskConvert.convert(tasks);
        taskVOs = buildTaskProsInstMsg(taskVOs);
        taskVOs = buildTaskUserName(taskVOs, START_USER);
        taskVOs = buildTaskUserName(taskVOs, ASSIGNEE);
        DataTrunk<PEPTaskVO> dataTrunk = new DataTrunk<>();
        dataTrunk.setData(taskVOs);
        dataTrunk.setCount(0);
        return dataTrunk;
    }

    @Override
    public List<PEPTaskVO> findHistoricalProcessTrajectory(String procInstId) {
        List<HistoricTaskInstance> historicTaskInstances = historyService
            .createHistoricTaskInstanceQuery()
            .processInstanceId(procInstId)
            .includeTaskLocalVariables()
            .finished()
            .orderByHistoricTaskInstanceStartTime()
            .asc()
            .list();
        List<PEPTaskVO> hisTasks = TaskConvert.convertHisTasks(historicTaskInstances);
        hisTasks = buildTaskUserName(hisTasks, ASSIGNEE);
        return hisTasks;
    }

    /**
     * 为task封装流程实例信息
     *
     * @param tasks taskVO集合
     * @return 包含流程实例信息的taskVO集合
     */
    private List<PEPTaskVO> buildTaskProsInstMsg(List<PEPTaskVO> tasks) {
        if (CollectionUtil.isEmpty(tasks)) {
            return tasks;
        }
        Set<String> processInstanceIds = new HashSet<>();
        //key:procInstId  value:vos
        Map<String, List<PEPTaskVO>> matchMap = new HashMap<>(16);
        for (PEPTaskVO taskVO : tasks) {
            processInstanceIds.add(taskVO.getProcInstId());
            List<PEPTaskVO> matchTaskVOs = matchMap.get(taskVO.getProcInstId());
            if (null == matchTaskVOs) {
                matchTaskVOs = new ArrayList<>();
            }
            matchTaskVOs.add(taskVO);
            matchMap.put(taskVO.getProcInstId(), matchTaskVOs);
        }
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processInstanceIds(processInstanceIds).list();
        for (ProcessInstance processInstance : processInstances) {
            List<PEPTaskVO> matchTaskVOs = matchMap.get(processInstance.getId());
            if (CollectionUtil.isEmpty(matchTaskVOs)) {
                continue;
            }
            for (PEPTaskVO pepTaskVO : matchTaskVOs) {
                pepTaskVO.setPepProcInstVO(ProcInstConvert.convert(processInstance));
            }
        }
        return tasks;
    }

    /**
     * 为task集合封装人员名称
     *
     * @param tasks 任务集合
     * @param type  流程启动者或者经办人
     * @return taskVO集合
     */
    private List<PEPTaskVO> buildTaskUserName(List<PEPTaskVO> tasks, String type) {
        if (CollectionUtil.isEmpty(tasks)) {
            return tasks;
        }
        Set<String> userIds = new HashSet<>();
        //key:userId  value:vos
        Map<String, List<PEPTaskVO>> matchMap = new HashMap<>(16);
        for (PEPTaskVO taskVO : tasks) {
            String userId = getUserId(taskVO, type);
            if (StringUtil.isEmpty(userId)) {
                continue;
            }
            userIds.add(userId);
            matchMap = buildMatchUserMap(matchMap, userId, taskVO);
        }
        if (CollectionUtil.isEmpty(userIds)) {
            return tasks;
        }
        List<User> users = new ArrayList<>(userDao.findAll(userIds));
        for (User user : users) {
            List<PEPTaskVO> matchTaskVOs = matchMap.get(user.getId());
            if (CollectionUtil.isEmpty(matchTaskVOs)) {
                continue;
            }
            for (PEPTaskVO pepTaskVO : matchTaskVOs) {
                if (ASSIGNEE.equals(type)) {
                    pepTaskVO.setAssigneeName(user.getName());
                    continue;
                }
                pepTaskVO.getPepProcInstVO().setStartUserName(user.getName());
            }
        }
        return tasks;
    }

    private String getUserId(PEPTaskVO taskVO, String type) {
        if (ASSIGNEE.equals(type)) {
            return taskVO.getAssignee();
        }
        if (null == taskVO.getPepProcInstVO()) {
            return null;
        }
        return taskVO.getPepProcInstVO().getStartUserId();
    }

    private Map<String, List<PEPTaskVO>> buildMatchUserMap(Map<String, List<PEPTaskVO>> matchMap,
                                                           String userId, PEPTaskVO pepTaskVO) {
        List<PEPTaskVO> matchTaskVOs = matchMap.get(userId);
        if (null == matchTaskVOs) {
            matchTaskVOs = new ArrayList<>();
        }
        matchTaskVOs.add(pepTaskVO);
        matchMap.put(userId, matchTaskVOs);
        return matchMap;
    }
}
