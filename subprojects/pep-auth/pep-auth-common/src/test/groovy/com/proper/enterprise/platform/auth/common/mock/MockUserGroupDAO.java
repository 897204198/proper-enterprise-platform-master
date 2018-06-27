package com.proper.enterprise.platform.auth.common.mock;

import com.proper.enterprise.platform.api.auth.dao.UserGroupDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.UserGroup;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MockUserGroupDAO implements UserGroupDao {
    @Override
    public UserGroup getNewUserGroup() {
        return null;
    }

    @Override
    public UserGroup save(UserGroup group) {
        return null;
    }

    @Override
    public <S extends UserGroup> Collection<S> save(Iterable<S> var1) {
        return null;
    }

    @Override
    public UserGroup updateForSelective(UserGroup group) {
        return null;
    }

    @Override
    public Collection<? extends UserGroup> findAll(Collection<String> idList) {
        return null;
    }

    @Override
    public Collection<UserGroup> findAll() {
        return null;
    }

    @Override
    public Collection<UserGroup> findAll(Iterable<String> var1) {
        return null;
    }

    @Override
    public Collection<UserGroup> findAll(Sort var1) {
        return null;
    }

    @Override
    public Page<UserGroup> findAll(Pageable var1) {
        return null;
    }

    @Override
    public UserGroup findByName(String name, EnableEnum enable) {
        return null;
    }

    @Override
    public Collection<? extends UserGroup> getGroups(String name, String description, EnableEnum enable) {
        return null;
    }

    @Override
    public DataTrunk<? extends UserGroup> getGroupsPagination(String name, String description, EnableEnum enable) {
        return null;
    }

    @Override
    public UserGroup findOne(String var1) {
        return null;
    }

    @Override
    public boolean exists(String var1) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(String var1) {

    }

    @Override
    public void delete(UserGroup var1) {

    }

    @Override
    public void delete(Iterable<? extends UserGroup> var1) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public DataTrunk<UserGroup> findPage() {
        return null;
    }

    @Override
    public DataTrunk<UserGroup> findPage(Sort sort) {
        return null;
    }

    @Override
    public DataTrunk<UserGroup> findPage(Pageable pageable) {
        return null;
    }
}
