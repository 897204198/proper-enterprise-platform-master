package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.http.HttpClient;
import com.proper.enterprise.platform.notice.server.sdk.enums.NoticeType;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeRequest;
import com.proper.enterprise.platform.notice.server.sdk.request.NoticeTarget;
import com.proper.enterprise.platform.notice.service.impl.NoticeSenderImpl;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.util.DataDicUtil;
import com.proper.enterprise.platform.template.service.TemplateService;
import com.proper.enterprise.platform.template.vo.TemplateVO;
import com.proper.enterprise.platform.workflow.service.WorkflowMailSender;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("workflowMailSenderImpl")
public class WorkflowMailSenderImpl implements WorkflowMailSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeSenderImpl.class);

    @Autowired
    private TemplateService templateService;

    @Override
    public void send(String to,
                     String code,
                     ExecutionEntity execution) {
        send(to, null, null, code, execution, null);
    }

    @Override
    public void send(String to,
                     String cc,
                     String code,
                     ExecutionEntity execution) {
        send(to, cc, null, code, execution, null);
    }

    @Override
    public void send(String to,
                     String cc,
                     String bcc,
                     String code,
                     ExecutionEntity execution) {
        send(to, cc, bcc, code, execution, null);
    }

    @Override
    public void send(String to,
                     String cc,
                     String bcc,
                     String code,
                     ExecutionEntity execution,
                     String... attachmentIds) {
        NoticeRequest noticeRequest = new NoticeRequest();
        noticeRequest.setBatchId(UUID.randomUUID().toString());
        NoticeTarget target = new NoticeTarget();
        target.setTo(to);
        target.setTargetExtMsg("cc", cc);
        target.setTargetExtMsg("bcc", bcc);
        target.setTargetExtMsg("attachmentIds", attachmentIds);
        noticeRequest.addTarget(target);
        TemplateVO template = templateService.getTemplate(code, execution.getVariables());
        noticeRequest.setTitle(template.getDetails().get(0).getTitle());
        noticeRequest.setContent(template.getDetails().get(0).getTemplate());
        noticeRequest.setNoticeType(NoticeType.EMAIL);
        this.accessNoticeServer(noticeRequest);
    }

    private void accessNoticeServer(NoticeRequest noticeModel) {
        String noticeServerUrl = null;
        DataDic dataDic = DataDicUtil.get("NOTICE_SERVER", "URL");
        if (dataDic != null) {
            noticeServerUrl = dataDic.getName();
        }
        String noticeServerToken = null;
        dataDic = DataDicUtil.get("NOTICE_SERVER", "TOKEN");
        if (dataDic != null) {
            noticeServerToken = dataDic.getName();
        }
        try {
            String data = JSONUtil.toJSON(noticeModel);
            LOGGER.debug("MAIL SENDER SEND:" + data);
            ResponseEntity<byte[]> response = HttpClient.post(noticeServerUrl
                + "/notice/server/send?access_token="
                + noticeServerToken, MediaType.APPLICATION_JSON, data);
            if (HttpStatus.CREATED != response.getStatusCode()) {
                String str = StringUtil.toEncodedString(response.getBody());
                LOGGER.debug("MAIL SENDER SEND {} : {}", noticeModel.getBatchId(), str);
            }
        } catch (Exception e) {
            LOGGER.error("WorkflowMailSenderImpl.accessNoticeServer[Exception]:", e);
        }
    }
}
