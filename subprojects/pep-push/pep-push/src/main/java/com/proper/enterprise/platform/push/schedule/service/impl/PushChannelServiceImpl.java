package com.proper.enterprise.platform.push.schedule.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.push.api.openapi.service.PushChannelService;
import com.proper.enterprise.platform.push.entity.PushChannelEntity;
import com.proper.enterprise.platform.push.repository.PushChannelRepository;
import com.proper.enterprise.platform.push.vo.PushChannelVO;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PushChannelServiceImpl implements PushChannelService {


    private PushChannelRepository pushChannelRepository;

    @Autowired
    public PushChannelServiceImpl(PushChannelRepository pushChannelRepository) {
        this.pushChannelRepository = pushChannelRepository;
    }

    @Override
    public PushChannelVO addChannel(PushChannelVO pushChannelVO) {
        checkVo(pushChannelVO);
        PushChannelEntity pushChannelEntity = pushChannelRepository.save(PushChannelVO.convertVoToEntity(pushChannelVO));
        return PushChannelVO.convertEntityToVo(pushChannelEntity);
    }

    @Override
    public PushChannelVO updateChannel(PushChannelVO pushChannelVO) {
        checkVo(pushChannelVO);
        PushChannelEntity pushChannelEntity = pushChannelRepository.findOne(pushChannelVO.getId());
        if (pushChannelEntity != null) {
            return PushChannelVO.convertEntityToVo(pushChannelRepository.save(pushChannelEntity));
        } else {
            throw new ErrMsgException(I18NUtil.getMessage("pep.push.update.fail"));
        }
    }

    @Override
    public boolean deleteChannel(String ids) {
        String[] split = ids.split(",");
        for (String id : split) {
            pushChannelRepository.delete(id);
        }
        return true;
    }

    @Override
    public DataTrunk<PushChannelVO> findAll() {
        DataTrunk<PushChannelVO> result = new DataTrunk<>();
        Iterable<PushChannelEntity> pushChannelEntities = pushChannelRepository.findAll();
        List<PushChannelVO> vos = new ArrayList<>();
        for (PushChannelEntity entity : pushChannelEntities) {
            vos.add(PushChannelVO.convertEntityToVo(entity));
        }
        result.setData(vos);
        result.setCount(vos.size());
        return result;
    }

    private void checkVo(PushChannelVO pushChannelVO) {
        if (PushChannelVO.checkEmpty(pushChannelVO)) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.push.formatting.error"));
        } else if (StringUtils.isEmpty(pushChannelVO.getId()) && checkChannelNameExist(pushChannelVO)) {
            throw new ErrMsgException(I18NUtil.getMessage("pep.push.channel.name.add.error"));
        }
    }

    private boolean checkChannelNameExist(PushChannelVO pushChannelVO) {
        PushChannelEntity byChannelName = pushChannelRepository.findByChannelName(pushChannelVO.getChannelName());
        return byChannelName != null;
    }

}
