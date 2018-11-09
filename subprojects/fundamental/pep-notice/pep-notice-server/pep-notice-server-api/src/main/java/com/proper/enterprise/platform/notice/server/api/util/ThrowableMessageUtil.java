package com.proper.enterprise.platform.notice.server.api.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ThrowableMessageUtil {

    /**
     * 获取堆栈信息
     *
     * @param throwable 异常
     * @return 堆栈信息
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }
}
