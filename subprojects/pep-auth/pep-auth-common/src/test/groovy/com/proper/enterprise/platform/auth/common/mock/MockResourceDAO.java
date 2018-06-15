package com.proper.enterprise.platform.auth.common.mock;

import com.proper.enterprise.platform.api.auth.dao.ResourceDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MockResourceDAO implements ResourceDao {
    @Override
    public Resource save(Resource resource) {
        return null;
    }

    @Override
    public <S extends Resource> Collection<S> save(Iterable<S> var1) {
        return null;
    }

    @Override
    public Resource updateForSelective(Resource resource) {
        return null;
    }

    @Override
    public Collection<? extends Resource> findAll(EnableEnum enableEnum) {
        return null;
    }

    @Override
    public Collection<? extends Resource> findAll(String name, EnableEnum enableEnum) {
        return null;
    }

    @Override
    public Collection<? extends Resource> findAll(Collection<String> ids) {
        return null;
    }

    @Override
    public Collection<Resource> findAll() {
        return null;
    }

    @Override
    public Collection<Resource> findAll(Iterable<String> var1) {
        return null;
    }

    @Override
    public Collection<Resource> findAll(Sort var1) {
        return null;
    }

    @Override
    public Page<Resource> findAll(Pageable var1) {
        return null;
    }


    @Override
    public Resource getNewResourceEntity() {
        return null;
    }

    @Override
    public Resource get(String id) {
        return null;
    }

    @Override
    public Resource findOne(String var1) {
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
    public void delete(Resource var1) {

    }

    @Override
    public void delete(Iterable<? extends Resource> var1) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public DataTrunk<Resource> findPage() {
        return null;
    }

    @Override
    public DataTrunk<Resource> findPage(Sort sort) {
        return null;
    }

    @Override
    public DataTrunk<Resource> findPage(Pageable pageable) {
        return null;
    }
}
