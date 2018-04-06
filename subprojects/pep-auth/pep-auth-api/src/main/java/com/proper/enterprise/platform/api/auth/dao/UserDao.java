package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.service.BaseService;

import java.util.Collection;

public interface UserDao extends BaseService<User, String> {

    User save(User user);

    void save(User... users);

    User getNewUser();

    User get(String id);

    User get(String id, EnableEnum enable);

    User getByUsername(String username);

    Collection<? extends User> findAll(Collection<String> idList);

    User findByValidTrueAndId(String id);

    Collection<? extends User> getUsersByCondition(String condition);

    Collection<? extends User> getUsersByCondition(String userName, String name, String email, String phone, EnableEnum enable);

    DataTrunk<? extends User> findUsersPagniation(String userName, String name, String email, String phone, EnableEnum enable);

    void deleteAll();

}
