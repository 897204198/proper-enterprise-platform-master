package com.proper.enterprise.platform.auth.common.mock;

import com.proper.enterprise.platform.api.auth.dao.UserDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MockUserDAO implements UserDao {
    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public void save(User... users) {

    }

    @Override
    public <S extends User> Collection<S> save(Iterable<S> var1) {
        return null;
    }

    @Override
    public User findById(String var1) {
        return null;
    }

    @Override
    public boolean existsById(String var1) {
        return false;
    }

    @Override
    public User addUserRole(String userId, String roleId) {
        return null;
    }

    @Override
    public User deleteUserRole(String userId, String roleId) {
        return null;
    }

    @Override
    public boolean deleteByIds(Collection<String> ids) {
        return false;
    }

    @Override
    public User updateForSelective(User user) {
        return null;
    }

    @Override
    public User getNewUser() {
        return null;
    }

    @Override
    public User getByUsername(String username, EnableEnum enableEnum) {
        return null;
    }

    @Override
    public User getCurrentUserByUserId(String userId) {
        return null;
    }

    @Override
    public Collection<? extends User> findAll(Collection<String> idList) {
        return null;
    }

    @Override
    public Collection<User> findAll() {
        return null;
    }

    @Override
    public Collection<User> findAll(Sort var1) {
        return null;
    }

    @Override
    public Page<User> findAll(Pageable var1) {
        return null;
    }

    @Override
    public Collection<User> findAllById(Iterable<String> var1) {
        return null;
    }

    @Override
    public Collection<? extends User> getUsersByOrCondition(String condition, EnableEnum enable) {
        return null;
    }

    @Override
    public Collection<? extends User> getUsersByAndCondition(String userName, String name, String email, String phone, EnableEnum enable) {
        return null;
    }

    @Override
    public DataTrunk<? extends User> findUsersPagination(String userName, String name, String email, String phone, EnableEnum enable) {
        return null;
    }

    @Override
    public User changePassword(String userId, String oldPassword, String password) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(String var1) {

    }

    @Override
    public void delete(User var1) {

    }

    @Override
    public void delete(Iterable<? extends User> var1) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public DataTrunk<User> findPage() {
        return null;
    }

    @Override
    public DataTrunk<User> findPage(Sort sort) {
        return null;
    }

    @Override
    public DataTrunk<User> findPage(Pageable pageable) {
        return null;
    }
}
