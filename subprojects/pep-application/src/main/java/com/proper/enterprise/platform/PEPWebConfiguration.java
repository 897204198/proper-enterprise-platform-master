package com.proper.enterprise.platform;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class PEPWebConfiguration {

    @Bean
    List<String> ignorePatternsListIndex() {
        return Arrays.asList("GET:/", "GET:/banner");
    }

}
