package com.proper.enterprise.platform.auth.common.rule;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.auth.rule.BaseAuthRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户角色规则
 * 所有用户
 */
@Service
public class UserAllAuthRuleImpl implements BaseAuthRuleService {

    @Autowired
    private UserDao userDao;

    @Override
    public Set<String> perform(String param) {
        Set<String> userIds = new HashSet<>();
        for (User user : userDao.findAll()) {
            userIds.add(user.getId());
        }
        return userIds;
    }
}
