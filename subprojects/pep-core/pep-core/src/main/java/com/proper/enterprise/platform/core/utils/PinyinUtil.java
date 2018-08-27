package com.proper.enterprise.platform.core.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 汉字串转换拼音串
 */
public class PinyinUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PinyinUtil.class);

    private static HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private PinyinUtil() { }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new PinyinUtil();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    /**
     * 转换一个字符串
     * @param str str
     * @return 字符串
     */
    public static String quanpin(String str) {
        if (StringUtil.isNull(str)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String tempPinyin;
        for (int i = 0; i < str.length(); ++i) {
            tempPinyin = getCharacterPinYin(str.charAt(i));
            if (tempPinyin == null) {
                // 如果str.charAt(i)非汉字，则保持原样
                sb.append(str.charAt(i));
            } else {
                sb.append(tempPinyin);
            }
        }
        return sb.toString();
    }

    /**
     * 转换单个字符
     * @param c c
     * @return 字符串
     */
    private static String getCharacterPinYin(char c) {
        String[] pinyin = null;
        try {
            pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            LOGGER.debug("PinyinUtil.getCharacterPinYin[Exception]", e);
        }
        // 如果c不是汉字，toHanyuPinyinStringArray会返回null
        if (pinyin == null) {
            return null;
        }
        // 只取一个发音，如果是多音字，仅取第一个发音
        return pinyin[0];
    }

}
