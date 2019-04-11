package com.proper.enterprise.platform.auth.common.rule;

import com.proper.enterprise.platform.auth.rule.BaseAuthRuleService;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户角色规则
 * 指定用户
 */
@Service
public class UserAuthRuleImpl implements BaseAuthRuleService {

    @Override
    public Set<String> perform(String param) {
        if (StringUtil.isEmpty(param)) {
            return new HashSet<>();
        }
        String[] userIds = param.split("\\,");
        return new HashSet<>(Arrays.asList(userIds));
    }
}
