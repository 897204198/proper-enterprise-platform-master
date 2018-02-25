package com.proper.enterprise.platform.workflow.convert;

import com.proper.enterprise.platform.api.auth.model.User;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserConvert {
    private UserConvert() {

    }

    public static org.flowable.idm.api.User convert(User pepUser) {
        if (null == pepUser) {
            return null;
        }
        org.flowable.idm.api.User user = new UserEntityImpl();
        user.setFirstName(pepUser.getName());
        user.setLastName(pepUser.getUsername());
        user.setId(pepUser.getId());
        user.setEmail(user.getEmail());
        user.setPassword(pepUser.getPassword());
        return user;
    }


    public static List<org.flowable.idm.api.User> convertCollection(Collection<? extends User> pepUsers) {
        List<org.flowable.idm.api.User> users = new ArrayList<>();
        for (com.proper.enterprise.platform.api.auth.model.User pepUser : pepUsers) {
            org.flowable.idm.api.User user = convert(pepUser);
            if (null == user) {
                continue;
            }
            users.add(user);
        }
        return users;
    }
}
