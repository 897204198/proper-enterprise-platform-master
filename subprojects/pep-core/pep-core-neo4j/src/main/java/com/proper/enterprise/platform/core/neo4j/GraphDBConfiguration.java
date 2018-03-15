package com.proper.enterprise.platform.core.neo4j;

import org.neo4j.ogm.MetaData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GraphDBConfiguration {

    @Value("${neo4j.driver}")
    private String driver;

    @Value("${neo4j.host}")
    private String host;

    @Value("${neo4j.http.port}")
    private String port;

    @Value("${neo4j.username}")
    private String username;

    @Value("${neo4j.password}")
    private String password;

    @Value("${neo4j.package}")
    private String sessionPackage;

    @Bean
    public org.neo4j.ogm.config.Configuration configuration() {
        org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
        config.driverConfiguration().setDriverClassName(driver)
            .setURI("http://".concat(username).concat(":").concat(password).concat("@").concat(host).concat(":").concat(port));
        return config;
    }

    @Bean
    public MetaData neo4jMetaData() {
        return new MetaData(sessionPackage);
    }
}
