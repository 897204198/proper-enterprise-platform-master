package com.proper.enterprise.platform.workflow.handler;

import com.proper.enterprise.platform.core.utils.DateUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("variableTimeHandler")
public class VariableTimeHandler {

    public String toString(Long date, String format) {
        return DateUtil.toString(new Date(date), format);
    }
}
