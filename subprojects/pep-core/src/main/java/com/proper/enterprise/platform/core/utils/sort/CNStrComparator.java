package com.proper.enterprise.platform.core.utils.sort;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * 中文字符串比较器
 * 按拼音字母及声调排序，如：“汪” 会排在 “王” 前面
 */
public class CNStrComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        return Collator.getInstance(Locale.CHINESE).compare(o1, o2);
    }

}
