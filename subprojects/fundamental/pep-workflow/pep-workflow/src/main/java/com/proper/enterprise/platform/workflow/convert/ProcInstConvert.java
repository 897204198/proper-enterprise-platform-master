package com.proper.enterprise.platform.workflow.convert;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.workflow.model.PEPProcInst;
import org.flowable.engine.history.HistoricProcessInstance;

import java.util.ArrayList;
import java.util.List;

public class ProcInstConvert {

    public static List<PEPProcInst> convert(List<HistoricProcessInstance> historicProcessInstances) {
        List<PEPProcInst> pepProcInsts = new ArrayList<>();
        if (CollectionUtil.isEmpty(historicProcessInstances)) {
            return pepProcInsts;
        }
        for (HistoricProcessInstance historicProcessInstance : historicProcessInstances) {
            pepProcInsts.add(new PEPProcInst(historicProcessInstance));
        }
        return pepProcInsts;
    }
}
