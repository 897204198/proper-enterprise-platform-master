package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

public interface UserDao {

    User save(User user);

    void save(User... users);

    Collection<? extends User> save(Collection<? extends User> users);

    User getNewUser();

    User get(String id);

    User getByUsername(String username);

    Collection<? extends User> findAll(Collection<String> idList);

    User findByValidTrueAndId(String id);

    Collection<? extends User> getUsersByCondition(String condition);

    DataTrunk<? extends User> getUsersByCondition(String userName, String name, String email, String phone, String enable,
                                                   Integer pageNo, Integer pageSize);

    boolean hasPermissionOfUser(User user, String reqUrl, RequestMethod requestMethod);

    Collection<? extends Menu> getMenus(User user);

    void deleteAll();

}
