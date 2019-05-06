package com.proper.enterprise.platform.workflow.plugin.util;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.workflow.plugin.service.AutoArchiveService;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;

public class ArchiveUtil {

    private ArchiveUtil() {

    }

    /**
     * 归档
     *
     * @param execution 当前执行实例
     * @param forms     归档表单
     */
    public static void archiveToMongo(ExecutionEntity execution, String... forms) {
        ((AutoArchiveService) PEPApplicationContext.getBean("autoArchiveToMongoService"))
            .archive(execution, forms);
    }
}
