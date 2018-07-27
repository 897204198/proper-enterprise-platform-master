package com.proper.enterprise.platform.push.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.jpa.service.BaseJpaService;
import com.proper.enterprise.platform.push.api.PushMsg;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;

public interface PushMsgService extends BaseJpaService<PushMsg, String> {
    /**
     * 根据查询条件获取推送消息集合
     *
     * @param example  查询条件
     * @param pageable 分页
     * @return List 推送消息集合
     */
    DataTrunk<? extends PushMsg> findByDateTypeAndAppkey(Example example, Pageable pageable);
}
