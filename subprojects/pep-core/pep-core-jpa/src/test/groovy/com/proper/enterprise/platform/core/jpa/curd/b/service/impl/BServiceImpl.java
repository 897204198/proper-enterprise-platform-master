package com.proper.enterprise.platform.core.jpa.curd.b.service.impl;

import com.proper.enterprise.platform.core.jpa.curd.b.api.B;
import com.proper.enterprise.platform.core.jpa.curd.b.entity.BEntity;
import com.proper.enterprise.platform.core.jpa.curd.b.repository.BRepository;
import com.proper.enterprise.platform.core.jpa.curd.b.service.BService;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BServiceImpl extends AbstractJpaServiceSupport<B, BRepository, String> implements BService {
    @Autowired
    private BRepository brepository;

    @Override
    public BRepository getRepository() {
        return brepository;
    }

    @Override
    public B saveB(B b) {
        return brepository.save((BEntity)b);
    }



}
