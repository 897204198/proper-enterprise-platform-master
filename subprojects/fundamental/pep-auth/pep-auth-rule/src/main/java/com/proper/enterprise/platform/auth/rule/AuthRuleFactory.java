package com.proper.enterprise.platform.auth.rule;

import com.proper.enterprise.platform.core.PEPApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 规则工厂
 */
public class AuthRuleFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRuleFactory.class);


    private AuthRuleFactory() {

    }

    private static final String SUFFIX = "AuthRuleImpl";

    /**
     * 根据规则编码获取规则实现
     * 约定
     * 实现的bean名称为core+AuthRule
     *
     * @param code 规则编码
     * @return 规则实现
     */
    public static BaseAuthRuleService produce(String code) {
        try {
            return (BaseAuthRuleService) PEPApplicationContext.getBean(code + SUFFIX);
        } catch (Exception e) {
            LOGGER.error("find AuthRuleImpl error", e);
            return null;
        }
    }
}
