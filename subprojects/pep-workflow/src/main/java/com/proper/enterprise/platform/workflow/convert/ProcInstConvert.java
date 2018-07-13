package com.proper.enterprise.platform.workflow.convert;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;
import com.proper.enterprise.platform.workflow.vo.PEPProcInstVO;
import com.proper.enterprise.platform.workflow.vo.enums.PEPProcInstStateEnum;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;

import java.util.ArrayList;
import java.util.List;

import static com.proper.enterprise.platform.core.PEPConstants.DEFAULT_DATETIME_FORMAT;

public class ProcInstConvert {

    public static PEPProcInstVO convert(ProcessInstance processInstance) {
        PEPProcInstVO pepProcInstVO = new PEPProcInstVO();
        pepProcInstVO.setProcInstId(processInstance.getId());
        pepProcInstVO.setCreateTime(DateUtil.toString(processInstance.getStartTime(), DEFAULT_DATETIME_FORMAT));
        pepProcInstVO.setEnded(processInstance.isEnded());
        pepProcInstVO.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        pepProcInstVO.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
        pepProcInstVO.setProcessDefinitionName(processInstance.getProcessDefinitionName());
        pepProcInstVO.setStartUserId(processInstance.getStartUserId());
        pepProcInstVO.setStateCode(pepProcInstVO.getEnded() ? PEPProcInstStateEnum.DONE.name() : PEPProcInstStateEnum.DOING.name());
        pepProcInstVO.setStateValue(pepProcInstVO.getEnded()
            ? DataDicUtil.get(PEPProcInstStateEnum.DONE).getName()
            : DataDicUtil.get(PEPProcInstStateEnum.DOING).getName());
        return pepProcInstVO;
    }

    public static PEPProcInstVO convert(HistoricProcessInstance historicProcessInstance) {
        PEPProcInstVO pepProcInstVO = new PEPProcInstVO();
        pepProcInstVO.setCreateTime(DateUtil.toString(historicProcessInstance.getStartTime(), DEFAULT_DATETIME_FORMAT));
        pepProcInstVO.setEnded(null != historicProcessInstance.getEndTime());
        pepProcInstVO.setProcInstId(historicProcessInstance.getId());
        pepProcInstVO.setProcessDefinitionId(historicProcessInstance.getProcessDefinitionId());
        pepProcInstVO.setProcessDefinitionKey(historicProcessInstance.getProcessDefinitionKey());
        pepProcInstVO.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
        pepProcInstVO.setStartUserId(historicProcessInstance.getStartUserId());
        pepProcInstVO.setEndTime(pepProcInstVO.getEnded()
            ? DateUtil.toString(historicProcessInstance.getEndTime(), DEFAULT_DATETIME_FORMAT)
            : null);
        pepProcInstVO.setStateCode(pepProcInstVO.getEnded() ? PEPProcInstStateEnum.DONE.name() : PEPProcInstStateEnum.DOING.name());
        pepProcInstVO.setStateValue(pepProcInstVO.getEnded()
            ? DataDicUtil.get(PEPProcInstStateEnum.DONE).getName()
            : DataDicUtil.get(PEPProcInstStateEnum.DOING).getName());
        return pepProcInstVO;
    }

    public static List<PEPProcInstVO> convert(List<HistoricProcessInstance> historicProcessInstances) {
        List<PEPProcInstVO> pepProcInstVOs = new ArrayList<>();
        if (CollectionUtil.isEmpty(historicProcessInstances)) {
            return pepProcInstVOs;
        }
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            pepProcInstVOs.add(convert(historicProcessInstance));
        }
        return pepProcInstVOs;
    }
}
