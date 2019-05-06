package com.proper.enterprise.platform.workflow.api.notice;

import com.proper.enterprise.platform.core.security.Authentication;
import com.proper.enterprise.platform.workflow.plugin.service.EndNotice;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("endNoticeTest")
@Primary
public class EndNoticeImpl implements EndNotice {
    @Override
    public void notice(ExecutionEntity execution) {
        Authentication.setCurrentUserId(execution.getProcessDefinitionKey());
    }
}
