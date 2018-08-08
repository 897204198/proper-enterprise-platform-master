package com.proper.enterprise.platform.core.jpa.repository;

import com.proper.enterprise.platform.core.jpa.util.JPAUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.io.Serializable;

public class BaseJpaRepositoryImpl<T, IDT extends Serializable> extends SimpleJpaRepository<T, IDT> implements BaseJpaRepository<T, IDT> {

    private final EntityManager entityManager;
    private final JpaEntityInformation<T, ?> entityInformation;

    public BaseJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
    }


    @Override
    public T updateForSelective(T var1) {
        if (entityInformation.isNew(var1)) {
            throw new PersistenceException("entity not persist");
        }
        T oldEntity = this.findOne((IDT) entityInformation.getId(var1));
        BeanUtils.copyProperties(oldEntity, var1, JPAUtil.getNotNullColumnNames(var1));
        return this.save(var1);
    }

    @Override
    public boolean deleteById(IDT var1) {
        try {
            this.delete(var1);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}

