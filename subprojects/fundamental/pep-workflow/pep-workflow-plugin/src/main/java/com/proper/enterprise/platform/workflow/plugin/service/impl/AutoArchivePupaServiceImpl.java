package com.proper.enterprise.platform.workflow.plugin.service.impl;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.websocket.util.StompMessageSendUtil;
import com.proper.enterprise.platform.workflow.plugin.service.AutoArchiveService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service(value = "autoArchivePupa")
public class AutoArchivePupaServiceImpl implements AutoArchiveService {

    private static final String PROCINSTID = "procInstId";

    private static final String PROCESS_DEFINITION_ID = "processDefinitionId";

    private static final String PROCESS_DEFINITION_KEY = "processDefinitionKey ";

    @Override
    public void archive(ExecutionEntity execution, String... forms) {
        Map<String, String> archiveData = new HashMap<>(16);
        archiveData.put(PROCINSTID, execution.getProcessInstanceId());
        archiveData.put(PROCESS_DEFINITION_ID, execution.getProcessDefinitionId());
        archiveData.put(PROCESS_DEFINITION_KEY, execution.getProcessDefinitionKey());
        StompMessageSendUtil.send("/topic/autoArchive", JSONUtil.toJSONIgnoreException(archiveData));
    }
}
