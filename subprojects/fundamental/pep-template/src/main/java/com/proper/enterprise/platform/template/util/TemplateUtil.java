package com.proper.enterprise.platform.template.util;

import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateUtil.class);

    public static String template2Content(String tmpl, Map<String, Object> variables) {
        String re = "(?<=\\{).*?(?=\\})";
        Pattern p = Pattern.compile(re);
        if (StringUtil.isNull(tmpl)) {
            return "";
        }
        Matcher m = p.matcher(tmpl);
        String message = tmpl;
        while (m.find()) {
            String key = m.group();
            if (StringUtil.isEmpty(key)) {
                continue;
            }
            String value = paramValid(variables.get(key));
            message = message.replaceAll("\\{" + key + "\\}", value);
        }
        return message;
    }

    public static String paramValid(Object param) {
        String result;
        try {
            if (param instanceof Integer
                || param instanceof Double
                || param instanceof Float
                || param instanceof Long) {
                result = String.valueOf(param);
            } else if (param instanceof String) {
                result = (String) param;
            } else if (param instanceof Boolean) {
                boolean b = (boolean) param;
                result = b ? "true" : "false";
            } else if (param instanceof Date) {
                Date date = (Date) param;
                result = DateUtil.toString(DateUtil.toLocalDateTime(date), "yyyy-MM-dd");
            } else {
                result = null;
            }
        } catch (Exception ex) {
            LOGGER.error("TemplateUtil.paramValid:{}", ex);
            result = null;
        }
        return StringUtil.isEmpty(result) ? "" : result;
    }

}
