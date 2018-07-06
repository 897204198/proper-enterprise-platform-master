package com.proper.enterprise.platform.workflow.frame.service;

import java.util.Map;

public interface ArchiveService {

    /**
     * 归档接口
     *
     * @param frameMainFormTestVO 主表mainForm表单
     * @return 归档成功
     */
    String archive(Map<String, Object> frameMainFormTestVO);
}
