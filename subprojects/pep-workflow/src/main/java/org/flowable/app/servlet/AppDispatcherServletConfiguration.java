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
package org.flowable.app.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@ComponentScan(value = { "org.flowable.app.conf", "org.flowable.app.rest.editor", "org.flowable.app.rest.idm.remote", "org.flowable.app.rest.exception" })
@EnableAsync
public class AppDispatcherServletConfiguration extends WebMvcConfigurationSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppDispatcherServletConfiguration.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment environment;

    @Bean
    public SessionLocaleResolver localeResolver() {
        return new SessionLocaleResolver();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LOGGER.debug("Configuring localeChangeInterceptor");
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(environment.getProperty("file.upload.max.size", Long.class));
        return multipartResolver;
    }

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        LOGGER.debug("Creating requestMappingHandlerMapping");
        RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
        requestMappingHandlerMapping.setUseSuffixPatternMatch(false);
        requestMappingHandlerMapping.setRemoveSemicolonContent(false);
        Object[] interceptors = { localeChangeInterceptor() };
        requestMappingHandlerMapping.setInterceptors(interceptors);
        return requestMappingHandlerMapping;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        addDefaultHttpMessageConverters(converters);
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) converter;
                jackson2HttpMessageConverter.setObjectMapper(objectMapper);
                break;
            }
        }
    }
}