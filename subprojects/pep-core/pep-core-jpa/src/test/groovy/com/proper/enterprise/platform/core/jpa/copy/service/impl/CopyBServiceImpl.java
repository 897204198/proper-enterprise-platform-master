package com.proper.enterprise.platform.core.jpa.copy.service.impl;

import com.proper.enterprise.platform.core.jpa.copy.entity.CopyBEntity;
import com.proper.enterprise.platform.core.jpa.copy.repository.CopyBRepository;
import com.proper.enterprise.platform.core.jpa.copy.service.CopyBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CopyBServiceImpl implements CopyBService {

    @Autowired
    CopyBRepository copyBRepository;

    @Override
    public CopyBEntity save(CopyBEntity a) {
        return copyBRepository.save(a);
    }

    @Override
    public CopyBEntity findOne(String id) {
        return copyBRepository.findOne(id);
    }
}
