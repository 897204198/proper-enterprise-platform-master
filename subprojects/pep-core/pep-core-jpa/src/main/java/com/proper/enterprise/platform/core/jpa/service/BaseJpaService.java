package com.proper.enterprise.platform.core.jpa.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.service.BaseService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;

/**
 * JpaService基类
 * 方法中覆盖了PagingAndSortingRepository,QueryByExampleExecutor,JpaSpecificationExecutor中的全部方法
 * 用于给JpaService提供基础增删改查,分页查询的基本实现
 * findData为PEP扩展方法 提供分页结果封装功能
 *
 * @param <T>  泛型
 * @param <ID> 主键泛型
 */
public interface BaseJpaService<T, ID extends Serializable> extends BaseService<T, ID> {

    long count(Specification<T> spec);

    <S extends T> long count(Example<S> example);

    <S extends T> boolean exists(Example<S> example);

    void deleteInBatch(Iterable<T> entities);

    void deleteAllInBatch();

    T findOne(Specification<T> spec);

    <S extends T> S findOne(Example<S> example);

    T getOne(ID id);

    List<T> findAll(Specification<T> spec);

    Page<T> findAll(Specification<T> spec, Pageable pageable);

    List<T> findAll(Specification<T> spec, Sort sort);

    <S extends T> List<S> findAll(Example<S> example);

    <S extends T> List<S> findAll(Example<S> example, Sort sort);

    <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);

    DataTrunk<T> findData(Example<T> example);

    <S extends T> DataTrunk<S> findData(Example<S> example, Pageable pageable);

    <S extends T> DataTrunk<S> findData(Example<S> example, Sort sort);

    DataTrunk<T> findData(Specification<T> spec);

    DataTrunk<T> findData(Specification<T> spec, Pageable pageable);

    DataTrunk<T> findData(Specification<T> spec, Sort sort);
}
