package com.proper.enterprise.platform.sys.datadic.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.enums.EnableEnum;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.entity.DataDicCatalogEntity;
import com.proper.enterprise.platform.sys.datadic.enums.DataDicTypeEnum;
import com.proper.enterprise.platform.sys.datadic.repository.DataDicCatalogRepository;
import com.proper.enterprise.platform.sys.datadic.service.DataDicCatalogService;
import com.proper.enterprise.platform.sys.datadic.service.DataDicService;
import com.proper.enterprise.platform.sys.datadic.vo.DataDicCatalogVO;
import com.proper.enterprise.platform.core.i18n.I18NUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataDicCatalogServiceImpl implements DataDicCatalogService {

    private DataDicCatalogRepository dataDicCatalogRepository;

    private DataDicService dataDicService;

    @Autowired
    public DataDicCatalogServiceImpl(DataDicCatalogRepository dataDicCatalogRepository,
                                     DataDicService dataDicService) {
        this.dataDicCatalogRepository = dataDicCatalogRepository;
        this.dataDicService = dataDicService;
    }


    @Override
    public Collection<DataDicCatalogVO> findAll(String catalogCode, String catalogName,
                                                DataDicTypeEnum catalogType, EnableEnum enable) {
        Boolean searchEnable = true;
        if (EnableEnum.ALL == enable || null == enable) {
            searchEnable = null;
        }
        if (EnableEnum.ENABLE == enable) {
            searchEnable = true;
        }
        if (EnableEnum.DISABLE == enable) {
            searchEnable = false;
        }
        return BeanUtil.convert(dataDicCatalogRepository.findAll(catalogCode, catalogName,
            catalogType, searchEnable), DataDicCatalogVO.class);
    }

    @Override
    public DataTrunk<DataDicCatalogVO> findPage(String catalogCode, String catalogName,
                                                DataDicTypeEnum catalogType, EnableEnum enable, Pageable pageable) {
        Boolean searchEnable = true;
        if (EnableEnum.ALL == enable || null == enable) {
            searchEnable = null;
        }
        if (EnableEnum.ENABLE == enable) {
            searchEnable = true;
        }
        if (EnableEnum.DISABLE == enable) {
            searchEnable = false;
        }
        return BeanUtil.convert(dataDicCatalogRepository.findAll(catalogCode, catalogName,
            catalogType, searchEnable, pageable), DataDicCatalogVO.class);
    }

    @Override
    public Collection<DataDicCatalogVO> findByParentCatalog(String parentCatalog, DataDicTypeEnum dataDicType, EnableEnum enable) {
        if (StringUtil.isEmpty(parentCatalog)) {
            return new ArrayList<>();
        }
        if (null == enable) {
            enable = EnableEnum.ENABLE;
        }
        Collection<DataDicCatalogEntity> dataDicCatalogEntities = dataDicCatalogRepository.findAllByParentCatalogCode(
            parentCatalog,
            dataDicType,
            EnableEnum.ALL != enable ? enable == EnableEnum.ENABLE : null);
        if (null == dataDicCatalogEntities) {
            return new ArrayList<>();
        }
        Collection<DataDicCatalogVO> dataDicCatalogVOS = new ArrayList<>();
        DataDicCatalogVO dataDicCatalogVO = BeanUtil.convert(dataDicCatalogRepository.findByCatalogCode(parentCatalog), DataDicCatalogVO.class);
        dataDicCatalogVO.setDataDics(dataDicService.findByCatalog(parentCatalog, dataDicType, enable));
        dataDicCatalogVOS.add(dataDicCatalogVO);
        for (DataDicCatalogEntity dataDicCatalogEntity : dataDicCatalogEntities) {
            dataDicCatalogVOS.addAll(findByParentCatalog(dataDicCatalogEntity.getCatalogCode(), dataDicType, enable));
        }
        return dataDicCatalogVOS;
    }

    @Override
    public DataDicCatalogVO get(String id) {
        return BeanUtil.convert(dataDicCatalogRepository.findById(id).orElse(null), DataDicCatalogVO.class);
    }

    @Override
    public DataDicCatalogVO save(DataDicCatalogVO dataDicCatalogVO) {
        if (null == dataDicCatalogVO.getEnable()) {
            dataDicCatalogVO.setEnable(true);
        }
        if (null == dataDicCatalogVO.getCatalogType()) {
            dataDicCatalogVO.setCatalogType(DataDicTypeEnum.SYSTEM);
        }
        if (null == dataDicCatalogVO.getSort()) {
            dataDicCatalogVO.setSort(1);
        }
        validUnique(dataDicCatalogVO);
        DataDicCatalogEntity catalogEntity = BeanUtil.convert(dataDicCatalogVO, DataDicCatalogEntity.class);
        if (StringUtil.isNotEmpty(dataDicCatalogVO.getParentId())) {
            catalogEntity.setParent(dataDicCatalogRepository.getOne(dataDicCatalogVO.getParentId()));
        }
        return BeanUtil.convert(dataDicCatalogRepository.save(catalogEntity), DataDicCatalogVO.class);
    }

    @Override
    public DataDicCatalogVO update(DataDicCatalogVO dataDicCatalogVO) {
        validUnique(dataDicCatalogVO);
        DataDicCatalogEntity oldCatalog = dataDicCatalogRepository.findById(dataDicCatalogVO.getId()).<ErrMsgException>orElseThrow(() -> {
            throw new ErrMsgException("Could NOT find data dic cataglog with " + dataDicCatalogVO.getId());
        });
        String oldCatalogCode = oldCatalog.getCatalogCode();
        DataDicCatalogEntity catalogEntity = BeanUtil.convert(dataDicCatalogVO, DataDicCatalogEntity.class);
        if (StringUtil.isNotEmpty(dataDicCatalogVO.getParentId())) {
            catalogEntity.setParent(dataDicCatalogRepository.getOne(dataDicCatalogVO.getParentId()));
        }
        DataDicCatalogEntity dataDicCatalogEntity = dataDicCatalogRepository.updateForSelective(catalogEntity);
        if (!dataDicCatalogEntity.getCatalogCode().equals(oldCatalogCode)) {
            dataDicService.updateCatalog(oldCatalogCode, dataDicCatalogEntity.getCatalogCode());
        }
        return BeanUtil.convert(dataDicCatalogEntity, DataDicCatalogVO.class);
    }

    @Override
    public boolean deleteByIds(String ids) {
        if (StringUtil.isEmpty(ids)) {
            return false;
        }
        String[] idArr = ids.split(",");
        Collection<DataDicCatalogEntity> dataCatalogs = dataDicCatalogRepository.findAllById(Arrays.asList(idArr));
        if (CollectionUtil.isEmpty(dataCatalogs)) {
            return false;
        }
        for (DataDicCatalogEntity dataDicCatalogEntity : dataCatalogs) {
            Collection<DataDicCatalogEntity> childrenCatalog = dataDicCatalogRepository.findAllByParentCatalogCode(
                dataDicCatalogEntity.getCatalogCode(),
                null,
                null);
            if (CollectionUtil.isNotEmpty(childrenCatalog)) {
                throw new ErrMsgException(I18NUtil.getMessage("pep.sys.datadic.catalog.del.relevanceChildren.error"));
            }
            Collection datadics = dataDicService.findByCatalog(dataDicCatalogEntity.getCatalogCode());
            if (CollectionUtil.isNotEmpty(datadics)) {
                throw new ErrMsgException(I18NUtil.getMessage("pep.sys.datadic.catalog.del.relevance.error"));
            }
        }
        dataDicCatalogRepository.deleteAll(dataCatalogs);
        return true;
    }

    private void validUnique(DataDicCatalogVO dataDicCatalogVO) {
        DataDicCatalogEntity dataDicCatalogEntity = dataDicCatalogRepository.findByCatalogCode(dataDicCatalogVO.getCatalogCode());
        if (null == dataDicCatalogEntity || dataDicCatalogEntity.getId().equals(dataDicCatalogVO.getId())) {
            return;
        }
        throw new ErrMsgException(I18NUtil.getMessage("pep.sys.datadic.catalog.code.unique"));
    }
}
