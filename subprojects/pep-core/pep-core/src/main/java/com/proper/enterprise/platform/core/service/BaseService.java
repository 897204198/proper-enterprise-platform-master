package com.proper.enterprise.platform.core.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Collection;

/**
 * Service基类
 * 方法中覆盖了PagingAndSortingRepository中的全部方法
 * 用于给Service提供基础增删改查,分页查询的基本实现
 * findData为PEP扩展方法  提供分页结果封装功能
 *
 * @param <T>  泛型
 * @param <ID> 主键泛型
 */
public interface BaseService<T, ID extends Serializable> {

    <S extends T> S save(S var1);

    <S extends T> Collection<S> save(Iterable<S> var1);

    T findOne(ID var1);

    boolean exists(ID var1);

    long count();

    void delete(ID var1);

    void delete(T var1);

    void delete(Iterable<? extends T> var1);

    void deleteAll();

    Collection<T> findAll();

    Collection<T> findAll(Iterable<ID> var1);

    Collection<T> findAll(Sort var1);

    Page<T> findAll(Pageable var1);

    DataTrunk<T> findData();

    DataTrunk<T> findData(Sort sort);

    DataTrunk<T> findData(Pageable pageable);
}
