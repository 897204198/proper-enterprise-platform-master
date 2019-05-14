package com.proper.enterprise.platform.core.jpa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstraintViolationMessage {

    /**
     * 索引名称
     *
     * @return 索引名称
     */
    String name();

    /**
     * 当违反约束后提示的信息
     * <p>
     * 若配置i18n则匹配i18n 否则匹配原文
     *
     * @return 当违反约束后提示的信息
     */
    String message();
}
