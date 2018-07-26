package com.proper.enterprise.platform.push.common.schedule.service;

/**
 * 每日定时任务：统计前一天的推送数据
 *
 * @author guozhimin
 */
public interface PushStatisticTaskService {

    /**
     * 统计前一天的推送数据
     *
     * @throws Exception 系统异常
     */
    public void saveYesterdayPushStatistic() throws Exception;
}
