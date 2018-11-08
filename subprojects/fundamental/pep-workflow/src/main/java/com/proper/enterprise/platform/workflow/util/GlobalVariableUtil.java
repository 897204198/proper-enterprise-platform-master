package com.proper.enterprise.platform.workflow.util;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import org.apache.commons.collections.MapUtils;

import java.util.List;
import java.util.Map;

public class GlobalVariableUtil {
    GlobalVariableUtil() {
    }

    public static Map<String, Object> setGlobalVariable(Map<String, Object> globalVariables,
                                                        Map<String, Object> formVariables,
                                                        List<String> globalVariableKeys) {
        if (MapUtils.isEmpty(formVariables)) {
            return globalVariables;
        }
        if (CollectionUtil.isEmpty(globalVariableKeys)) {
            return globalVariables;
        }
        for (String globalVariableKey : globalVariableKeys) {
            if (null != formVariables.get(globalVariableKey)) {
                globalVariables.put(globalVariableKey, formVariables.get(globalVariableKey));
            }
        }
        return globalVariables;
    }

}
