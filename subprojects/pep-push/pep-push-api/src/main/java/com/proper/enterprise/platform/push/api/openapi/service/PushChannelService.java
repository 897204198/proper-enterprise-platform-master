package com.proper.enterprise.platform.push.api.openapi.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.push.vo.PushChannelVO;

public interface PushChannelService {

    /**
     * 增加渠道
     *
     * @param pushChannelVO PushChannelVo
     * @return PushChannelVo
     */
    PushChannelVO addChannel(PushChannelVO pushChannelVO);

    /**
     * 修改渠道
     *
     * @param pushChannelVO PushChannelVo
     * @return PushChannelVo
     */
    PushChannelVO updateChannel(PushChannelVO pushChannelVO);

    /**
     * 删除渠道
     *
     * @param ids ids
     * @return boolean
     */
    boolean deleteChannel(String ids);

    /**
     * 查询渠道
     *
     * @return DataTrunk
     */
    DataTrunk<PushChannelVO> findAll();

    /**
     * 查询可用的渠道
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 数据
     */
    DataTrunk<PushChannelVO> findByEnable(String startDate, String endDate);

}
