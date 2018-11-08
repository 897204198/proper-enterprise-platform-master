package com.proper.enterprise.platform.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import javax.sql.DataSource;

public class WorkflowDataSourceFactoryBean extends AbstractFactoryBean<DataSource> {

    @Autowired
    private DataSource dataSource;

    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }

    @Override
    protected DataSource createInstance() {
        return dataSource;
    }
}
