package com.proper.enterprise.platform.workflow.convert;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.workflow.vo.PEPExtFormVO;
import com.proper.enterprise.platform.workflow.vo.PEPTaskVO;
import org.flowable.identitylink.api.IdentityLinkInfo;
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
        pepTaskVO.setForm(new PEPExtFormVO(task));
        pepTaskVO.setName(task.getName());
        pepTaskVO.setCreateTime(DateUtil.toString(task.getCreateTime(), DEFAULT_DATETIME_FORMAT));
        pepTaskVO = buildIdentityMsg(pepTaskVO, task.getIdentityLinks());
        return pepTaskVO;
    }

    public static PEPTaskVO convert(HistoricTaskInstance historicTaskInstance) {
        PEPTaskVO pepTaskVO = new PEPTaskVO();
        pepTaskVO.setProcInstId(historicTaskInstance.getProcessInstanceId());
        pepTaskVO.setTaskId(historicTaskInstance.getId());
        pepTaskVO.setForm(new PEPExtFormVO(historicTaskInstance));
        pepTaskVO.setAssignee(historicTaskInstance.getAssignee());
        pepTaskVO.setName(historicTaskInstance.getName());
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

    private static PEPTaskVO buildIdentityMsg(PEPTaskVO pepTaskVO, List<? extends IdentityLinkInfo> identityLinkInfos) {
        if (CollectionUtil.isEmpty(identityLinkInfos)) {
            return pepTaskVO;
        }
        for (IdentityLinkInfo identityLinkInfo : identityLinkInfos) {
            if ("candidate".equals(identityLinkInfo.getType())) {
                pepTaskVO.addCandidateUser(identityLinkInfo.getUserId());
                pepTaskVO.addCandidateGroup(identityLinkInfo.getGroupId());
                pepTaskVO.addCandidateRole(identityLinkInfo.getRoleId());
            }
            if ("assigne".equals(identityLinkInfo.getType())) {
                pepTaskVO.setAssignee(identityLinkInfo.getUserId());
            }
        }
        return pepTaskVO;
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
