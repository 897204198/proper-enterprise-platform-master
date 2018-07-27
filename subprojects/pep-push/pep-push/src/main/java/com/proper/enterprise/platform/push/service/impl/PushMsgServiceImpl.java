package com.proper.enterprise.platform.push.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.jpa.service.impl.AbstractJpaServiceSupport;
import com.proper.enterprise.platform.push.api.PushMsg;
import com.proper.enterprise.platform.push.entity.PushMsgEntity;
import com.proper.enterprise.platform.push.repository.PushMsgRepository;
import com.proper.enterprise.platform.push.service.PushMsgService;
import com.proper.enterprise.platform.push.vo.PushMsgVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PushMsgServiceImpl extends AbstractJpaServiceSupport<PushMsg, PushMsgRepository, String>
    implements PushMsgService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushMsgServiceImpl.class);
    @Autowired
    private PushMsgRepository pushMsgRepository;

    public PushMsgRepository getRepository() {
        return pushMsgRepository;
    }

    @Override
    public DataTrunk<? extends PushMsg> findByDateTypeAndAppkey(Example example, Pageable pageable) {
        Page<PushMsgEntity> page = pushMsgRepository.findAll(example, pageable);
        DataTrunk<? extends PushMsg> trunk = new DataTrunk<>(convertVo(page), page.getTotalElements());
        return trunk;
    }

    private List<PushMsgVO> convertVo(Page<PushMsgEntity> page) {
        List<PushMsgEntity> entityList = page.getContent();
        List<PushMsgVO> voList = new ArrayList<PushMsgVO>();
        for (PushMsgEntity entity : entityList) {
            PushMsgVO vo = new PushMsgVO();
            vo.setAppkey(entity.getAppkey());
            vo.setLastPushTime(entity.getLastModifyTime());
            vo.setMcontent(entity.getMcontent());
            vo.setMstatus(entity.getMstatus());
            vo.setPushMode(entity.getPushMode());
            vo.setId(entity.getId());
            vo.setUserid(entity.getUserid());
            voList.add(vo);
        }
        return voList;
    }
}
