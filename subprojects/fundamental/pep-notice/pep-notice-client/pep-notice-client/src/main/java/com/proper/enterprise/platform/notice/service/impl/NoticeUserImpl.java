package com.proper.enterprise.platform.notice.service.impl;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.notice.service.NoticeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class NoticeUserImpl implements NoticeUser {

    @Autowired
    private UserDao userDao;

    public Collection<? extends User> getUsersByIds(List<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return userDao.findAll(ids);
    }

}
