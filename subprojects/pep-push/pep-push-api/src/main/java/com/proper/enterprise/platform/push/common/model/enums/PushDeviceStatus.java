package com.proper.enterprise.platform.push.common.model.enums;

public enum PushDeviceStatus {
    /**
     * deviceid有效，正在使用此设备
     */
    VALID,
    /**
     * deviceid失效了,不能向此设备发送消息了（在ios设备上，同一个设备的deviceid可能会失效，需要更新为新的deviceid才能使用）
     */
    INVALID

}
