package com.proper.enterprise.platform.push.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.jpa.service.BaseJpaService;
import com.proper.enterprise.platform.push.api.PushMsg;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PushMsgService extends BaseJpaService<PushMsg, String> {
    /**
     * 根据查询条件获取推送消息集合
     *
     * @param example  查询条件
     * @param pageable 分页
     * @return List 推送消息集合
     */
    DataTrunk<? extends PushMsg> findByDateTypeAndAppkey(Example example, Pageable pageable);

    /**
     * 推送消息到指定用户，消息入库
     *
     * @param msgData 消息数据
     * @return List 推送消息ID的集合
     */
    List savePushMessageToUsers(Map<String, String> msgData);

    /**
     * 推送消息到一组设备上，这一组设备需要要相同的设备类型
     *
     * @param msgData 消息数据
     * @return List 推送消息ID的集合
     */
    List savePushMessageToDevices(Map<String, String> msgData);

    /**
     * 推送消息到全部用户
     *
     * @param msgData 消息数据
     * @return List 推送消息ID的集合
     * @throws IOException 集合转JSON异常
     */
    List savePushMessageToAllUsers(Map<String, String> msgData) throws IOException;

    /**
     * 推送消息到指定类型的全部设备
     *
     * @param msgData 消息数据
     * @return List 推送消息ID的集合
     * @throws IOException 集合转JSON异常
     */
    List savePushMessageToAllDevices(Map<String, String> msgData) throws IOException;
}
