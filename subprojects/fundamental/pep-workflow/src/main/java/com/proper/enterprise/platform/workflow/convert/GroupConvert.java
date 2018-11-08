package com.proper.enterprise.platform.workflow.convert;

import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import org.flowable.idm.api.Group;
import org.flowable.idm.engine.impl.persistence.entity.GroupEntityImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GroupConvert {

    private GroupConvert() {

    }

    public static Group convert(UserGroup userGroup) {
        if (null == userGroup) {
            return null;
        }
        Group group = new GroupEntityImpl();
        group.setId(userGroup.getId());
        group.setName(userGroup.getName());
        return group;
    }


    public static List<Group> convertCollection(Collection<? extends UserGroup> userGroups) {
        if (CollectionUtil.isEmpty(userGroups)) {
            return new ArrayList<>();
        }
        List<Group> groups = new ArrayList<>();
        for (UserGroup userGroup : userGroups) {
            Group group = convert(userGroup);
            if (null == group) {
                continue;
            }
            groups.add(group);
        }
        return groups;
    }
}
