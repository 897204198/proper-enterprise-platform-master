package com.proper.enterprise.platform.core.factory;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来将多个 list bean 合并成为一个 bean 的工厂 bean
 * 支持按 bean name 的样式进行合并
 */
public class ComposeListFactoryBean extends AbstractFactoryBean<List> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComposeListFactoryBean.class);

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
        if (StringUtil.isBlank(listBeanNamePattern)) {
            LOGGER.debug("Not define list bean name pattern, return empty list!");
            return instance;
        }

        try {
            String[] beanNames = PEPApplicationContext.getApplicationContext().getBeanNamesForType(List.class);
            for (String beanName : beanNames) {
                if (beanName.matches(listBeanNamePattern)) {
                    instance.addAll(PEPApplicationContext.getBean(beanName, List.class));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Find beans with '" + listBeanNamePattern + "' pattern error!", e);
        }
        return instance;
    }

}
