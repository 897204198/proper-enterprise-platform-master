package com.proper.enterprise.platform.workflow.plugin.service;

import org.flowable.engine.impl.persistence.entity.ExecutionEntity;

public interface WorkflowMailSender {

    /**
     * 发送邮件
     *
     * @param to        收件人(,隔开)
     * @param code      模板编码
     * @param execution 流程执行实例
     */
    void send(String to,
              String code,
              ExecutionEntity execution);

    /**
     * 发送邮件
     *
     * @param to        收件人(,隔开)
     * @param cc        抄送(,隔开)
     * @param code      模板编码
     * @param execution 流程执行实例
     */
    void send(String to,
              String cc,
              String code,
              ExecutionEntity execution);

    /**
     * 发送邮件
     *
     * @param to        收件人(,隔开)
     * @param cc        抄送(,隔开)
     * @param bcc       密送(,隔开)
     * @param code      模板编码
     * @param execution 流程执行实例
     */
    void send(String to,
              String cc,
              String bcc,
              String code,
              ExecutionEntity execution);

    /**
     * 发送邮件
     *
     * @param to            收件人(,隔开)
     * @param cc            抄送(,隔开)
     * @param bcc           密送(,隔开)
     * @param code          模板编码
     * @param execution     流程执行实例
     * @param attachmentIds 附件Id
     */
    void send(String to,
              String cc,
              String bcc,
              String code,
              ExecutionEntity execution,
              String... attachmentIds);
}
