package com.proper.enterprise.platform.core.jpa.copy.service.impl;

import com.proper.enterprise.platform.core.jpa.copy.entity.CopyAEntity;
import com.proper.enterprise.platform.core.jpa.copy.repository.CopyARepository;
import com.proper.enterprise.platform.core.jpa.copy.service.CopyAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CopyAServiceImpl implements CopyAService {

    @Autowired
    CopyARepository copyaRepository;

    @Override
    public CopyAEntity save(CopyAEntity a) {
        return copyaRepository.save(a);
    }

    @Override
    public CopyAEntity findOne(String id) {
        return copyaRepository.findOne(id);
    }
}
