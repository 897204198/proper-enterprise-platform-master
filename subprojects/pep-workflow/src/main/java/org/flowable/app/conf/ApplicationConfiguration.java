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
package org.flowable.app.conf;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = {
    "org.flowable.app.conf",
    "org.flowable.app.repository",
    "org.flowable.app.service",
    "org.flowable.app.filter",
    "org.flowable.app.security",
    "org.flowable.app.model.component" })
public class ApplicationConfiguration {

    /**
     * This is needed to make property resolving work on annotations ... (see http://stackoverflow.com/questions/11925952/custom-spring-property-source-does-not-resolve-placeholders-in-value)
     *
     * @Scheduled(cron="${someProperty}")
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer placeholderConfigurer = new PropertyPlaceholderConfigurer();
        placeholderConfigurer.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        return placeholderConfigurer;
    }

    @Configuration
    @Profile("production")
    @PropertySource(value = "classpath:conf/workflow/workflow-production.properties")
    static class Production { }

    @Configuration
    @Profile("dev")
    @PropertySource(value = "classpath:conf/workflow/workflow-dev.properties")
    static class Dev { }

    @Configuration
    @Profile("test")
    @PropertySource(value = "classpath:conf/workflow/workflow-test.properties")
    static class Test { }

}
