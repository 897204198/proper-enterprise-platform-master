package com.proper.enterprise.platform.core.convert.annotation;

import java.lang.annotation.*;

/**
 * 声明类型注解
 * 当返回值是接口或者是抽象类型的时候 如果需要通过BeanUtil.covert做自动转换
 * 那么需要在target的set方法上使用该注解声明具体类型(source的get方法如果有需要也可以做声明)
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DeclareType {
    Class classType();
}
