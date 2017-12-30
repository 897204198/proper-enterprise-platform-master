package com.proper.enterprise.platform.workflow;

import org.activiti.spring.SpringProcessEngineConfiguration;

import java.io.InputStream;

public class PEPSpringProcessEngineConfiguration extends SpringProcessEngineConfiguration {

    protected String mappingFile = "org/activiti/db/mapping/mappings.xml";

    public void setMappingFile(String mappingFile) {
        this.mappingFile = mappingFile;
    }

    @Override
    public InputStream getMyBatisXmlConfigurationStream() {
        return getResourceAsStream(mappingFile);
    }

}
