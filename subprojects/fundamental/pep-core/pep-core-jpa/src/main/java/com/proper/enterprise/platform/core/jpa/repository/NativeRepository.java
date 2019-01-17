package com.proper.enterprise.platform.core.jpa.repository;

import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Component
public class NativeRepository {

    @PersistenceContext
    EntityManager em;

    public List executeQuery(String sql) {
        return em.createNativeQuery(sql).getResultList();
    }

    public List executeQuery(String sql, Map<String, Object> params) {
        Query query = em.createNativeQuery(sql);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }


    @SuppressWarnings("deprecation")
    public List executeEntityMapQuery(String sql) {
        Query query = em.createNativeQuery(sql);
        // TODO: 2019/1/11 待处理  过期的方法 jpa并未提出替换方案 但是方法过期了等提出方案后替换
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }

    public int executeUpdate(String sql) {
        return em.createNativeQuery(sql).executeUpdate();
    }

    public int executeUpdate(String sql, Map<String, Object> params) {
        Query query = em.createNativeQuery(sql);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.executeUpdate();
    }

    public int executeJpqlUpdate(String sql, Map<String, Object> params) {
        Query query = em.createQuery(sql);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.executeUpdate();
    }

}
