package com.proper.enterprise.platform.page.custom.grid.document;

import com.proper.enterprise.platform.core.mongo.document.BaseDocument;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 表格页面自定义配置信息
 */
@Document(collection = "custom_grid_config")
public class CustomGridDocument extends BaseDocument {

    /**
     * 功能名称
     */
    private String title;

    /**
     * 功能编码
     */
    @Indexed(unique = true)
    private String code;

    /**
     * 数据表
     */
    private String tableName;

    /**
     * 是否一次加载所有表格数据
     */
    private String isAllDataLoad;

    /**
     * 是否可翻页
     */
    private String pagination;

    /**
     * 操作按钮
     */
    private List<String> operationType;

    /**
     * 表数据过滤条件
     */
    private String condition;

    /**
     * 基础数据
     */
    private List<CustomGridDataDocument> initData;

    /**
     * 表格列信息
     */
    private List<CustomGridColumnDocument> columns;

    /**
     * 表格数据排序
     */
    private List<CustomGridOrderDocument> order;

    /**
     * 查询面板元素
     */
    private List<CustomGridElementDocument> search;

    /**
     * 编辑面板元素
     */
    private List<CustomGridElementDocument> dialog;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPagination() {
        return pagination;
    }

    public void setPagination(String pagination) {
        this.pagination = pagination;
    }

    public List<String> getOperationType() {
        return operationType;
    }

    public void setOperationType(List<String> operationType) {
        this.operationType = operationType;
    }

    public List<CustomGridDataDocument> getInitData() {
        return initData;
    }

    public void setInitData(List<CustomGridDataDocument> initData) {
        this.initData = initData;
    }

    public List<CustomGridColumnDocument> getColumns() {
        return columns;
    }

    public void setColumns(List<CustomGridColumnDocument> columns) {
        this.columns = columns;
    }

    public List<CustomGridOrderDocument> getOrder() {
        return order;
    }

    public void setOrder(List<CustomGridOrderDocument> order) {
        this.order = order;
    }

    public List<CustomGridElementDocument> getSearch() {
        return search;
    }

    public void setSearch(List<CustomGridElementDocument> search) {
        this.search = search;
    }

    public List<CustomGridElementDocument> getDialog() {
        return dialog;
    }

    public void setDialog(List<CustomGridElementDocument> dialog) {
        this.dialog = dialog;
    }

    public String getIsAllDataLoad() {
        return isAllDataLoad;
    }

    public void setIsAllDataLoad(String isAllDataLoad) {
        this.isAllDataLoad = isAllDataLoad;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
