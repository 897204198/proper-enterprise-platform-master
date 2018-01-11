package com.proper.enterprise.platform.common.pay.utils;

import com.proper.enterprise.platform.core.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 支付工具类
 */
public class PayUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayUtils.class);

    /**
     * 分转元
     *
     * @param fenValue 分
     * @return 转换后的元
     */
    public static String convertMoneyFen2Yuan(String fenValue) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(new BigDecimal(String.valueOf(fenValue)).divide(new BigDecimal("100")));
    }

    /**
     * 元转分
     *
     * @param yuanValue 元
     * @return 转换后的分
     */
    public static String convertMoneyYuan2Fen(String yuanValue) {
        BigDecimal fee = new BigDecimal(yuanValue);
        return String.valueOf(fee.multiply(new BigDecimal("100")).intValue());
    }

    /**
     * 输出BeanLog
     *
     * @param t 泛型对象
     * @param <T> 泛型
     */
    public static <T> void logEntity(T t) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(JSONUtil.toJSONIgnoreException(t));
        }
    }
}
