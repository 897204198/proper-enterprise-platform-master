package com.proper.enterprise.platform.core.convert.annotation;

import com.proper.enterprise.platform.core.convert.handler.FromHandler;
import com.proper.enterprise.platform.core.convert.handler.TargetHandler;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(POJOConverters.class)
@Inherited
public @interface POJOConverter {

    /**
     * from或target的字段名称
     */
    String fieldName();

    /**
     * from的类的类型  若sourceClassName有值取sourceClassName
     */
    Class fromBy() default void.class;

    /**
     * from的类的ClassName  用于多态场景  当有时知晓A类型但是类型以父类引用的形态存在无法直接编译时使用
     */
    String fromClassName() default "";

    /**
     * from转换器
     * 用于java bean转换的时候复杂的转换
     * 写一个类实现FromHandler 类的参数为fromClass
     *
     * @return 转换器类型
     */
    Class<? extends FromHandler> fromHandleBy() default FromHandler.class;

    /**
     * target的类的类型 若sourceClassName有值取targetClassName
     */
    Class targetBy() default void.class;

    /**
     * target的类的ClassName  用于多态场景  当有时知晓A类型但是类型以父类引用的形态存在无法直接编译时使用
     */
    String targetClassName() default "";


    /**
     * target转换器
     * 用于java bean转换的时候复杂的转换
     * 写一个类实现TargetHandler 类的参数为targetClass
     *
     * @return 转换器类型
     */
    Class<? extends TargetHandler> targetHandleBy() default TargetHandler.class;
}
