package com.proper.enterprise.platform.core.neo4j.service.impl;

import com.proper.enterprise.platform.core.neo4j.repository.BaseNeo4jRepository;
import com.proper.enterprise.platform.core.neo4j.service.BaseNeo4jService;
import com.proper.enterprise.platform.core.service.impl.ServiceSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * Neo4jService基类的基础实现
 *
 * @param <T> 泛型
 * @param <R> repository泛型
 */
public abstract class Neo4jServiceSupport<T, R extends BaseNeo4jRepository, IDT extends Serializable>
    extends ServiceSupport<T, R, IDT> implements BaseNeo4jService<T, IDT> {

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
    public T findOne(Long idt, int depth) {
        return (T) getRepository().findOne(idt, depth);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterable<T> findAll(int depth) {
        return getRepository().findAll(depth);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterable<T> findAll(Sort sort, int depth) {
        return getRepository().findAll(sort, depth);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterable<T> findAll(Iterable<Long> ids, int depth) {
        return getRepository().findAll(ids, depth);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterable<T> findAll(Iterable<Long> ids, Sort sort) {
        return getRepository().findAll(ids, sort);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterable<T> findAll(Iterable<Long> ids, Sort sort, int depth) {
        return getRepository().findAll(ids, sort, depth);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page<T> findAll(Pageable pageable, int depth) {
        return getRepository().findAll(pageable, depth);
    }
}
