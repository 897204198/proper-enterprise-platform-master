package com.proper.enterprise.platform.sequence.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sequence.entity.SequenceEntity;
import com.proper.enterprise.platform.sequence.util.SerialNumberUtil;
import com.proper.enterprise.platform.sequence.vo.SequenceVO;
import com.proper.enterprise.platform.sys.i18n.I18NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.proper.enterprise.platform.sequence.repository.SequenceRepository;
import com.proper.enterprise.platform.sequence.service.SequenceService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class SequenceServiceImpl implements SequenceService {

    private SequenceRepository sequenceRepository;

    private I18NService i18NService;

    @Autowired
    public SequenceServiceImpl(SequenceRepository sequenceRepository, I18NService i18NService) {
        this.sequenceRepository = sequenceRepository;
        this.i18NService = i18NService;
    }

    @Override
    public SequenceVO save(SequenceVO sequenceVO) {
        SequenceEntity entity = BeanUtil.convert(sequenceVO, SequenceEntity.class);
        sequenceVO = BeanUtil.convert(sequenceRepository.save(entity), SequenceVO.class);
        sequenceVO.setInitialValue(SerialNumberUtil.getCurrentSerialNumber(sequenceVO.getSequenceCode()));
        return sequenceVO;
    }

    @Override
    public boolean deleteByIds(String ids) {
        if (StringUtil.isNotNull(ids)) {
            String[] idArr = ids.split(",");
            List<String> idList = new ArrayList<>();
            Collections.addAll(idList, idArr);
            Collection<SequenceEntity> list = sequenceRepository.findAllById(idList);
            sequenceRepository.deleteAll(list);
            return list.size() > 0;
        }
        return false;
    }

    @Override
    public SequenceVO update(String id, SequenceVO sequenceVO) {
        if (StringUtil.isNotBlank(id)) {
            sequenceVO.setId(id);
            SequenceEntity entity = BeanUtil.convert(sequenceVO, SequenceEntity.class);
            SerialNumberUtil.setCurrentSerialNumber(sequenceVO.getSequenceCode(), sequenceVO.getInitialValue());
            sequenceVO = BeanUtil.convert(sequenceRepository.updateForSelective(entity), SequenceVO.class);
            sequenceVO.setInitialValue(SerialNumberUtil.getCurrentSerialNumber(sequenceVO.getSequenceCode()));
            return sequenceVO;
        }
        throw new ErrMsgException(i18NService.getMessage("sequence.update.error"));
    }

    @Override
    public Collection<SequenceVO> findAll(String sequenceCode) {
        Collection<SequenceVO> sequenceVOS = BeanUtil.convert(sequenceRepository.findAll(sequenceCode), SequenceVO.class);
        for (SequenceVO sequenceVO : sequenceVOS) {
            sequenceVO.setInitialValue(SerialNumberUtil.getCurrentSerialNumber(sequenceVO.getSequenceCode()));
        }
        return sequenceVOS;
    }

    @Override
    public DataTrunk<SequenceVO> findAll(String sequenceCode, Pageable pageable) {
        DataTrunk<SequenceVO> dataTrunk = BeanUtil.convert(sequenceRepository.findAll(sequenceCode, pageable), SequenceVO.class);
        for (SequenceVO sequenceVO : dataTrunk.getData()) {
            sequenceVO.setInitialValue(SerialNumberUtil.getCurrentSerialNumber(sequenceVO.getSequenceCode()));
        }
        return dataTrunk;
    }

    @Override
    public SequenceVO findBySequenceCode(String sequenceCode) {
        return BeanUtil.convert(sequenceRepository.findBySequenceCode(sequenceCode), SequenceVO.class);
    }

}
