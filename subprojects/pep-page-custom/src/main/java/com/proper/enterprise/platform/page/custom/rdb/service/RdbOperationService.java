package com.proper.enterprise.platform.page.custom.rdb.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;

import java.util.List;
import java.util.Map;

public interface RdbOperationService {

    /**
     * 通过id查询指定表数据
     */
    Map<String, Object> getDataById(String tableName, String id);

    /**
     * 通过条件查询指定表数据
     */
    List<Map<String, Object>> getDataForList(String tableName, String condition, String order);

    /**
     * 通过条件查询指定表数据
     */
    DataTrunk<Map<String, Object>> getDataForPage(String tableName, String condition, String order,
            int pageNo, int pageSize);

    /**
     * 指定表添加数据,并返回id
     */
    String addData(String tableName, String userId, Map<String, String> dataMap);

    /**
     * 通过id修改指定表数据
     */
    void updateDataById(String tableName, String id, String userId, Map<String, String> dataMap);

    /**
     * 通过id删除指定表数据
     */
    void deleteDataById(String tableName, String id);

    /**
     * 通过ids删除指定表数据
     */
    void deleteDataByIds(String tableName, String ids);

    /**
     * 取得数据库内的所有表名称
     */
    List<Map<String, Object>> getTables() throws Exception;

    /**
     * 取得指定表的基本信息
     */
    List<Map<String, Object>> getTableInfo(String tableName) throws Exception;

    /**
     * mapKey大写转小写
     */
    Map<String, Object> mapKeyToLowerCase(Map<String, Object> map);
}
