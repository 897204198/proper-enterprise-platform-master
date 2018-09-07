package com.proper.enterprise.platform.push.schedule.service.impl;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.push.api.openapi.service.PushChannelService;
import com.proper.enterprise.platform.push.entity.PushChannelEntity;
import com.proper.enterprise.platform.push.repository.PushChannelRepository;
import com.proper.enterprise.platform.push.repository.PushMsgStatisticRepository;
import com.proper.enterprise.platform.push.vo.PushChannelVO;
import com.proper.enterprise.platform.sys.i18n.I18NUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PushChannelServiceImpl implements PushChannelService {


    private PushChannelRepository pushChannelRepository;
    @Autowired
    private PushMsgStatisticRepository pushMsgStatisticRepository;

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
            return PushChannelVO.convertEntityToVo(pushChannelRepository.updateForSelective(PushChannelVO.convertVoToEntity(pushChannelVO)));
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
        Date msendDate = DateUtil.addDay(new Date(), -7);
        String dateStr = DateUtil.toString(msendDate, PEPConstants.DEFAULT_DATE_FORMAT);
        Date endtDate = new Date();
        String endDateStr = DateUtil.toString(endtDate, PEPConstants.DEFAULT_DATE_FORMAT);
        Iterable<PushChannelEntity> pushChannelEntities = pushChannelRepository.findByEnable(true);
        List<PushChannelVO> vos = new ArrayList<>();
        for (PushChannelEntity entity : pushChannelEntities) {
            String channelCount = pushMsgStatisticRepository.findPushCount(entity.getChannelName(), dateStr, endDateStr);

            if (StringUtil.isNull(channelCount)) {
                channelCount = "0";
            }
            vos.add(PushChannelVO.convertEntityToVo(entity, channelCount));
        }
        Collections.sort(vos, new Comparator<PushChannelVO>() {
            @Override
            public int compare(PushChannelVO o1, PushChannelVO o2) {
                if (Integer.parseInt(o1.getChannelCount()) > Integer.parseInt(o2.getChannelCount())) {
                    return -1;
                }
                return 1;
            }
        });
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
