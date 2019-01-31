package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.notice.service.NoticeSender;
import com.proper.enterprise.platform.template.service.TemplateService;
import com.proper.enterprise.platform.template.vo.TemplateVO;
import com.proper.enterprise.platform.workflow.service.WorkflowMailSender;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("workflowMailSenderImpl")
public class WorkflowMailSenderImpl implements WorkflowMailSender {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private NoticeSender noticeSender;

    @Override
    public void send(String to,
                     String code,
                     ExecutionEntity execution) {
        send(to, null, null, code, execution, (String[]) null);
    }

    @Override
    public void send(String to,
                     String cc,
                     String code,
                     ExecutionEntity execution) {
        send(to, cc, null, code, execution, (String[]) null);
    }

    @Override
    public void send(String to,
                     String cc,
                     String bcc,
                     String code,
                     ExecutionEntity execution) {
        send(to, cc, bcc, code, execution, (String[]) null);
    }

    @Override
    public void send(String to,
                     String cc,
                     String bcc,
                     String code,
                     ExecutionEntity execution,
                     String... attachmentIds) {
        TemplateVO template = templateService.getTemplate(code, execution.getVariables());
        String title = template.getDetails().get(0).getTitle();
        String content = template.getDetails().get(0).getTemplate();
        noticeSender.sendNoticeEmail(to, cc, bcc, title, content, null, attachmentIds);
    }
}
