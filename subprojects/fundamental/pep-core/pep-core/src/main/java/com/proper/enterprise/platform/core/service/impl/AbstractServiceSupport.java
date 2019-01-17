package com.proper.enterprise.platform.core.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.core.service.BaseService;
import com.proper.enterprise.platform.core.support.AbstractQuerySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

/**
 * Service基类的基础实现
 *
 * @param <T>   泛型
 * @param <R>   repository泛型
 * @param <IDT> ID泛型
 */
public abstract class AbstractServiceSupport<T, R extends BaseRepository, IDT extends Serializable> extends AbstractQuerySupport
    implements BaseService<T, IDT> {

    /**
     * getRepository()
     *
     * @return repository泛型
     */
    public abstract R getRepository();

    @Override
    @SuppressWarnings("unchecked")
    public <S> S save(S var1) {
        return (S) getRepository().save(var1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends T> Collection<S> save(Iterable<S> var1) {
        return (Collection<S>) getRepository().saveAll(var1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T findById(IDT var1) {
        Optional<T> result = getRepository().findById(var1);
        return result.orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean existsById(IDT var1) {
        return getRepository().existsById(var1);
    }


    @Override
    public long count() {
        return getRepository().count();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(IDT var1) {
        getRepository().delete(var1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(T var1) {
        getRepository().delete(var1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(Iterable<? extends T> var1) {
        getRepository().deleteAll(var1);
    }

    @Override
    public void deleteAll() {
        getRepository().deleteAll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<T> findAll() {
        return (Collection<T>) getRepository().findAll();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<T> findAll(Sort var1) {
        return (Collection<T>) getRepository().findAll(var1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<T> findAll(Pageable var1) {
        return getRepository().findAll(var1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<T> findAllById(Iterable<IDT> var1) {
        return (Collection<T>) getRepository().findAllById(var1);
    }

    @Override
    public DataTrunk<T> findPage() {
        return this.findPage(getPageRequest());
    }

    @Override
    public DataTrunk<T> findPage(Sort sort) {
        return this.findPage(getPageRequest(sort));
    }

    @Override
    public DataTrunk<T> findPage(Pageable pageable) {
        return getDataTrunk(this.findAll(pageable));
    }

    public DataTrunk<T> getDataTrunk(Iterable<T> data) {
        Collection<T> collection = (Collection<T>) data;
        return getDataTrunk(collection);
    }

    public DataTrunk<T> getDataTrunk(Collection<T> collection) {
        return new DataTrunk<>(collection, collection.size());
    }

    public DataTrunk<T> getDataTrunk(Page<T> data) {
        return new DataTrunk<>(data);
    }

    public DataTrunk<T> getDataTrunk(Iterable<T> data, long count) {
        Collection<T> collection = (Collection<T>) data;
        return new DataTrunk<>(collection, count);
    }

    public DataTrunk<T> getDataTrunk(Collection<T> allData, int pageNo, int pageSize) {
        return new DataTrunk<>(allData, pageNo, pageSize);
    }


}
