package com.proper.enterprise.platform.workflow.handler;

import com.proper.enterprise.platform.core.utils.DateUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("variableTimeHandler")
@Deprecated
public class VariableTimeHandler {

    /**
     * 流程图转换时间格式方法(弃用) 统一时间格式为 long
     *
     * @param dateTime   时间(long类型)
     * @param format 转换格式
     * @return 转换后的时间 String
     */
    @Deprecated
    public String toString(Long dateTime, String format) {
        return DateUtil.toString(new Date(dateTime), format);
    }

    /**
     * 流程图转换时间格式方法
     *
     * @param dateTime 时间(long类型)
     * @param format   转换格式
     * @return 转换后的时间 String
     */
    public String longToString(Long dateTime, String format) {
        return DateUtil.toString(new Date(dateTime), format);
    }
}
