package com.proper.enterprise.platform.oopsearch.config.conf;

public class ModuleSearchConfig extends AbstractSearchConfigs {

    /**
     * 默认构造函数
     *
     * @param searchTables  查询使用的表
     * @param searchColumns 查询使用的字段
     * @param limit         返回结果最大条数
     * @param extendByYear  对于年份，输入内容，可以被识别的内容
     * @param extendByMonth 对于月份，输入内容，可以被识别的内容
     * @param extendByDay   对于天，输入内容，可以被识别的内容
     */
    public ModuleSearchConfig(String searchTables, String searchColumns, int limit, String extendByYear, String extendByMonth, String extendByDay) {
        super(searchTables, searchColumns, limit, extendByYear, extendByMonth, extendByDay);
    }
}
