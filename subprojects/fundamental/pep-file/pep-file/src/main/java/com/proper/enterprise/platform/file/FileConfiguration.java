package com.proper.enterprise.platform.file;

import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.dfs.DFSProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
@PropertySource("classpath:/application-file.properties")
public class FileConfiguration {

    private DFSProperties dfsProperties;

    private CoreProperties coreProperties;

    @Autowired
    public FileConfiguration(DFSProperties defProperties, CoreProperties coreProperties) {
        this.dfsProperties = defProperties;
        this.coreProperties = coreProperties;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setMaxUploadSize(dfsProperties.getUploadMaxsize());
        commonsMultipartResolver.setDefaultEncoding(coreProperties.getCharset());
        return commonsMultipartResolver;
    }

}
