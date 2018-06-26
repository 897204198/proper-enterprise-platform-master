package com.proper.enterprise.platform.core.jpa.copy.service;

import com.proper.enterprise.platform.core.jpa.copy.entity.CopyBEntity;

public interface CopyBService {

    CopyBEntity save(CopyBEntity a);

    CopyBEntity findOne(String id);
}
