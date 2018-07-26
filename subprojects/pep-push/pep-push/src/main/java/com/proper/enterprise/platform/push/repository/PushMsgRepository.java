package com.proper.enterprise.platform.push.repository;

import com.proper.enterprise.platform.core.jpa.repository.BaseJpaRepository;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;
import com.proper.enterprise.platform.push.common.model.enums.PushMsgStatus;
import com.proper.enterprise.platform.push.entity.PushDeviceEntity;
import com.proper.enterprise.platform.push.entity.PushMsgEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PushMsgRepository extends BaseJpaRepository<PushMsgEntity, String> {

    /**
     * 通过应用标识，消息id获取消息集合
     *
     * @param appkey    应用标识
     * @param lstMsgids 消息id
     * @return 消息集合
     */
    List<PushMsgEntity> findByAppkeyAndMsgidIn(String appkey, List<String> lstMsgids);

    /**
     * 通过应用标识，设备，用户id，消息状态获取消息集合
     *
     * @param appkey 应用标识
     * @param device 设备
     * @param uid    用户id
     * @param unsend 消息状态
     * @return 消息集合
     */
    List<PushMsgEntity> findByAppkeyAndDeviceAndUseridAndMstatus(String appkey, PushDeviceEntity device, String uid,
                                                                 PushMsgStatus unsend);

    /**
     * 通过应用标识，设备，用户id，消息状态获取消息集合
     *
     * @param appkey       应用标识
     * @param unsend       消息状态
     * @param maxSendCount 最大发送次数
     * @param pushMode     推送方式
     * @param pageable     分页信息
     * @return 消息集合
     */
    Page<PushMsgEntity> findByAppkeyAndMstatusAndSendCountLessThanEqualAndDevicePushMode(String appkey, PushMsgStatus unsend, int maxSendCount,
                                                                                         PushMode pushMode, Pageable pageable);

    /**
     * 通过应用标识，时间删除传入时间之前的消息
     *
     * @param appkey 应用标识
     * @param date2  时间
     * @return int
     */
    @Modifying
    @Query("delete from PushMsgEntity m where m.appkey=:appkey and  m.createTime <= :date2")
    public int deleteByAppkeyAndCreateTimeLessThan(@Param("appkey") String appkey, @Param("date2") String date2);

    /**
     * 通过消息最后修改时间获取消息统计信息
     *
     * @param mstartDate 最后修改时间段的开始
     * @param mendDate   最后修改时间段的结束
     * @return 消息集合
     */
    @Query("SELECT A.appkey, A.device, A.lastModifyTime, A.mstatus, COUNT(A.appkey) "
        + "FROM PushMsgEntity AS A "
        + "WHERE A.lastModifyTime>=:mstartDate AND A.lastModifyTime< :mendDate GROUP BY  A.appkey, A.device, A.lastModifyTime, A.mstatus")
    List findByLastModifyTimeByGroup(@Param("mstartDate") String mstartDate, @Param("mendDate") String mendDate);

}
