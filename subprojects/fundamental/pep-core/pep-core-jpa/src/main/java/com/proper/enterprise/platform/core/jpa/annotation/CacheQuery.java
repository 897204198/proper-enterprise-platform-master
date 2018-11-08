package com.proper.enterprise.platform.core.jpa.annotation;

import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
public @interface CacheQuery {

}
