package com.proper.enterprise.platform.workflow;

import org.flowable.idm.engine.IdmEngineConfiguration;
import java.io.InputStream;

public class PEPIdmEngineConfiguration extends IdmEngineConfiguration {

    protected String mappingFile = "conf/workflow/idm/engine/mapping/mappings.xml";

    public void setMappingFile(String mappingFile) {
        this.mappingFile = mappingFile;
    }

    @Override
    public InputStream getMyBatisXmlConfigurationStream() {
        return getResourceAsStream(mappingFile);
    }

}
