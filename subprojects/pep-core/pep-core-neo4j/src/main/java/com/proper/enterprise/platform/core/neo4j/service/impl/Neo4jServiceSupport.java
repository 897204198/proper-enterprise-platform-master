package com.proper.enterprise.platform.core.neo4j.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.neo4j.repository.BaseNeo4jRepository;
import com.proper.enterprise.platform.core.neo4j.service.BaseNeo4jService;
import com.proper.enterprise.platform.core.service.impl.ServiceSupport;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.cypher.query.Pagination;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Collection;

/**
 * Neo4jService基类的基础实现
 *
 * @param <T> 泛型
 * @param <R> repository泛型
 */
public abstract class Neo4jServiceSupport<T, R extends BaseNeo4jRepository, IDT extends Serializable>
    extends ServiceSupport<T, R, IDT> implements BaseNeo4jService<T, IDT> {
    @Autowired
    private Session session;

    public Session getSession() {
        return session;
    }

    private Pagination getPagination() {
        return new Pagination(getPageRequest().getPageNumber(), getPageRequest().getPageSize());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends T> S save(S s, int depth) {
        return (S) getRepository().save(s, depth);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends T> Iterable<S> save(Iterable<S> entities, int depth) {
        return getRepository().save(entities, depth);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T findOne(String idt, int depth) {
        return (T) getRepository().findOne(idt, depth);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<T> findAll(int depth) {
        return (Collection<T>) getRepository().findAll(depth);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<T> findAll(Sort sort, int depth) {
        return (Collection<T>) getRepository().findAll(sort, depth);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<T> findAll(Iterable<String> ids, int depth) {
        return (Collection<T>) getRepository().findAll(ids, depth);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<T> findAll(Iterable<String> ids, Sort sort) {
        return (Collection<T>) getRepository().findAll(ids, sort);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<T> findAll(Iterable<String> ids, Sort sort, int depth) {
        return (Collection<T>) getRepository().findAll(ids, sort, depth);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends T> Collection<I> findAll(Class<I> classType, Filters filters) {
        return getSession().loadAll(classType, filters, new SortOrder(), 1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends T> Collection<I> findAll(Class<I> classType, Filters filters, int depth) {
        return getSession().loadAll(classType, filters, new SortOrder(), depth);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends T> Collection<I> findAll(Class<I> classType, SortOrder sortOrder) {
        return getSession().loadAll(classType, new Filters(), sortOrder, 1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends T> Collection<I> findAll(Class<I> classType, SortOrder sortOrder, int depth) {
        return getSession().loadAll(classType, new Filters(), sortOrder, depth);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends T> Collection<I> findAll(Class<I> classType, Filters filters, SortOrder sortOrder) {
        return getSession().loadAll(classType, filters, sortOrder, 1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends T> Collection<I> findAll(Class<I> classType, Filters filters, SortOrder sortOrder, int depth) {
        return getSession().loadAll(classType, filters, sortOrder, depth);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<T> findAll(Pageable pageable, int depth) {
        return getRepository().findAll(pageable, depth);
    }


    @Override
    public <I extends T> DataTrunk<I> findPage(Class<I> classType, Filters filters) {
        return findPage(classType, filters, new SortOrder(), getPagination());
    }

    @Override
    public <I extends T> DataTrunk<I> findPage(Class<I> classType, Filters filters, int depth) {
        return findPage(classType, filters, new SortOrder(), getPagination(), depth);
    }

    @Override
    public <I extends T> DataTrunk<I> findPage(Class<I> classType, SortOrder sortOrder) {
        return findPage(classType, new Filters(), sortOrder, getPagination());
    }

    @Override
    public <I extends T> DataTrunk<I> findPage(Class<I> classType, SortOrder sortOrder, int depth) {
        return findPage(classType, new Filters(), sortOrder, getPagination(), depth);
    }

    @Override
    public <I extends T> DataTrunk<I> findPage(Class<I> classType, Filters filters, SortOrder sortOrder) {
        return findPage(classType, filters, sortOrder, getPagination());
    }

    @Override
    public <I extends T> DataTrunk<I> findPage(Class<I> classType, Filters filters, SortOrder sortOrder, int depth) {
        return findPage(classType, filters, sortOrder, getPagination(), depth);
    }

    @Override
    public <I extends T> DataTrunk<I> findPage(Class<I> classType, Filters filters, SortOrder sortOrder, Pagination pagination) {
        return findPage(classType, filters, sortOrder, pagination, 1);
    }

    @Override
    public <I extends T> DataTrunk<I> findPage(Class<I> classType, Filters filters, SortOrder sortOrder, Pagination pagination, int depth) {
        return new DataTrunk<>(getSession().loadAll(classType, filters, sortOrder, pagination, depth), getSession().count(classType, filters));
    }
}
