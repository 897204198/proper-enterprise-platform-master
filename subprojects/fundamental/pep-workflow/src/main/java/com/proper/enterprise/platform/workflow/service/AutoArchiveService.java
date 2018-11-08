package com.proper.enterprise.platform.workflow.service;

import org.flowable.engine.impl.persistence.entity.ExecutionEntity;

public interface AutoArchiveService {

    /**
     * 归档
     *
     * @param execution 当前执行实例
     * @param forms     归档表单
     */
    void archive(ExecutionEntity execution, String... forms);

}
