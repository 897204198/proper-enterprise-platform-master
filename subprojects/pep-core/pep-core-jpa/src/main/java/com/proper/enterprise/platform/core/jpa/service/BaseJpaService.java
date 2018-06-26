package com.proper.enterprise.platform.core.jpa.service;

import com.proper.enterprise.platform.core.api.IBase;
import com.proper.enterprise.platform.core.service.BaseService;
import com.proper.enterprise.platform.core.entity.DataTrunk;
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
public interface BaseJpaService<T extends IBase, ID extends Serializable> extends BaseService<T, ID> {

    /**
     * 更新
     * @param var1 var1
     * @param <S> S
     * @return 返回结果
     */
    <S extends T> S updateForSelective(S var1);

    /**
     * 条数
     * @param spec spec
     * @return 返回结果
     */
    long count(Specification<T> spec);

    /**
     * 条数
     * @param example example
     * @param <S> S
     * @return 返回结果
     */
    <S extends T> long count(Example<S> example);

    /**
     * 是否存在
     * @param example example
     * @param <S> S
     * @return 返回结果
     */
    <S extends T> boolean exists(Example<S> example);

    /**
     * 通过id 删除
     * @param var1 var1
     * @return 返回结果
     */
    boolean deleteById(ID var1);

    /**
     * 删除方法
     * @param entities entities
     */
    void deleteInBatch(Iterable<T> entities);

    /**
     * 删除方法
     */
    void deleteAllInBatch();

    /**
     * 查询
     * @param spec spec
     * @return 泛型
     */
    T findOne(Specification<T> spec);

    /**
     * 查询
     * @param example  example
     * @param <S> S
     * @return 返回结果
     */
    <S extends T> S findOne(Example<S> example);

    /**
     * 查询
     * @param id id
     * @return 返回结果泛型
     */
    T getOne(ID id);

    /**
     * 查询所有
     * @param spec spec
     * @return 返回集合
     */
    List<T> findAll(Specification<T> spec);

    /**
     * 分页查询
     * @param spec spec
     * @param pageable pageable
     * @return 返回结果
     */
    Page<T> findAll(Specification<T> spec, Pageable pageable);

    /**
     * 查询方法
     * @param spec spec
     * @param sort sort
     * @return 返回结果集合
     */
    List<T> findAll(Specification<T> spec, Sort sort);

    /**
     * 查询所有
     * @param example example
     * @param <S> S
     * @return 返回结果
     */
    <S extends T> List<S> findAll(Example<S> example);

    /**
     * 查询所有
     * @param example
     * @param sort
     * @param <S>
     * @return
     */
    <S extends T> List<S> findAll(Example<S> example, Sort sort);

    /**
     * 分页查询所有
     * @param example example
     * @param pageable pageable
     * @param <S> S
     * @return 返回结果
     */
    <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);

    /**
     * 分页查询
     * @param example example
     * @return 返回结果
     */
    DataTrunk<T> findPage(Example<T> example);

    /**
     * 分页查询
     * @param example example
     * @param pageable pageable
     * @param <S> S
     * @return 返回结果
     */
    <S extends T> DataTrunk<S> findPage(Example<S> example, Pageable pageable);

    /**
     * 分页查询并且有序
     * @param example example
     * @param sort sort
     * @param <S> S
     * @return 返回结果
     */
    <S extends T> DataTrunk<S> findPage(Example<S> example, Sort sort);

    /**
     * 分页查询
     * @param spec spec
     * @return 返回结果
     */
    DataTrunk<T> findPage(Specification<T> spec);

    /**
     * 分页查询
     * @param spec spec
     * @param pageable pageable
     * @return 返回结果
     */
    DataTrunk<T> findPage(Specification<T> spec, Pageable pageable);

    /**
     * 分页查询并且有序
     * @param spec spec
     * @param sort sort
     * @return 返回结果
     */
    DataTrunk<T> findPage(Specification<T> spec, Sort sort);
}
