package com.proper.enterprise.platform.core.neo4j.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.service.BaseService;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.cypher.query.Pagination;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Collection;


/**
 * Neo4jService基类
 * 方法中覆盖了PagingAndSortingRepository,Neo4jRepository中的全部方法
 * 用于给BaseNeo4jService提供基础增删改查,分页查询的基本实现
 * findData为PEP扩展方法 提供分页结果封装功能
 *
 * @param <T> 泛型
 */
public interface BaseNeo4jService<T, ID extends Serializable> extends BaseService<T, ID> {

    <S extends T> S save(S s, int depth);

    <S extends T> Iterable<S> save(Iterable<S> entities, int depth);

    T findOne(String id, int depth);

    Collection<T> findAll(int depth);

    Collection<T> findAll(Sort sort, int depth);

    Collection<T> findAll(Iterable<String> ids, int depth);

    Collection<T> findAll(Iterable<String> ids, Sort sort);

    Collection<T> findAll(Iterable<String> ids, Sort sort, int depth);

    @SuppressWarnings("unchecked")
    <I extends T> Collection<I> findAll(Class<I> classType, Filters filters);

    @SuppressWarnings("unchecked")
    <I extends T> Collection<I> findAll(Class<I> classType, Filters filters, int depth);

    @SuppressWarnings("unchecked")
    <I extends T> Collection<I> findAll(Class<I> classType, SortOrder sortOrder);

    @SuppressWarnings("unchecked")
    <I extends T> Collection<I> findAll(Class<I> classType, SortOrder sortOrder, int depth);

    @SuppressWarnings("unchecked")
    <I extends T> Collection<I> findAll(Class<I> classType, Filters filters, SortOrder sortOrder);

    @SuppressWarnings("unchecked")
    <I extends T> Collection<I> findAll(Class<I> classType, Filters filters, SortOrder sortOrder, int depth);

    Page<T> findAll(Pageable pageable, int depth);

    <I extends T> DataTrunk<I> findPage(Class<I> classType, Filters filters);

    <I extends T> DataTrunk<I> findPage(Class<I> classType, Filters filters, int depth);

    <I extends T> DataTrunk<I> findPage(Class<I> classType, SortOrder sortOrder);

    <I extends T> DataTrunk<I> findPage(Class<I> classType, SortOrder sortOrder, int depth);

    <I extends T> DataTrunk<I> findPage(Class<I> classType, Filters filters, SortOrder sortOrder);

    <I extends T> DataTrunk<I> findPage(Class<I> classType, Filters filters, SortOrder sortOrder, int depth);

    <I extends T> DataTrunk<I> findPage(Class<I> classType, Filters filters, SortOrder sortOrder, Pagination pagination);

    <I extends T> DataTrunk<I> findPage(Class<I> classType, Filters filters, SortOrder sortOrder, Pagination pagination, int depth);
}
