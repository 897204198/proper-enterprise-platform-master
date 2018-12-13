package com.proper.enterprise.platform.workflow.repository;

import com.proper.enterprise.platform.workflow.predicate.VariableQueryPredicate;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.engine.ManagementService;
import org.flowable.idm.engine.impl.util.CommandContextUtil;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PEPHistoricVariableInstanceRepository {

    private ManagementService managementService;

    @Autowired
    public PEPHistoricVariableInstanceRepository(ManagementService managementService) {
        this.managementService = managementService;
    }

    /**
     * 通过流程任务Id获取任务变量
     *
     * @param variableQueryPredicate Id集合
     * @return 任务变量集合
     */
    public List<HistoricVariableInstance> findHistoricVariableByQueryPredicate(VariableQueryPredicate variableQueryPredicate) {
        List<HistoricVariableInstance> historicVariableInstances = new ArrayList<>();
        if (variableQueryPredicate == null) {
            return historicVariableInstances;
        }
        historicVariableInstances = managementService.executeCommand(
            (Command<List<HistoricVariableInstance>>) commandContext
                -> CommandContextUtil.getDbSqlSession().selectList("selectHistoricVariableByQueryPredicate", variableQueryPredicate));
        return historicVariableInstances;
    }
}
