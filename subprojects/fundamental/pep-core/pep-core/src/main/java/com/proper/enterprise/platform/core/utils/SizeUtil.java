package com.proper.enterprise.platform.core.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SizeUtil {

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private SizeUtil() {
    }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new SizeUtil();
    }

    /**
     * 将字节转换为KB,MB,GB单位
     *
     * @param size 字节数
     * @return 转换为KB,MB,GB的单位数
     */
    public static String parseSize(Long size) {
        Long carryOver = 1024L;
        double bigDecimal;
        if (size == null) {
            size = 0L;
        }
        if (size < carryOver) {
            return
                new BigDecimal(Double.toString(size / 1024.000)).setScale(2, RoundingMode.HALF_UP) + " KB";
        } else {
            bigDecimal = new BigDecimal(Double.toString(size / 1024.000)).setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        if (bigDecimal < carryOver) {
            return
                new BigDecimal(Double.toString(bigDecimal)).setScale(2, RoundingMode.HALF_UP) + " KB";
        } else {
            bigDecimal = new BigDecimal(Double.toString(bigDecimal / 1024.000)).setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        if (bigDecimal < carryOver) {
            return
                new BigDecimal(Double.toString(bigDecimal)).setScale(2, RoundingMode.HALF_UP) + " MB";
        } else {
            return
                new BigDecimal(Double.toString(bigDecimal / 1024.000)).setScale(2, RoundingMode.HALF_UP) + " GB";
        }
    }
}
