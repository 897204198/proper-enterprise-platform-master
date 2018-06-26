package com.proper.enterprise.platform.core.jpa.copy.service;

import com.proper.enterprise.platform.core.jpa.copy.entity.CopyAEntity;

public interface CopyAService {

    CopyAEntity save(CopyAEntity a);

    CopyAEntity findOne(String id);
}
