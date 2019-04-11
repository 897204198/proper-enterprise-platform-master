package com.proper.enterprise.platform.auth.common.rule;

import com.proper.enterprise.platform.api.auth.dao.UserGroupDao;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.auth.rule.BaseAuthRuleService;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户组角色规则
 * 指定用户组
 */
@Service
public class GroupAuthRuleImpl implements BaseAuthRuleService {

    @Autowired
    private UserGroupDao userGroupDao;

    @Override
    public Set<String> perform(String param) {
        if (StringUtil.isEmpty(param)) {
            return new HashSet<>();
        }
        String[] groupIds = param.split("\\,");
        Collection<? extends UserGroup> userGroups = userGroupDao.findAll(Arrays.asList(groupIds));
        if (CollectionUtil.isEmpty(userGroups)) {
            return new HashSet<>();
        }
        Set<String> userIds = new HashSet<>();
        for (UserGroup userGroup : userGroups) {
            if (CollectionUtil.isNotEmpty(userGroup.getUsers())) {
                for (User user : userGroup.getUsers()) {
                    userIds.add(user.getId());
                }
            }
        }
        return userIds;
    }
}
