package com.proper.enterprise.platform.search.api.model;

public class SearchColumn {

    private String column;

    private String table;

    private String type;

    private String descColumn;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescColumn() {
        return descColumn;
    }

    public void setDescColumn(String descColumn) {
        this.descColumn = descColumn;
    }
}
