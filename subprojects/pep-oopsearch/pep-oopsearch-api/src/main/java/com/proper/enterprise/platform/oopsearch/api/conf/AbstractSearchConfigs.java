package com.proper.enterprise.platform.oopsearch.api.conf;

import com.proper.enterprise.platform.oopsearch.api.model.SearchColumnModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * oopsearch组件查询配置类父类
 * 每一个使用oopsearch组件的业务模块，需配置一个查询配置类，继承该类
 * */
public abstract class AbstractSearchConfigs {

    // logger
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSearchConfigs.class);

    // 查询表名
    public String searchTables;

    // 查询字段
    public String searchColumns;

    // 查询最大条数
    public int limit;

    // 对于年份，输入内容，可以被识别的内容
    public String extendByYear;

    // 对于月份，输入内容，可以被识别的内容
    public String extendByMonth;

    // 对于天，输入内容，可以被识别的内容
    public String extendByDay;

    // 可识别年份文字描述数组
    public String[] extendByYearArr;

    // 可识别月份文字描述数组
    public String[] extendByMonthArr;

    // 可识别天文字描述数组
    public String[] extendByDayArr;

    // 表名数组
    public List<String> tableNameList;

    // 表名与查询字段对象对应关系集合
    public Map<String, List<SearchColumnModel>> searchTableColumnMap;

    public Map<String, Map<String, SearchColumnModel>> searchTableColumn;

    /**
     * 默认构造函数
     * @param  searchTables 查询使用的表
     * @param  searchColumns 查询使用的字段
     * @param  limit 返回结果最大条数
     * @param  extendByYear 对于年份，输入内容，可以被识别的内容
     * @param  extendByMonth 对于月份，输入内容，可以被识别的内容
     * @param  extendByDay 对于天，输入内容，可以被识别的内容
     * */
    public AbstractSearchConfigs(String searchTables,
                                 String searchColumns,
                                 int limit,
                                 String extendByYear,
                                 String extendByMonth,
                                 String extendByDay) {
        this.searchTables = searchTables.toLowerCase();
        this.searchColumns = searchColumns.toLowerCase();
        this.limit = limit;
        this.extendByYear = extendByYear;
        this.extendByMonth = extendByMonth;
        this.extendByDay = extendByDay;
        searchTableColumnMap = new HashMap<>();
        searchTableColumn = new HashMap<>();
        init();
    }

    /**
     * 初始化各变量
     * */
    public void init() {
        String[] tableNameArr = searchTables.split(",");
        tableNameList = Arrays.asList(tableNameArr);

        String[] columnArr = searchColumns.split(",");
        for (int i = 0; i < tableNameArr.length; i++) {
            String tableName = tableNameArr[i];
            List<SearchColumnModel> columnList = new ArrayList<>();
            Map<String, SearchColumnModel> columnMap = new HashMap<>();
            for (int j = 0; j < columnArr.length; j++) {
                String[] searchColumnArr = columnArr[j].split(":");
                if (searchColumnArr[0].equalsIgnoreCase(tableName)) {
                    SearchColumnModel searchColumn = new SearchColumnModel();
                    searchColumn.setTable(searchColumnArr[0]);
                    searchColumn.setColumn(searchColumnArr[1]);
                    searchColumn.setType(searchColumnArr[2]);
                    searchColumn.setDescColumn(searchColumnArr[3]);
                    columnList.add(searchColumn);
                    columnMap.put(searchColumn.getColumn(), searchColumn);
                }
            }
            searchTableColumnMap.put(tableName, columnList);
            searchTableColumn.put(tableName, columnMap);
        }

        extendByYearArr = extendByYear.split(",");
        extendByMonthArr = extendByMonth.split(",");
        extendByDayArr = extendByDay.split(",");
    }

    /**
     * 根据字段的类型（string、date、int）获取对应的字段集合
     * @param  type 字段类型（string、date、int）
     *
     * @return 查询字段集合
     * */
    public List<SearchColumnModel> getSearchColumnListByType(String type) {
        List<SearchColumnModel> searchColumnList = new ArrayList<>();
        List<String> tableNameList = this.getTableNameList();
        for (String tableName: tableNameList) {
            List<SearchColumnModel> searchColumnTempList = searchTableColumnMap.get(tableName);
            for (SearchColumnModel searchColumn: searchColumnTempList) {
                if (type.equalsIgnoreCase(searchColumn.getType())) {
                    searchColumnList.add(searchColumn);
                }
            }
        }
        return searchColumnList;
    }

    /**
     * 获取查询字段与表名对应的集合(获取的是searchTableColumnMap变量的拷贝)
     *
     * @return 查询字段对象与表名对应的集合
     * */
    public Map<String, List<SearchColumnModel>> getSearchTableColumnMap() {
        Map<String, List<SearchColumnModel>> copySearchTableColumnMap = new HashMap<>();
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(byteOut);
            out.writeObject(searchTableColumnMap);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream inputStream = new ObjectInputStream(byteIn);
            copySearchTableColumnMap = (Map<String, List<SearchColumnModel>>) inputStream.readObject();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return copySearchTableColumnMap;
    }

    public Map<String, Map<String, SearchColumnModel>> getSearchTableColumn() {
        return searchTableColumn;
    }

    /**
     * 获取查询使用的表名集合
     *
     * @return 表名集合
     * */
    public List<String> getTableNameList() {
        return tableNameList;
    }

    /**
     * 通过表名获取查询字段对象
     *
     * @return 查询字段对象的集合
     * */
    public List<SearchColumnModel> getSearchColumnListByTable(String tableName) {
        return searchTableColumnMap.get(tableName.toLowerCase());
    }

    /**
     * 通过表名获取查询字段对象
     *
     * @return 查询字段对象的映射
     * */
    public Map<String, SearchColumnModel> getSearchColumnMapByTable(String tableName) {
        return searchTableColumn.get(tableName.toLowerCase());
    }

    /**
     * 获取查询最大条数
     *
     * @return 最大条数
     * */
    public int getLimit() {
        return limit;
    }


    /**
     * 获取对于年份，输入内容，可以被识别的内容数组
     *
     * @return 年份内容识别数组
     * */
    public final String[] getExtendByYearArr() {
        if (extendByYearArr != null) {
            return extendByYearArr.clone();
        } else {
            return null;
        }
    }

    /**
     * 获取对于月份，输入内容，可以被识别的内容数组
     *
     * @return 月份内容识别数组
     * */
    public String[] getExtendByMonthArr() {
        if (extendByMonthArr != null) {
            return extendByMonthArr.clone();
        } else {
            return null;
        }
    }

    /**
     * 获取对于天，输入内容，可以被识别的内容数组
     *
     * @return 天内容识别数组
     * */
    public String[] getExtendByDayArr() {
        if (extendByDayArr != null) {
            return extendByDayArr.clone();
        } else {
            return null;
        }
    }
}
