package com.proper.enterprise.platform.auth.common.mock;

import com.proper.enterprise.platform.api.auth.dao.MenuDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MockMenuDAO implements MenuDao {
    @Override
    public Menu get(String id) {
        return null;
    }

    @Override
    public Collection<? extends Menu> findParents(EnableEnum menuEnable) {
        return null;
    }

    @Override
    public Collection<? extends Menu> findAllEq(String name) {
        return null;
    }

    @Override
    public Collection<? extends Menu> findAll(String name, String description, String route, EnableEnum enable, String parentId) {
        return null;
    }

    @Override
    public Collection<Menu> findAll() {
        return null;
    }

    @Override
    public Collection<Menu> findAll(Sort var1) {
        return null;
    }

    @Override
    public Page<Menu> findAll(Pageable var1) {
        return null;
    }

    @Override
    public Collection<Menu> findAllById(Iterable<String> var1) {
        return null;
    }

    @Override
    public DataTrunk<? extends Menu> findPage(String name, String description, String route, EnableEnum enable, String parentId) {
        return null;
    }

    @Override
    public DataTrunk<Menu> findPage() {
        return null;
    }

    @Override
    public DataTrunk<Menu> findPage(Sort sort) {
        return null;
    }

    @Override
    public DataTrunk<Menu> findPage(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Menu> S save(S var1) {
        return null;
    }

    @Override
    public <S extends Menu> Collection<S> save(Iterable<S> var1) {
        return null;
    }

    @Override
    public Menu findById(String var1) {
        return null;
    }

    @Override
    public boolean existsById(String var1) {
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
    public void delete(Menu var1) {

    }

    @Override
    public void delete(Iterable<? extends Menu> var1) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Menu updateForSelective(Menu menu) {
        return null;
    }
}
