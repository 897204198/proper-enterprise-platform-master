package com.proper.enterprise.platform.core.jpa.curd.b.service.impl;

import com.proper.enterprise.platform.core.jpa.curd.b.api.B;
import com.proper.enterprise.platform.core.jpa.curd.b.repository.BRepository;
import com.proper.enterprise.platform.core.jpa.curd.b.service.BService;
import com.proper.enterprise.platform.core.jpa.service.impl.JpaServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BServiceImpl extends JpaServiceSupport<B, BRepository, String> implements BService {
    @Autowired
    private BRepository brepository;

    @Override
    public BRepository getRepository() {
        return brepository;
    }

}
