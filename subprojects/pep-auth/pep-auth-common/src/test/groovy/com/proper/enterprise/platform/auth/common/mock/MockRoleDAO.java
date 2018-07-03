package com.proper.enterprise.platform.auth.common.mock;

import com.proper.enterprise.platform.api.auth.dao.RoleDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Role;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class MockRoleDAO implements RoleDao {
    @Override
    public Role getNewRole() {
        return null;
    }

    @Override
    public Role save(Role role) {
        return null;
    }

    @Override
    public <S extends Role> Collection<S> save(Iterable<S> var1) {
        return null;
    }

    @Override
    public Role findById(String var1) {
        return null;
    }

    @Override
    public boolean existsById(String var1) {
        return false;
    }

    @Override
    public Role updateForSelective(Role role) {
        return null;
    }

    @Override
    public Collection<? extends Role> findRoles(Collection<String> idList) {
        return null;
    }

    @Override
    public Collection<? extends Role> findRoles(EnableEnum enable) {
        return null;
    }

    @Override
    public Collection<? extends Role> findRoles(String name, EnableEnum enable) {
        return null;
    }

    @Override
    public Collection<? extends Role> findRolesByParentId(List<String> parentIds) {
        return null;
    }

    @Override
    public Collection<? extends Role> findRolesLike(String name, EnableEnum enable) {
        return null;
    }

    @Override
    public Collection<? extends Role> findRolesLike(String name, String description, String parentId, EnableEnum enable) {
        return null;
    }

    @Override
    public DataTrunk<? extends Role> findRolesPagination(String name, String description, String parentId, EnableEnum enable) {
        return null;
    }

    @Override
    public Collection<? extends Role> findParentRoles(String currentRoleId) {
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
    public void delete(Role var1) {

    }

    @Override
    public void delete(Iterable<? extends Role> var1) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Collection<Role> findAll() {
        return null;
    }

    @Override
    public Collection<Role> findAll(Sort var1) {
        return null;
    }

    @Override
    public Page<Role> findAll(Pageable var1) {
        return null;
    }

    @Override
    public Collection<Role> findAllById(Iterable<String> var1) {
        return null;
    }

    @Override
    public DataTrunk<Role> findPage() {
        return null;
    }

    @Override
    public DataTrunk<Role> findPage(Sort sort) {
        return null;
    }

    @Override
    public DataTrunk<Role> findPage(Pageable pageable) {
        return null;
    }
}
