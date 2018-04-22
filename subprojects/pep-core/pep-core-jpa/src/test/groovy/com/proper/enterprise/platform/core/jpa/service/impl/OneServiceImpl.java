package com.proper.enterprise.platform.core.jpa.service.impl;

import com.proper.enterprise.platform.core.jpa.entity.OneEntity;
import com.proper.enterprise.platform.core.jpa.repository.OneRepository;
import com.proper.enterprise.platform.core.jpa.service.OneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OneServiceImpl extends JpaServiceSupport<OneEntity, OneRepository, String> implements OneService {

    @Autowired
    private OneRepository oneRepository;

    @Override
    public OneRepository getRepository() {
        return oneRepository;
    }
}
