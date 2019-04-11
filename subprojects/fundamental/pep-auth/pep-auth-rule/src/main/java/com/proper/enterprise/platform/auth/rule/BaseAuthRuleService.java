package com.proper.enterprise.platform.auth.rule;

import java.util.Set;

/**
 * 规则基础接口
 * 实现执行方法 解析参数并返回满足规则的用户Id集合
 *
 * 约定
 * 规则的实现bean名称 应为规则code+AuthRuleImpl 否则规则工厂无法根据code找到对应规则
 */
public interface BaseAuthRuleService {

    /**
     * 执行规则并获得用户id集合
     *
     * @param param 规则参数
     * @return 用户id集合
     */
    Set<String> perform(String param);
}
