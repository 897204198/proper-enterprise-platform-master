package com.proper.enterprise.platform.workflow.util;

import com.proper.enterprise.platform.core.utils.DateUtil;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

public class VariableUtil {

    VariableUtil() {
    }

    public static Map<String, Object> handleVariableDateType(Map<String, Object> variables) {
        if (MapUtils.isEmpty(variables)) {
            return variables;
        }
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            if (null == entry.getValue()) {
                continue;
            }
            if (!(entry.getValue() instanceof String)) {
                continue;
            }
            if (DateUtil.isDate((String) entry.getValue())) {
                variables.put(entry.getKey(), DateUtil.parseSpecial((String) entry.getValue()));
            }
        }
        return variables;
    }
}
