package com.proper.enterprise.platform.search.api.conf;

import com.proper.enterprise.platform.search.api.model.SearchColumn;

import java.util.*;

public abstract class AbstractSearchConfigs {

    public String searchTables;

    public String searchColumns;

    public int limit;

    public String extendByYear;

    public String extendByMonth;

    public String extendByDay;

    public String[] extendByYearArr;

    public String[] extendByMonthArr;

    public String[] extendByDayArr;

    public List<String> tableNameList;

    public Map<String, List<SearchColumn>> searchTableColumnMap;

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
        init();
    }

    public void init() {
        String[] tableNameArr = searchTables.split(",");
        tableNameList = Arrays.asList(tableNameArr);

        String[] columnArr = searchColumns.split(",");
        for (int i = 0; i < tableNameArr.length; i++) {
            String tableName = tableNameArr[i];
            List<SearchColumn> columnList = new ArrayList<>();
            for (int j = 0; j < columnArr.length; j++) {
                String[] searchColumnArr = columnArr[j].split(":");
                if (searchColumnArr[0].equalsIgnoreCase(tableName)) {
                    SearchColumn searchColumn = new SearchColumn();
                    searchColumn.setTable(searchColumnArr[0]);
                    searchColumn.setColumn(searchColumnArr[1]);
                    searchColumn.setType(searchColumnArr[2]);
                    searchColumn.setDescColumn(searchColumnArr[3]);

                    columnList.add(searchColumn);
                }
            }
            searchTableColumnMap.put(tableName, columnList);
        }

        extendByYearArr = extendByYear.split(",");
        extendByMonthArr = extendByMonth.split(",");
        extendByDayArr = extendByDay.split(",");
    }

    public List<SearchColumn> getSearchColumnListByType(String type) {
        List<SearchColumn> searchColumnList = new ArrayList<>();
        List<String> tableNameList = this.getTableNameList();
        for (String tableName: tableNameList) {
            List<SearchColumn> searchColumnTempList = searchTableColumnMap.get(tableName);
            for (SearchColumn searchColumn: searchColumnTempList) {
                if (type.equalsIgnoreCase(searchColumn.getType())) {
                    searchColumnList.add(searchColumn);
                }
            }
        }
        return searchColumnList;
    }

    public Map<String, List<SearchColumn>> getSearchTableColumnMap() {
        return searchTableColumnMap;
    }

    public List<String> getTableNameList() {
        return tableNameList;
    }

    public List<SearchColumn> getSearchColumnListByTable(String tableName) {
        return searchTableColumnMap.get(tableName.toLowerCase());
    }

    public int getLimit() {
        return limit;
    }


    public final String[] getExtendByYearArr() {
        if (extendByYearArr != null) {
            return extendByYearArr.clone();
        } else {
            return null;
        }
    }

    public String[] getExtendByMonthArr() {
        if (extendByMonthArr != null) {
            return extendByMonthArr.clone();
        } else {
            return null;
        }
    }

    public String[] getExtendByDayArr() {
        if (extendByDayArr != null) {
            return extendByDayArr.clone();
        } else {
            return null;
        }
    }
}
