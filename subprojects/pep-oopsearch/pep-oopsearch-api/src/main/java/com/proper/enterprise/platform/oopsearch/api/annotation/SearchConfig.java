package com.proper.enterprise.platform.oopsearch.api.annotation;

import java.lang.annotation.*;

/**
 * oopsearch组件查询配置类注解
 * 每一个使用oopsearch组件的业务模块，必须在其对应的查询配置类上使用该注解
 * */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SearchConfig {
}
