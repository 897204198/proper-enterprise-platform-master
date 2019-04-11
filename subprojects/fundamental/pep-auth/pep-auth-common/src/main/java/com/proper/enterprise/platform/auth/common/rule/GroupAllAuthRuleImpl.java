package com.proper.enterprise.platform.auth.common.rule;

import com.proper.enterprise.platform.api.auth.dao.UserGroupDao;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.auth.rule.BaseAuthRuleService;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户组角色规则
 * 所有用户组
 */
@Service
public class GroupAllAuthRuleImpl implements BaseAuthRuleService {

    @Autowired
    private UserGroupDao userGroupDao;

    @Override
    public Set<String> perform(String param) {
        Set<String> userIds = new HashSet<>();
        for (UserGroup userGroup : userGroupDao.findAll()) {
            if (CollectionUtil.isNotEmpty(userGroup.getUsers())) {
                for (User user : userGroup.getUsers()) {
                    userIds.add(user.getId());
                }
            }
        }
        return userIds;
    }
}
