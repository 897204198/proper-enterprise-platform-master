package com.proper.enterprise.platform.core.convert.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface POJOConverters {
    POJOConverter[] value();
}
