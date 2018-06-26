package com.proper.enterprise.platform.push.repository;

import java.util.Collection;
import java.util.List;

import com.proper.enterprise.platform.core.repository.BaseRepository;
import com.proper.enterprise.platform.push.entity.PushDeviceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.proper.enterprise.platform.push.common.model.enums.PushDeviceStatus;
import com.proper.enterprise.platform.push.common.model.enums.PushDeviceType;
import com.proper.enterprise.platform.push.common.model.enums.PushMode;

public interface PushDeviceRepository extends BaseRepository<PushDeviceEntity, String> {

    /**
     * 通过应用标识和设备id查找设备信息
     *
     * @param appkey 应用标识
     * @param deviceid 设备id
     * @return PushDevice
     */
    PushDeviceEntity findByAppkeyAndDeviceid(String appkey, String deviceid);

    /**
     * 通过应用标识，设备类型，设备有效状态获取设备集合
     *
     * @param appkey 应用标识
     * @param devicetype 设备类型
     * @param mstatus 设备有效状态
     * @param pageable 分页信息
     * @return 设备信息集合
     */
    Page<PushDeviceEntity> findByAppkeyAndDevicetypeAndMstatus(String appkey, PushDeviceType devicetype,
                                                               PushDeviceStatus mstatus, Pageable pageable);

    /**
     * 通过应用标识，设备类型，推送方式，设备有效状态，用户id获取设备集合
     *
     * @param appkey 应用标识
     * @param devicetype 设备类型
     * @param pushMode 推送方式
     * @param mstatus 设备有效状态
     * @param lstUids 用户id集合
     * @return 设备信息集合
     */
    List<PushDeviceEntity> findByAppkeyAndDevicetypeAndPushModeAndMstatusAndUseridIn(String appkey, PushDeviceType devicetype, PushMode pushMode,
                                                                                     PushDeviceStatus mstatus, Collection<String> lstUids);

    /**
     * 通过应用标识，设备类型，推送方式，设备有效状态，设备id获取设备集合
     *
     * @param appkey 应用标识
     * @param deviceType 设备类型
     * @param pushMode 推送方式
     * @param valid 设备有效状态
     * @param lstDeviceids 设备id集合
     * @return 设备信息集合
     */
    List<PushDeviceEntity> findByAppkeyAndDevicetypeAndPushModeAndMstatusAndDeviceidIn(String appkey, PushDeviceType deviceType, PushMode pushMode,
                                                                                       PushDeviceStatus valid, List<String> lstDeviceids);

    /**
     * 通过应用标识，用户id获取设备集合
     *
     * @param appkey 应用标识
     * @param userid 用户id
     * @return 设备信息集合
     */
    List<PushDeviceEntity> findByAppkeyAndUserid(String appkey, String userid);
}
