package com.proper.enterprise.platform.core.jpa.service.impl;

import com.proper.enterprise.platform.core.jpa.entity.DateTimeEntity;
import com.proper.enterprise.platform.core.jpa.repository.DateTimeRepository;
import com.proper.enterprise.platform.core.jpa.service.DateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DateTimeServiceImpl extends AbstractJpaServiceSupport<DateTimeEntity, DateTimeRepository, String> implements DateTimeService {

    @Autowired
    private DateTimeRepository dateTimeRepository;

    @Override
    public DateTimeRepository getRepository() {
        return dateTimeRepository;
    }
}
