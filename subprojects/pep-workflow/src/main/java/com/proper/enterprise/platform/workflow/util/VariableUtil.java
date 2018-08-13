package com.proper.enterprise.platform.workflow.util;

import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.sys.datadic.DataDicLiteBean;
import org.apache.commons.collections.MapUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableUtil {

    VariableUtil() {
    }

    public static Map<String, Object> handleVariableSpecialType(Map<String, Object> variables) {
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
                variables.put(entry.getKey(), DateUtil.parseGMTSpecial((String) entry.getValue()));
                continue;
            }
            if (isDataDic((String) entry.getValue())) {
                variables.put(entry.getKey(), parseToDataDic((String) entry.getValue()));
            }
        }
        return variables;
    }

    public static Map<String, String> convertVariableToMsgParam(Map<String, Object> globalVariables) {
        Map<String, String> msgParam = new HashMap<>(16);
        if (CollectionUtil.isEmpty(globalVariables)) {
            return msgParam;
        }
        for (Map.Entry<String, Object> entry : globalVariables.entrySet()) {
            if (entry.getValue() instanceof String) {
                msgParam.put(entry.getKey(), (String) entry.getValue());
            }
            if (entry.getValue() instanceof Long && null != entry.getValue()) {
                msgParam.put(entry.getKey(), ((Long) entry.getValue()).toString());
            }
        }
        return msgParam;
    }

    private static boolean isDataDic(String dataDic) {
        if (StringUtil.isEmpty(dataDic)) {
            return false;
        }
        String re = "\\{\"catalog\":(.*),\"code\":(.*)\\}";
        Pattern p = Pattern.compile(re);
        Matcher m = p.matcher(dataDic);
        return m.find();
    }

    private static DataDicLiteBean parseToDataDic(String dataDic) {
        try {
            return JSONUtil.parse(dataDic, DataDicLiteBean.class);
        } catch (IOException e) {
            return null;
        }
    }
}
