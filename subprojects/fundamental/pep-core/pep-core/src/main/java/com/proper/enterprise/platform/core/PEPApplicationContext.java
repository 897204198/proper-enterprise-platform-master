package com.proper.enterprise.platform.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class PEPApplicationContext implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static final PEPApplicationContext HOLDER = new PEPApplicationContext();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        HOLDER.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return HOLDER.applicationContext;
    }

    public static Object getBean(String name) throws BeansException {
        return HOLDER.applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return HOLDER.applicationContext.getBean(requiredType);
    }

    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return HOLDER.applicationContext.getBean(name, requiredType);
    }

}
