package com.proper.enterprise.platform.core.neo4j;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.neo4j.conversion.MetaDataDrivenConversionService;
import org.springframework.data.neo4j.repository.config.*;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.proper.**.repository")
@EnableTransactionManagement
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
    public SessionFactory sessionFactory() {
        return new SessionFactory(configuration(), sessionPackage);
    }

    @Bean
    public Neo4jTransactionManager transactionManager() {
        return new Neo4jTransactionManager(sessionFactory());
    }

    @Bean
    public ConversionService springConversionService() {
        return new MetaDataDrivenConversionService(sessionFactory().metaData());
    }

}
