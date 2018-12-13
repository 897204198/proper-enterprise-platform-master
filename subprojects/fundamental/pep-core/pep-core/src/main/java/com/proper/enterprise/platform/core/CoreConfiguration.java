package com.proper.enterprise.platform.core;

import com.proper.enterprise.platform.core.i18n.AntPatternReloadableResourceBundleMessageSource;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.core.utils.gzip.filter.GZipFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.StringUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.util.Properties;

@Configuration
@PropertySource("classpath:/application-core.properties")
public class CoreConfiguration {

    private CoreProperties coreProperties;

    @Autowired
    public CoreConfiguration(CoreProperties coreProperties) {
        this.coreProperties = coreProperties;
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("com.proper.enterprise");
        return marshaller;
    }

    @Bean
    public Jaxb2Marshaller unmarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("com.proper.enterprise");
        return marshaller;
    }

    @Bean
    public FilterRegistrationBean<GZipFilter> gzipFilter() {
        FilterRegistrationBean<GZipFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new GZipFilter());
        registration.addUrlPatterns("/*");
        if (StringUtil.isNotEmpty(coreProperties.getGzipExcludePathPatterns())) {
            registration.addInitParameter("excludePathPatterns", coreProperties.getGzipExcludePathPatterns());
        }
        registration.setName("gzipFilter");
        return registration;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.messages")
    public MessageSourceProperties messageSourceProperties() {
        return new MessageSourceProperties();
    }

    @Bean
    public AntPatternReloadableResourceBundleMessageSource messageSource(MessageSourceProperties properties, CoreProperties coreProperties) {
        AntPatternReloadableResourceBundleMessageSource messageSource = new AntPatternReloadableResourceBundleMessageSource();
        if (StringUtils.hasText(properties.getBasename())) {
            messageSource.setBasenames(StringUtils.commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(properties.getBasename())));
        }
        messageSource.setUseCodeAsDefaultMessage(properties.isUseCodeAsDefaultMessage());
        messageSource.setDefaultEncoding(coreProperties.getCharset());
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean validator(AntPatternReloadableResourceBundleMessageSource messageSource) {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setProviderClass(org.hibernate.validator.HibernateValidator.class);
        validator.setValidationMessageSource(messageSource);
        Properties properties = new Properties();
        properties.setProperty("hibernate.validator.fail_fast", "true");
        validator.setValidationProperties(properties);
        return validator;
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(LocalValidatorFactoryBean validator) {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
        methodValidationPostProcessor.setValidator(validator);
        return methodValidationPostProcessor;
    }

}
