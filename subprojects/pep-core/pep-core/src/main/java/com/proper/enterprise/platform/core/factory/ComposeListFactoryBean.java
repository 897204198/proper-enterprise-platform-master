package com.proper.enterprise.platform.core.factory;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来将多个 list bean 合并成为一个 bean 的工厂 bean
 * 支持按 bean name 的样式进行合并
 */
public class ComposeListFactoryBean extends AbstractFactoryBean<List> {

    /**
     * 要合并的 list 类型 bean 的名称样式
     */
    private String listBeanNamePattern;

    public ComposeListFactoryBean(String listBeanNamePattern) {
        this.listBeanNamePattern = listBeanNamePattern;
    }

    @Override
    public Class<List> getObjectType() {
        return List.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List createInstance() {
        List instance = new ArrayList();
        if (StringUtil.isNull(listBeanNamePattern)) {
            return instance;
        }

        String[] beanNames = PEPApplicationContext.getApplicationContext().getBeanNamesForType(List.class);
        for (String beanName : beanNames) {
            if (beanName.matches(listBeanNamePattern)) {
                instance.addAll(PEPApplicationContext.getBean(beanName, List.class));
            }
        }
        return instance;
    }

}
