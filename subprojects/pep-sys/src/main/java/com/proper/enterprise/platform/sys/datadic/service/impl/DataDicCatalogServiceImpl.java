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
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

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
    public DataDicCatalogVO get(String id) {
        return BeanUtil.convert(dataDicCatalogRepository.findOne(id), DataDicCatalogVO.class);
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
        return BeanUtil.convert(dataDicCatalogRepository.save(
            BeanUtil.convert(dataDicCatalogVO, DataDicCatalogEntity.class)), DataDicCatalogVO.class);
    }

    @Override
    public DataDicCatalogVO update(DataDicCatalogVO dataDicCatalogVO) {
        validUnique(dataDicCatalogVO);
        DataDicCatalogEntity oldCatalog = dataDicCatalogRepository.findOne(dataDicCatalogVO.getId());
        String oldCatalogCode = oldCatalog.getCatalogCode();
        DataDicCatalogEntity dataDicCatalogEntity = dataDicCatalogRepository.updateForSelective(
            BeanUtil.convert(dataDicCatalogVO, DataDicCatalogEntity.class));
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
        Collection<DataDicCatalogEntity> dataCatalogs = dataDicCatalogRepository.findAll(Arrays.asList(idArr));
        if (CollectionUtil.isEmpty(dataCatalogs)) {
            return false;
        }
        for (DataDicCatalogEntity dataDicCatalogEntity : dataCatalogs) {
            Collection datadics = dataDicService.findByCatalog(dataDicCatalogEntity.getCatalogCode());
            if (CollectionUtil.isNotEmpty(datadics)) {
                throw new ErrMsgException(I18NUtil.getMessage("pep.sys.datadic.catalog.del.relevance.error"));
            }
        }
        dataDicCatalogRepository.delete(dataCatalogs);
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
