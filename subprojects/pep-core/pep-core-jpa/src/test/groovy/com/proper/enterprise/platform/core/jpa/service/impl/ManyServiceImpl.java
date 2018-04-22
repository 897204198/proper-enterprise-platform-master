package com.proper.enterprise.platform.core.jpa.service.impl;

import com.proper.enterprise.platform.core.jpa.entity.ManyEntity;
import com.proper.enterprise.platform.core.jpa.repository.ManyRepository;
import com.proper.enterprise.platform.core.jpa.service.ManyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManyServiceImpl extends JpaServiceSupport<ManyEntity, ManyRepository, String> implements ManyService {

    @Autowired
    private ManyRepository manyRepository;

    @Override
    public ManyRepository getRepository() {
        return manyRepository;
    }
}
