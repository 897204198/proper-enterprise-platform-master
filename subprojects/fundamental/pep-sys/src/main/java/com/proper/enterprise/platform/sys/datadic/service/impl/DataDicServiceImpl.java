package com.proper.enterprise.platform.sys.datadic.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.enums.EnableEnum;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.entity.DataDicEntity;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import com.proper.enterprise.platform.core.i18n.I18NUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class DataDicServiceImpl extends AbstractJpaServiceSupport<DataDic, DataDicRepository, String> implements DataDicService {

    @Autowired
    private DataDicRepository repository;

    @Override
    public DataDicRepository getRepository() {
        return repository;
    }

    @Override
    public Collection<? extends DataDic> findByCatalog(String catalog) {
        return this.findByCatalog(catalog, null);
    }

    @Override
    public Collection<? extends DataDic> findByCatalog(String catalog, DataDicTypeEnum dataDicType) {
        return this.findByCatalog(catalog, dataDicType, EnableEnum.ALL);
    }

    @Override
    @CacheQuery
    public Collection<? extends DataDic> findByCatalog(String catalog, DataDicTypeEnum dataDicType, EnableEnum enable) {
        if (StringUtil.isEmpty(catalog)) {
            return new ArrayList<>();
        }
        if (null == enable) {
            enable = EnableEnum.ENABLE;
        }
        EnableEnum finalEnable = enable;
        Specification<DataDic> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.and(cb.equal(root.get("catalog"), catalog)));
            if (null != dataDicType) {
                predicates.add(cb.and(cb.equal(root.get("dataDicType"), dataDicType)));
            }
            if (EnableEnum.ALL != finalEnable) {
                predicates.add(cb.and(cb.equal(root.get("enable"), finalEnable == EnableEnum.ENABLE)));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return this.findAll(specification, new Sort(Sort.Direction.ASC, "order"));
    }

    @Override
    public Collection<? extends DataDic> find(String catalog, String code, String name, DataDicTypeEnum dataDicType, EnableEnum enable) {
        return findAll(buildQuerySpec(catalog, code, name, dataDicType, enable));
    }

    @Override
    public DataTrunk<? extends DataDic> findPage(String catalog, String code, String name, DataDicTypeEnum dataDicType, EnableEnum enable) {
        return findPage(buildQuerySpec(catalog, code, name, dataDicType, enable));
    }


    private Specification<DataDic> buildQuerySpec(String catalog, String code, String name, DataDicTypeEnum dataDicType, EnableEnum enable) {
        if (null == enable) {
            enable = EnableEnum.ALL;
        }
        EnableEnum finalEnable = enable;
        Specification<DataDic> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtil.isNotEmpty(catalog)) {
                predicates.add(cb.and(cb.equal(root.get("catalog"), catalog)));
            }
            if (StringUtil.isNotEmpty(code)) {
                predicates.add(cb.and(cb.equal(root.get("code"), code)));
            }
            if (StringUtil.isNotEmpty(name)) {
                predicates.add(cb.and(cb.equal(root.get("name"), name)));
            }
            if (null != dataDicType) {
                predicates.add(cb.and(cb.equal(root.get("dataDicType"), dataDicType)));
            }
            if (EnableEnum.ALL != finalEnable) {
                predicates.add(cb.and(cb.equal(root.get("enable"), finalEnable == EnableEnum.ENABLE)));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return specification;
    }


    @Override
    @CacheQuery
    public DataDic get(String catalog, String code) {
        return get(catalog, code, null);
    }

    @Override
    @CacheQuery
    public DataDic get(String catalog, String code, DataDicTypeEnum dataDicType) {
        Specification<DataDic> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.and(cb.equal(root.get("catalog"), catalog)));
            predicates.add(cb.and(cb.equal(root.get("code"), code)));
            if (null != dataDicType) {
                predicates.add(cb.and(cb.equal(root.get("dataDicType"), dataDicType)));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return this.findOne(specification).orElse(null);
    }

    @Override
    public DataDic get(Enum dataDicEnum) {
        return this.get(dataDicEnum.getClass().getSimpleName(), dataDicEnum.name());
    }

    @Override
    public DataDic get(String id) {
        return repository.getOne(id);
    }

    @Override
    @CacheQuery
    public DataDic getDefault(String catalog) {
        Specification<DataDic> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.and(cb.equal(root.get("catalog"), catalog)));
            predicates.add(cb.and(cb.equal(root.get("enable"), true)));
            predicates.add(cb.and(cb.equal(root.get("deft"), true)));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        List<DataDic> defaults = this.findAll(specification);
        if (CollectionUtil.isEmpty(defaults)) {
            return null;
        }
        return defaults.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends DataDic> S save(S dataDic) {
        DataDicEntity  dataDicEntity = (DataDicEntity) dataDic;
        if (null == dataDicEntity.getDataDicType()) {
            dataDicEntity.setDataDicType(DataDicTypeEnum.SYSTEM);
        }
        if (null == dataDic.getEnable()) {
            dataDicEntity.setEnable(true);
        }
        if (null == dataDicEntity.getDeft()) {
            dataDicEntity.setDeft(false);
        }
        validate(dataDicEntity);
        dataDicEntity = repository.save(dataDicEntity);
        dataDic = (S) dataDicEntity;
        return dataDic;
    }

    @Override
    public DataDic update(DataDic dataDic) {
        validate(dataDic);
        return this.updateForSelective(dataDic);
    }

    @Override
    public void updateCatalog(String catalog, String changeCatalog) {
        Collection<? extends DataDic> dataDics = this.findByCatalog(catalog);
        if (CollectionUtil.isEmpty(dataDics)) {
            return;
        }
        for (DataDic dataDic : dataDics) {
            dataDic.setCatalog(changeCatalog);
            this.updateForSelective(dataDic);
        }
    }

    @Override
    public boolean deleteByIds(String ids) {
        if (StringUtil.isEmpty(ids)) {
            return false;
        }
        String[] idArr = ids.split(",");
        Collection<DataDic> dataDics = this.findAllById(Arrays.asList(idArr));
        if (CollectionUtil.isEmpty(dataDics)) {
            return false;
        }
        this.delete(dataDics);
        return true;
    }

    private void validate(DataDic dataDic) {
        validateEmpty(dataDic);
        validateUnique(dataDic);
    }

    private void validateEmpty(DataDic dataDic) {
        if (StringUtil.isEmpty(dataDic.getCatalog())) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.sys.datadic.catalog.empty"));
        }
        if (StringUtil.isEmpty(dataDic.getCode())) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.sys.datadic.code.empty"));
        }
        if (StringUtil.isEmpty(dataDic.getName())) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.sys.datadic.name.empty"));

        }
    }

    private void validateUnique(DataDic dataDic) {
        DataDic oldDataDic = get(dataDic.getCatalog(), dataDic.getCode());
        if (null == oldDataDic) {
            return;
        }
        if (StringUtil.isEmpty(dataDic.getId()) || !dataDic.getId().equals(oldDataDic.getId())) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.sys.datadic.unique"));
        }
    }


}
