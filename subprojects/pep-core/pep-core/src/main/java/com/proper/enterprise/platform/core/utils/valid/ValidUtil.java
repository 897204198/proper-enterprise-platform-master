package com.proper.enterprise.platform.core.utils.valid;

import com.proper.enterprise.platform.core.utils.StringUtil;

import java.util.regex.Pattern;

/**
 * 验证工具类
 * <p>
 * 验证输入内容是否符合格式
 */
public class ValidUtil {

    private ValidUtil() {

    }

    private static Pattern emailPattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    /**
     * 校验是否为email
     *
     * @param email email
     * @return true false
     */
    public static boolean isEmail(String email) {
        if (StringUtil.isEmpty(email)) {
            return false;
        }
        return emailPattern.matcher(email).matches();
    }
}
