/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.app.conf;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.activiti.engine.ActivitiException;
import org.hibernate.ejb.HibernatePersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySources({
    @PropertySource(value = "classpath:/META-INF/conf/workflow/activiti-app.properties", ignoreResourceNotFound = true),
    @PropertySource(value = "classpath:conf/workflow/activiti-app.properties"),
    @PropertySource(value = "file:conf/workflow/activiti-app.properties", ignoreResourceNotFound = true),
})
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    protected static final String LIQUIBASE_CHANGELOG_PREFIX = "ACT_DE_";

    @Inject
    private Environment env;

    @Inject
    private DataSource dataSource;

    @Bean(name = "actEntityManagerFactory")
    public EntityManagerFactory entityManagerFactory() {
        log.info("Configuring EntityManager");
        LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();
        lcemfb.setPersistenceProvider(new HibernatePersistence());
        lcemfb.setPersistenceUnitName("persistenceUnit");
        lcemfb.setDataSource(dataSource);
        lcemfb.setJpaDialect(new HibernateJpaDialect());
        lcemfb.setJpaVendorAdapter(jpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.cache.use_second_level_cache", false);
        jpaProperties.put("hibernate.generate_statistics", env.getProperty("hibernate.generate_statistics", Boolean.class, false));
        lcemfb.setJpaProperties(jpaProperties);

        lcemfb.setPackagesToScan("org.activiti.app.domain");
        lcemfb.afterPropertiesSet();
        return lcemfb.getObject();
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(env.getProperty("hibernate.show_sql", Boolean.class, false));
        jpaVendorAdapter.setDatabasePlatform(env.getProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect"));
        return jpaVendorAdapter;
    }

    @Bean(name = "liquibase")
    public Liquibase liquibase() {
        log.info("Configuring Liquibase");

        try {
            DatabaseConnection connection = new JdbcConnection(dataSource.getConnection());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(connection);
            database.setDatabaseChangeLogTableName(LIQUIBASE_CHANGELOG_PREFIX + database.getDatabaseChangeLogTableName());
            database.setDatabaseChangeLogLockTableName(LIQUIBASE_CHANGELOG_PREFIX + database.getDatabaseChangeLogLockTableName());

            Liquibase liquibase = new Liquibase("META-INF/liquibase/activiti-app-db-changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update("activiti");
            return liquibase;

        } catch (Exception e) {
            throw new ActivitiException("Error creating liquibase database");
        }
    }

}
