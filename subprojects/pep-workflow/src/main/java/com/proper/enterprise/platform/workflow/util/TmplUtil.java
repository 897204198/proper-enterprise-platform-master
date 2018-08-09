package com.proper.enterprise.platform.workflow.util;

import com.proper.enterprise.platform.core.utils.StringUtil;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TmplUtil {

    private TmplUtil() {
    }

    /**
     * 模板解析
     *
     * @param tmpl      模板 ${***}编写参数
     * @param variables 参数集合
     * @return 拼接后的消息
     */
    public static String resolveTmpl(String tmpl, Map<String, Object> variables) {
        String re = "(?<=\\$\\{).*?(?=\\})";
        Pattern p = Pattern.compile(re);
        Matcher m = p.matcher(tmpl);
        String message = tmpl;
        while (m.find()) {
            String key = m.group();
            if (StringUtil.isEmpty(key)) {
                continue;
            }
            String value = (String) variables.get(key);
            message = message.replaceAll("\\$\\{" + key + "\\}", StringUtil.isEmpty(value) ? "" : value);
        }
        return message;
    }
}
