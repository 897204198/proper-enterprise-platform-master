package com.proper.enterprise.platform.sys.datadic.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.enums.EnableEnum;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.jpa.annotation.CacheQuery;
import com.proper.enterprise.platform.core.jpa.service.impl.JpaServiceSupport;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.DataDic;
import com.proper.enterprise.platform.sys.datadic.entity.DataDicEntity;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;
import com.proper.enterprise.platform.sys.datadic.repository.DataDicRepository;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class DataDicServiceImpl extends JpaServiceSupport<DataDic, DataDicRepository, String> implements DataDicService {

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
        return this.findAll(specification);
    }

    @Override
    public Collection<? extends DataDic> find(DataDicTypeEnum dataDicType) {
        return findAll(buildSpec(dataDicType));
    }

    @Override
    public DataTrunk<? extends DataDic> findPage(DataDicTypeEnum dataDicType) {
        return findPage(buildSpec(dataDicType));
    }

    private Specification<DataDic> buildSpec(DataDicTypeEnum dataDicType) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (null != dataDicType) {
                predicates.add(cb.and(cb.equal(root.get("dataDicType"), dataDicType)));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    @Override
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
        return this.findOne(specification);
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
            predicates.add(cb.and(cb.equal(root.get("isDefault"), true)));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        List<DataDic> defaults = this.findAll(specification);
        if (CollectionUtil.isEmpty(defaults)) {
            return null;
        }
        return defaults.get(0);
    }

    @Override
    public DataDic save(DataDic dataDic) {
        if (null == dataDic.getDataDicType()) {
            dataDic.setDataDicType(DataDicTypeEnum.SYSTEM);
        }
        if (null == dataDic.getEnable()) {
            dataDic.setEnable(true);
        }
        validate(dataDic);
        return repository.save((DataDicEntity) dataDic);
    }

    @Override
    public DataDic update(DataDic dataDic) {
        validate(dataDic);
        return this.updateForSelective(dataDic);
    }

    @Override
    public boolean deleteByIds(String ids) {
        if (StringUtil.isEmpty(ids)) {
            return false;
        }
        String[] idArr = ids.split(",");
        Collection<DataDic> dataDics = this.findAll(Arrays.asList(idArr));
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
