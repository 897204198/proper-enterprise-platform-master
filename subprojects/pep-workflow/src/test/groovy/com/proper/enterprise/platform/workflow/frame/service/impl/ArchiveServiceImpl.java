package com.proper.enterprise.platform.workflow.frame.service.impl;

import com.proper.enterprise.platform.workflow.frame.service.ArchiveService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("archiveService")
public class ArchiveServiceImpl implements ArchiveService {

    @Override
    public String archive(Map<String, Object> frameMainFormTestVO) {
        return "success";
    }
}
