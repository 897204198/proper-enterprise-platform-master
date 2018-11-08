package com.proper.enterprise.platform.workflow.api;

import org.flowable.engine.impl.persistence.entity.ExecutionEntity;

public interface EndNotice {
    /**
     * 流程结束后通知
     *
     * @param execution 当前执行实例
     */
    void notice(ExecutionEntity execution);
}
