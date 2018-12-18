package com.proper.enterprise.platform.core.jpa.repository;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>, BaseRepository<T, ID> {

    /**
     * 更新
     *
     * @param var1 var1
     * @return T 泛型
     */
    T updateForSelective(T var1);


    /**
     * 根据Id删除并返回是否删除成功
     *
     * @param id id
     * @return true删除成功 false删除失败
     */
    boolean delete(ID id);

}
