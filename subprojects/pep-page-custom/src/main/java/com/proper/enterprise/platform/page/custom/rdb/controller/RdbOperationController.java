package com.proper.enterprise.platform.page.custom.rdb.controller;

import com.proper.enterprise.platform.api.auth.model.User;
import com.proper.enterprise.platform.api.auth.service.UserService;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.page.custom.rdb.service.RdbOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rdb")
public class RdbOperationController extends BaseController {

    @Autowired
    RdbOperationService rdbOperationService;

    @Autowired
    UserService userService;

    /**
     * 通过id查询指定表数据
     */
    @RequestMapping(path = "/{tableName}/{id}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getDataById(@PathVariable String tableName, @PathVariable String id) throws Exception {
        Map<String, Object> map = rdbOperationService.getDataById(tableName, id);
        Map<String, Object> mapNew = rdbOperationService.mapKeyToLowerCase(map);
        return responseOfGet(mapNew);
    }

    /**
     * 通过条件查询指定表数据
     */
    @RequestMapping(path = "/{tableName}", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getDataForList(@PathVariable String tableName, String condition,
                String order) throws Exception {
        List<Map<String, Object>> list = rdbOperationService.getDataForList(tableName, condition, order);
        List<Map<String, Object>> listNew = new ArrayList<Map<String, Object>>();
        Map<String, Object> mapNew = null;
        for (Map<String, Object> map : list) {
            mapNew = rdbOperationService.mapKeyToLowerCase(map);
            listNew.add(mapNew);
        }
        return responseOfGet(listNew);
    }

    /**
     * 通过条件查询指定表数据
     */
    @RequestMapping(path = "/page/{tableName}", method = RequestMethod.GET)
    public ResponseEntity<DataTrunk<Map<String, Object>>> getDataForPage(@PathVariable String tableName,
            String condition, String order, int pageNo, int pageSize) throws Exception {
        DataTrunk<Map<String, Object>> page = rdbOperationService.getDataForPage(
                tableName, condition, order, pageNo, pageSize);
        List<Map<String, Object>> listNew = new ArrayList<Map<String, Object>>();
        Map<String, Object> mapNew = null;
        for (Map<String, Object> map : page.getData()) {
            mapNew = rdbOperationService.mapKeyToLowerCase(map);
            listNew.add(mapNew);
        }
        page.setData(listNew);
        return responseOfGet(page);
    }

    /**
     * 指定表添加数据,并返回id
     */
    @RequestMapping(path = "/{tableName}", method = RequestMethod.POST)
    public ResponseEntity<String> addData(@PathVariable String tableName, @RequestBody Map<String, String> dataMap)
            throws Exception {
        User user = userService.getCurrentUser();
        rdbOperationService.addData(tableName, user.getId(), dataMap);
        return new ResponseEntity<>("", HttpStatus.CREATED);
    }

    /**
     * 通过id修改指定表数据
     */
    @RequestMapping(path = "/{tableName}/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateData(@PathVariable String tableName, @PathVariable String id,
            @RequestBody Map<String, String> dataMap) throws Exception {
        User user = userService.getCurrentUser();
        rdbOperationService.updateDataById(tableName, id, user.getId(), dataMap);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    /**
     * 通过id删除指定表数据,id复数用逗号分隔
     */
    @RequestMapping(path = "/{tableName}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteData(@PathVariable String tableName, @RequestParam(required = true) String ids) throws Exception {
        if (ids.split(",").length > 1) {
            rdbOperationService.deleteDataByIds(tableName, ids);
        } else {
            rdbOperationService.deleteDataById(tableName, ids);
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    /**
     * 取得数据库内的所有表名称
     */
    @RequestMapping(path = "/baseInfo/tables", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getTables() throws Exception {
        List<Map<String, Object>> list = rdbOperationService.getTables();
        for (Map<String, Object> map : list) {
            map.put("TABLE_NAME", map.get("TABLE_NAME").toString().toLowerCase());
        }
        return responseOfGet(list);
    }

    /**
     * 取得指定表的基本信息
     */
    @RequestMapping(path = "/baseInfo/tables/{tableName}", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getTableInfo(@PathVariable String tableName) throws Exception {
        List<Map<String, Object>> list = rdbOperationService.getTableInfo(tableName);
        for (Map<String, Object> map : list) {
            map.put("COLUMN_NAME", map.get("COLUMN_NAME").toString().toLowerCase());
        }
        return responseOfGet(list);
    }
}
