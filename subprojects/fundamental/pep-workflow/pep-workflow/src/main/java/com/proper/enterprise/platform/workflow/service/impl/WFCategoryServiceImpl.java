package com.proper.enterprise.platform.workflow.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.i18n.I18NUtil;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.workflow.entity.WFCategoryEntity;
import com.proper.enterprise.platform.workflow.repository.WFCategoryRepository;
import com.proper.enterprise.platform.workflow.service.WFCategoryService;
import com.proper.enterprise.platform.workflow.vo.WFCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class WFCategoryServiceImpl implements WFCategoryService {

    @Autowired
    private WFCategoryRepository wfCategoryRepository;

    private static final String DEFAULT_VALUE = "-1";

    @Override
    public WFCategoryVO save(WFCategoryVO wfCategoryVO) {
        valid(wfCategoryVO);
        WFCategoryEntity wfCategoryEntity = BeanUtil.convert(wfCategoryVO, WFCategoryEntity.class);
        String parentId = wfCategoryVO.getParentId();
        if (StringUtil.isNotNull(parentId) && !DEFAULT_VALUE.equals(parentId)) {
            wfCategoryEntity.addParent(wfCategoryRepository.findById(parentId).orElse(null));
        }
        return BeanUtil.convert(wfCategoryRepository.save(wfCategoryEntity), WFCategoryVO.class);
    }

    @Override
    public boolean deleteByIds(String ids) {
        if (StringUtil.isEmpty(ids)) {
            return false;
        }
        String[] idAttr = ids.split("\\,");
        for (String id : idAttr) {
            WFCategoryEntity parent = new WFCategoryEntity();
            parent.setId(id);
            if (wfCategoryRepository.findAll(null, null, parent).size() > 0) {
                throw new ErrMsgException(I18NUtil.getMessage("workflow.category.delete.relation.failed"));
            }
            wfCategoryRepository.delete(id);
        }
        return true;
    }

    @Override
    public WFCategoryVO update(WFCategoryVO wfCategoryVO) {
        valid(wfCategoryVO);
        WFCategoryEntity wfCategoryEntity = BeanUtil.convert(wfCategoryVO, WFCategoryEntity.class);
        String parentId = wfCategoryVO.getParentId();
        if (StringUtil.isNotNull(parentId) && !DEFAULT_VALUE.equals(parentId)) {
            wfCategoryEntity.addParent(wfCategoryRepository.findById(parentId).orElse(null));
        }
        return BeanUtil.convert(wfCategoryRepository.updateForSelective(wfCategoryEntity), WFCategoryVO.class);
    }

    @Override
    public Collection<WFCategoryVO> findAll(String name, String code, WFCategoryVO parent) {
        WFCategoryEntity parentEntity = BeanUtil.convert(parent, WFCategoryEntity.class);
        return BeanUtil.convert(wfCategoryRepository.findAll(name, code, parentEntity), WFCategoryVO.class);
    }

    @Override
    public DataTrunk<WFCategoryVO> findAll(String name, String code, WFCategoryVO parent, Pageable pageable) {
        WFCategoryEntity parentEntity = BeanUtil.convert(parent, WFCategoryEntity.class);
        return BeanUtil.convert(wfCategoryRepository.findAll(name, code, parentEntity, pageable), WFCategoryVO.class);
    }


    @Override
    public WFCategoryVO get(String id) {
        return BeanUtil.convert(wfCategoryRepository.findById(id).orElse(null), WFCategoryVO.class);
    }

    @Override
    public WFCategoryVO getByCode(String code) {
        return BeanUtil.convert(wfCategoryRepository.findByCode(code).orElse(null), WFCategoryVO.class);
    }

    private void valid(WFCategoryVO wfCategoryVO) {
        WFCategoryEntity wfCategoryEntity = wfCategoryRepository.findByCode(wfCategoryVO.getCode()).orElse(null);
        if (wfCategoryEntity != null && !wfCategoryEntity.getId().equals(wfCategoryVO.getId())) {
            throw new ErrMsgException(I18NUtil.getMessage("workflow.category.code.unique"));
        }
        wfCategoryEntity = wfCategoryRepository.findByName(wfCategoryVO.getName()).orElse(null);
        if (wfCategoryEntity != null && !wfCategoryEntity.getId().equals(wfCategoryVO.getId())) {
            throw new ErrMsgException(I18NUtil.getMessage("workflow.category.name.unique"));
        }
    }
}
