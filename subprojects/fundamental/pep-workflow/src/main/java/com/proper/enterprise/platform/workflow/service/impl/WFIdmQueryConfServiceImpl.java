package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.workflow.entity.WFIdmQueryConfEntity;
import com.proper.enterprise.platform.workflow.repository.WFIdmQueryConfRepository;
import com.proper.enterprise.platform.workflow.service.WFIdmQueryConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class WFIdmQueryConfServiceImpl implements WFIdmQueryConfService {

    private WFIdmQueryConfRepository wfIdmQueryConfRepository;

    @Autowired
    public WFIdmQueryConfServiceImpl(WFIdmQueryConfRepository wfIdmQueryConfRepository) {
        this.wfIdmQueryConfRepository = wfIdmQueryConfRepository;
    }

    @Override
    @CacheQuery
    public Collection<WFIdmQueryConfEntity> findAll() {
        return wfIdmQueryConfRepository.findAllByOrderBySort();
    }

    @Override
    @CacheQuery
    public WFIdmQueryConfEntity findByType(String type) {
        return wfIdmQueryConfRepository.findByType(type);
    }
}
