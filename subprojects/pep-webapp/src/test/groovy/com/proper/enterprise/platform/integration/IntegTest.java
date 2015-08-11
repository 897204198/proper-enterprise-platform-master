package com.proper.enterprise.platform.integration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@WebAppConfiguration
@ContextConfiguration({"/spring/applicationContext.xml", 
                       "/spring/applicationContext-profile.xml", 
                       "/spring/dal/applicationContext-datasource.xml", 
                       "/spring/dal/applicationContext-jpa.xml"})
@ActiveProfiles("test")
public @interface IntegTest {

}
