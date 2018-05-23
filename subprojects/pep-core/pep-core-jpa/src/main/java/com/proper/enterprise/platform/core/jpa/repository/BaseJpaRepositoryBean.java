package com.proper.enterprise.platform.core.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class BaseJpaRepositoryBean<R extends JpaRepository<T, IDT>, T, IDT extends Serializable> extends JpaRepositoryFactoryBean<R, T, IDT> {
    public BaseJpaRepositoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    protected RepositoryFactorySupport createRepositoryFactory(
        EntityManager entityManager) {
        return new ExpandJpaRepositoryFactory<T, IDT>(entityManager);
    }

    private static class ExpandJpaRepositoryFactory<T, IDT extends Serializable>
        extends JpaRepositoryFactory {

        private final EntityManager entityManager;

        public ExpandJpaRepositoryFactory(EntityManager entityManager) {

            super(entityManager);
            this.entityManager = entityManager;
        }

        protected Object getTargetRepository(RepositoryMetadata metadata) {
            JpaEntityInformation<T, Serializable> entityInformation =
                (JpaEntityInformation<T, Serializable>) getEntityInformation(metadata.getDomainType());
            return new BaseJpaRepositoryImpl<T, IDT>(entityInformation, entityManager);
        }

        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return BaseJpaRepositoryImpl.class;
        }
    }
}
