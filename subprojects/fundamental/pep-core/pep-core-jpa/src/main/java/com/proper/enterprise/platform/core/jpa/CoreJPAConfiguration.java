package com.proper.enterprise.platform.core.jpa;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepositoryBean;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.servlet.Filter;

@Configuration
@EnableJpaRepositories(basePackages = "com.proper.**.repository",
    repositoryFactoryBeanClass = BaseJpaRepositoryBean.class,
    transactionManagerRef = "jpaTransactionManager")
@ImportResource("classpath:/applicationContext-jpa-transaction.xml")
@PropertySource("classpath:/application-core-jpa.properties")
public class CoreJPAConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreJPAConfiguration.class);

    private JpaProperties jpaProperties;

    @Value("${spring.profiles.active}")
    private String profile;

    @Value("${spring.liquibase.change-log}")
    private String changelog;

    @Autowired
    public CoreJPAConfiguration(JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
    }

    @Bean
    public Filter hibernateFilter() {
        return new OpenEntityManagerInViewFilter();
    }

    @Bean
    public DruidDataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public SpringLiquibase liquibase(DruidDataSource dataSource) {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setDataSource(dataSource);
        springLiquibase.setChangeLog(changelog);
        LOGGER.debug("Initialize liquibase with context '{}'", profile);
        springLiquibase.setContexts(profile);
        return springLiquibase;
    }

    @Bean
    @DependsOn("liquibase")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DruidDataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.proper.**.entity");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        LOGGER.debug("Initialize entity manager factory with {}", jpaProperties.getProperties());
        factoryBean.setJpaPropertyMap(jpaProperties.getProperties());
        return factoryBean;
    }

}
