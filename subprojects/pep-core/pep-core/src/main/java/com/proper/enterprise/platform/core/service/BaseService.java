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

    /**
     * 保存方法
     * @param var1 var1
     * @param <S> <S>
     * @return 对象
     */
    <S extends T> S save(S var1);

    /**
     * 保存方法
     * @param var1 var1
     * @param <S> <S>
     * @return 对象
     */
    <S extends T> Collection<S> save(Iterable<S> var1);

    /**
     * 根据Id查询
     * @param var1 主键泛型
     * @return 泛型
     */
    T findOne(ID var1);

    /**
     * 是否存在
     * @param var1 主键泛型
     * @return boolean类型
     */
    boolean exists(ID var1);

    /**
     * 数量
     * @return long型
     */
    long count();

    /**
     * 删除方法
     * @param var1 主键泛型
     */
    void delete(ID var1);

    /**
     * 删除方法
     * @param var1 泛型
     */
    void delete(T var1);

    /**
     * 删除方法
     * @param var1 泛型
     */
    void delete(Iterable<? extends T> var1);

    /**
     * 删除所有
     */
    void deleteAll();

    /**
     * 查询所有
     * @return 集合
     */
    Collection<T> findAll();

    /**
     * 查询所有
     * @param var1 主键泛型
     * @return 集合
     */
    Collection<T> findAll(Iterable<ID> var1);

    /**
     * 查询所有并且有顺序
     * @param var1 顺序
     * @return 集合
     */
    Collection<T> findAll(Sort var1);

    /**
     * 查询所有并分页
     * @param var1 分页
     * @return 分页Page类型
     */
    Page<T> findAll(Pageable var1);

    /**
     * 查询分页
     * @return DataTrunk类型
     */
    DataTrunk<T> findPage();

    /**
     * 查询分页并有顺序
     * @param sort sort
     * @return DataTrunk类型
     */
    DataTrunk<T> findPage(Sort sort);

    /**
     * 查询并分页
     * @param pageable pageable
     * @return DataTrunk类型
     */
    DataTrunk<T> findPage(Pageable pageable);
}
