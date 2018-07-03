package com.proper.enterprise.platform.auth.common.jpa.dao.impl;

import com.proper.enterprise.platform.api.auth.dao.ResourceDao;
import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.auth.common.jpa.entity.ResourceEntity;
import com.proper.enterprise.platform.auth.common.jpa.repository.ResourceRepository;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ResourceDaoImpl extends AbstractJpaServiceSupport<Resource, ResourceRepository, String> implements ResourceDao {

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public ResourceRepository getRepository() {
        return resourceRepository;
    }

    @Override
    public Resource save(Resource resource) {
        if (null == resource.getEnable()) {
            resource.setEnable(true);
        }
        return resourceRepository.save((ResourceEntity) resource);
    }

    @Override
    public Resource updateForSelective(Resource resource) {
        return super.updateForSelective(resource);
    }

    @Override
    public Collection<? extends Resource> findAll(EnableEnum enableEnum) {
        switch (enableEnum) {
            case ALL:
                return this.findAll();
            case DISABLE:
                return resourceRepository.findAllByEnable(false);
            case ENABLE:
            default:
                return resourceRepository.findAllByEnable(true);
        }
    }

    @Override
    public Collection<? extends Resource> findAll(String name, EnableEnum enable) {
        Specification<Resource> specification = new Specification<Resource>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtil.isNotNull(name)) {
                    predicates.add(cb.like(root.get("name"), "%".concat(name).concat("%")));
                }
                if (null != enable && EnableEnum.ALL != enable) {
                    predicates.add(cb.equal(root.get("enable"), enable == EnableEnum.ENABLE));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return findAll(specification);
    }

    @Override
    public Collection<? extends Resource> findAll(Collection<String> ids) {
        return resourceRepository.findAllById(ids);
    }

    @Override
    public Resource getNewResourceEntity() {
        return new ResourceEntity();
    }

    @Override
    public Resource get(String id) {
        return resourceRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteAll() {
        resourceRepository.deleteAll();
    }
}
