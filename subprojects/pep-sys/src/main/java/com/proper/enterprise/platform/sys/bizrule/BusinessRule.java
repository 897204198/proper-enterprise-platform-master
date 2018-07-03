package com.proper.enterprise.platform.sys.bizrule;

import com.proper.enterprise.platform.core.utils.SpELParser;
import com.proper.enterprise.platform.sys.bizrule.entity.RuleEntity;
import com.proper.enterprise.platform.sys.bizrule.repository.RuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * 业务规则.
 */
@Component
public class BusinessRule {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessRule.class);

    private SpELParser parser;

    private RuleRepository ruleRepository;

    @Autowired
    public BusinessRule(SpELParser parser, RuleRepository ruleRepository) {
        this.parser = parser;
        this.ruleRepository = ruleRepository;
    }

    public Object expression(String ruleName, Map<String, Object> vars) {
        return expression(ruleName, vars, Object.class, null);
    }

    /**
     * 计算 ruleId 规则中设置的表达式，得到计算结果
     * @param ruleId        规则名称
     * @param vars          计算表达式所需参数
     * @param clz           返回值类型
     * @param defaultVal    默认值，无法获得规则或计算规则异常时返回
     * @param <T>           泛型信息
     * @return 计算结果
     */
    public <T> T expression(String ruleId, Map<String, Object> vars, Class<T> clz, T defaultVal) {
        Optional<RuleEntity> rule = ruleRepository.findById(ruleId);
        if (!rule.isPresent()) {
            LOGGER.debug("Could NOT found rule with name {}!", ruleId);
            return defaultVal;
        }
        RuleEntity ruleEntity = rule.get();
        try {
            LOGGER.info("Parsing {} rule({}) with {}", ruleEntity.getName(), ruleEntity.getRule(), vars);
            return parser.parse(ruleEntity.getRule(), vars, clz);
        } catch (ExpressionException ee) {
            LOGGER.error("Parse {} with {} throw exception!", ruleEntity.getRule(), vars, ee);
        }
        return defaultVal;
    }

}
