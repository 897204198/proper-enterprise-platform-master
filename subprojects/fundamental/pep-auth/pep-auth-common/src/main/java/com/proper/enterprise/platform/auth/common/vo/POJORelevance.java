package com.proper.enterprise.platform.auth.common.vo;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
/**
 * 避免权限过分改动 暂时将注解移动至auth vo包内 仅vo包内可以引用
 */
@interface POJORelevance {

    Class relevanceDO() default void.class;

    String relevanceDOClassName() default "";
}
