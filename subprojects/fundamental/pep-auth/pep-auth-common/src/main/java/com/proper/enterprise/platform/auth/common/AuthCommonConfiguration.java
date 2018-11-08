package com.proper.enterprise.platform.auth.common;

import com.proper.enterprise.platform.core.factory.ComposeListFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class AuthCommonConfiguration {

    @Bean
    @DependsOn("PEPApplicationContext")
    public ComposeListFactoryBean ignorePatternsList() {
        // ANT path pattern, start with HTTP method, such as: GET:/**/bar, *:/foo/**
        return new ComposeListFactoryBean("ignorePatternsList.+");
    }
}
