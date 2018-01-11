package com.proper.enterprise.platform.page.custom.rdb.service.impl;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.utils.DateUtil;
import com.proper.enterprise.platform.core.utils.StringUtil;
import com.proper.enterprise.platform.page.custom.rdb.service.RdbOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.*;

@Service
public class RdbOperationServiceImpl implements RdbOperationService {

    @Autowired
    DataSource dataSource;

    @Override
    public Map<String, Object> getDataById(String tableName, String id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        StringBuilder sql = new StringBuilder("");
        sql.append("select * from ").append(tableName).append(" where id=?");
        return jdbcTemplate.queryForMap(sql.toString(), id);
    }

    @Override
    public List<Map<String, Object>> getDataForList(String tableName, String condition, String order) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select * from ").append(tableName);
        if (StringUtil.isNotEmpty(condition)) {
            sql.append(" where ").append(condition);
        }
        if (StringUtil.isNotEmpty(order)) {
            sql.append(" order by ").append(order);
        }
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.queryForList(sql.toString());
    }

    @Override
    public DataTrunk<Map<String, Object>> getDataForPage(String tableName, String condition, String order,
            int pageNo, int pageSize) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        StringBuilder sql = new StringBuilder("");
        sql.append("select count(*) from ").append(tableName);
        if (StringUtil.isNotEmpty(condition)) {
            sql.append(" where ").append(condition);
        }
        DataTrunk dataTrunk = new DataTrunk();
        long count = jdbcTemplate.queryForObject(sql.toString(), Long.class);
        dataTrunk.setCount(count);

        sql = new StringBuilder("");
        sql.append("select * from ").append(tableName);
        if (StringUtil.isNotEmpty(condition)) {
            sql.append(" where ").append(condition);
        }
        if (StringUtil.isNotEmpty(order)) {
            sql.append(" order by ").append(order);
        }
        sql.append(" limit ?,?");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), (pageNo - 1) * pageSize, pageSize);
        dataTrunk.setData(list);
        return dataTrunk;
    }

    @Override
    public String addData(String tableName, String userId, Map<String, String> dataMap) {
        String id = UUID.randomUUID().toString();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        StringBuilder sqlN = new StringBuilder("");
        StringBuilder sqlV = new StringBuilder("");
        StringBuilder sqlNT = new StringBuilder("");
        StringBuilder sqlVT = new StringBuilder("");
        boolean hasId = false;
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            if ("id".equals(entry.getKey())) {
                hasId = true;
            }
            if (!"create_user_id".equals(entry.getKey()) && !"create_time".equals(entry.getKey())
                && !"last_modify_user_id".equals(entry.getKey()) && !"last_modify_time".equals(entry.getKey())) {
                sqlNT.append(entry.getKey()).append(",");
                sqlVT.append(":").append(entry.getKey()).append(",");
                paramMap.put(entry.getKey(), entry.getValue());
            }
        }
        if (hasId) {
            sqlN.append("insert into ").append(tableName).append("(").append(sqlNT.toString());
            sqlV.append(" values(").append(sqlVT.toString());
        } else {
            sqlN.append("insert into ").append(tableName).append("(id,").append(sqlNT.toString());
            sqlV.append(" values(:id,").append(sqlVT.toString());
            paramMap.put("id", id);
        }
        sqlN.append("create_user_id,create_time,last_modify_user_id,last_modify_time)");
        sqlV.append(":create_user_id,:create_time,:last_modify_user_id,:last_modify_time)");
        String time = DateUtil.getTimestamp(true);
        paramMap.put("create_user_id", userId);
        paramMap.put("create_time", time);
        paramMap.put("last_modify_user_id", userId);
        paramMap.put("last_modify_time", time);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplate.update(sqlN.toString() + sqlV.toString(), paramMap);
        return id;
    }

    @Override
    public void updateDataById(String tableName, String id, String userId, Map<String, String> dataMap) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);
        StringBuilder sql = new StringBuilder("");
        sql.append("update ").append(tableName).append(" set ");
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            if (!"last_modify_user_id".equals(entry.getKey()) && !"last_modify_time".equals(entry.getKey())) {
                sql.append(entry.getKey()).append("=:").append(entry.getKey()).append(",");
                paramMap.put(entry.getKey(), entry.getValue());
            }
        }
        sql.append("last_modify_user_id=:last_modify_user_id,last_modify_time=:last_modify_time");
        paramMap.put("last_modify_user_id", userId);
        paramMap.put("last_modify_time", DateUtil.getTimestamp(true));
        sql.append(" where id=:id");
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplate.update(sql.toString(), paramMap);
    }

    @Override
    public void deleteDataById(String tableName, String id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        StringBuilder sql = new StringBuilder("");
        sql.append("delete from ").append(tableName).append(" where id=?");
        jdbcTemplate.update(sql.toString(), id);
    }

    @Override
    public void deleteDataByIds(String tableName, String ids) {
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("ids", Arrays.asList(ids.split(",")));
        StringBuilder sql = new StringBuilder("");
        sql.append("delete from ").append(tableName).append(" where id in (:ids)");
        jdbcTemplate.update(sql.toString(), paramMap);
    }

    @Override
    public List<Map<String, Object>> getTables() throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            rs = metaData.getTables(conn.getCatalog(), null, null, new String[]{"TABLE"});
            while (rs.next()) {
                map = new HashMap<String, Object>();
                map.put("TABLE_NAME", rs.getString("TABLE_NAME"));
                map.put("REMARKS", rs.getString("REMARKS"));
                list.add(map);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getTableInfo(String tableName) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            rs = metaData.getColumns(conn.getCatalog(), null, tableName.toUpperCase(), null);
            while (rs.next()) {
                map = new HashMap<>();
                map.put("COLUMN_NAME", rs.getString("COLUMN_NAME"));
                map.put("TYPE_NAME", rs.getString("TYPE_NAME"));
                map.put("COLUMN_SIZE", rs.getString("COLUMN_SIZE"));
                map.put("REMARKS", rs.getString("REMARKS"));
                list.add(map);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return list;
    }

    @Override
    public Map<String, Object> mapKeyToLowerCase(Map<String, Object> map) {
        Map<String, Object> newMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            newMap.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        return newMap;
    }
}
