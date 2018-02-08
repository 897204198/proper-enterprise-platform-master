package com.proper.enterprise.platform.search.common.document;

import com.proper.enterprise.platform.search.api.document.SearchColumn;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "search_column")
public class SearchDocument implements SearchColumn {

    @Indexed
    private String con;

    private String col;

    private String tab;

    private String des;

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }

    @Override
    public String getCol() {
        return col;
    }

    @Override
    public void setCol(String col) {
        this.col = col;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
