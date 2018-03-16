package com.proper.enterprise.platform.core.jpa.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.jpa.service.BaseJpaService;
import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import com.proper.enterprise.platform.core.service.impl.ServiceSupport;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;

/**
 * JpaService基类的基础实现
 *
 * @param <T>   泛型
 * @param <R>   repository泛型
 * @param <IDT> ID泛型
 */
public abstract class JpaServiceSupport<T, R extends BaseJpaRepository, IDT extends Serializable>
    extends ServiceSupport<T, R, IDT> implements BaseJpaService<T, IDT> {
    @Override
    @SuppressWarnings("unchecked")
    public <S extends T> long count(Example<S> var1) {
        return getRepository().count(var1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public long count(Specification<T> var1) {
        return getRepository().count(var1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends T> boolean exists(Example<S> var1) {
        return getRepository().exists(var1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void deleteInBatch(Iterable<T> var1) {
        getRepository().deleteInBatch(var1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void deleteAllInBatch() {
        getRepository().deleteAllInBatch();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getOne(IDT var1) {
        return (T) getRepository().getOne(var1);
    }


    @Override
    @SuppressWarnings("unchecked")
    public <S extends T> S findOne(Example<S> var1) {
        return (S) getRepository().findOne(var1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T findOne(Specification<T> var1) {
        return (T) getRepository().findOne(var1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends T> List<S> findAll(Example<S> var1) {
        return getRepository().findAll(var1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends T> List<S> findAll(Example<S> var1, Sort var2) {
        return getRepository().findAll(var1, var2);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends T> Page<S> findAll(Example<S> var1, Pageable var2) {
        return getRepository().findAll(var1, var2);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll(Specification<T> var1) {
        return getRepository().findAll(var1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<T> findAll(Specification<T> var1, Pageable var2) {
        return getRepository().findAll(var1, var2);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll(Specification<T> var1, Sort var2) {
        return getRepository().findAll(var1, var2);
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataTrunk<T> findPage(Example<T> example) {
        return findPage(example, getPageRequest());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends T> DataTrunk<S> findPage(Example<S> example, Pageable pageable) {
        return new DataTrunk<>(this.findAll(example, pageable));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends T> DataTrunk<S> findPage(Example<S> example, Sort sort) {
        return findPage(example, getPageRequest(sort));
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataTrunk<T> findPage(Specification<T> spec) {
        return findPage(spec, getPageRequest());
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataTrunk<T> findPage(Specification<T> spec, Pageable pageable) {
        return getDataTrunk(getRepository().findAll(spec, pageable));
    }

    @Override
    @SuppressWarnings("unchecked")
    public DataTrunk<T> findPage(Specification<T> spec, Sort sort) {
        return findPage(spec, getPageRequest(sort));
    }
}
