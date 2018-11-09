package com.proper.enterprise.platform.notice.server.push.dao.service;

import com.proper.enterprise.platform.notice.server.push.vo.PushConfigVO;

public interface PushNoticeConfigService {

    /**
     * 保存推送配置
     *
     * @param appKey       应用唯一标识
     * @param pushConfigVO 配置VO
     */
    void save(String appKey, PushConfigVO pushConfigVO);

    /**
     * 清空配置
     *
     * @param appKey 应用唯一标识
     */
    void delete(String appKey);

    /**
     * 更新推送配置
     *
     * @param appKey       应用唯一标识
     * @param pushConfigVO 配置VO
     */
    void update(String appKey, PushConfigVO pushConfigVO);

    /**
     * 获取推送配置
     *
     * @param appKey 应用唯一标识
     * @return 配置VO
     */
    PushConfigVO get(String appKey);
}
