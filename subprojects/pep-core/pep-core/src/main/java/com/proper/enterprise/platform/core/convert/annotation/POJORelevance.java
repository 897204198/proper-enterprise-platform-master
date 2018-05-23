package com.proper.enterprise.platform.core.convert.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface POJORelevance {

    Class relevanceDO() default void.class;

    String relevanceDOClassName() default "";
}
