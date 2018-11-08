package com.proper.enterprise.platform.core.mongo.controller;

import com.proper.enterprise.platform.core.CoreProperties;
import com.proper.enterprise.platform.core.PEPPropertiesLoader;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.mongo.service.MongoShellService;
import com.proper.enterprise.platform.core.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/msc")
@Api(tags = "/msc")
public class MongoShellController extends BaseController {

    private static final String COMMA = ",";

    @Autowired
    private MongoShellService service;

    @PostMapping("/{collection}")
    @ApiOperation("‍保存mongo的数据")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Document> create(@ApiParam(value = "‍表名", required = true) @PathVariable String collection, @RequestBody String document) {
        return responseOfPost(service.insertOne(collection, document));
    }

    @GetMapping("/{collection}/{id}")
    @ApiOperation("‍根据id获取对应表的数据")
    public ResponseEntity<Document> get(@ApiParam(value = "‍表名", required = true) @PathVariable String collection,
                                        @ApiParam(value = "‍表对应的id", required = true) @PathVariable String id) throws Exception {
        Document doc = service.queryById(collection, id);
        return responseOfGet(doc);
    }

    @GetMapping("/{collection}")
    @ApiOperation("‍获取mongo的数据，参考文档：https://docs.mongodb.com/manual/tutorial/query-documents/")
    public ResponseEntity<DataTrunk<Document>> find(@ApiParam(value = "‍表名", required = true) @PathVariable String collection,
                                                    @ApiParam(value = "‍查询条件", required = true) @RequestParam String query,
                                                    String limit, String sort) throws Exception {
        query = decodeUrl(query);
        List<Document> docs = service.query(collection, query,
            StringUtil.isNumeric(limit) ? Integer.parseInt(limit) : -1,
            StringUtil.isNotNull(sort) ? decodeUrl(sort) : null);

        return docs.isEmpty()
            ? responseOfGet(new ArrayList<Document>(), 0)
            : responseOfGet(docs, service.count(collection, query));
    }

    private String decodeUrl(String str) throws UnsupportedEncodingException {
        return URLDecoder.decode(str, PEPPropertiesLoader.load(CoreProperties.class).getCharset());
    }

    @DeleteMapping("/{collection}/{ids}")
    @ApiOperation("‍删除ID对应表的信息")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteByIds(@ApiParam(value = "‍表名", required = true) @PathVariable String collection,
                                      @ApiParam(value = "‍id列表(使用\",\"分割)", required = true) @PathVariable String ids) throws Exception {
        boolean exist;
        if (ids.contains(COMMA)) {
            List<Document> docs = service.deleteByIds(collection, ids.split(","));
            exist = !docs.isEmpty();
        } else {
            Document doc = service.deleteById(collection, ids);
            exist = doc != null;
        }
        return responseOfDelete(exist);
    }

    @PutMapping("/{collection}/{id}")
    @ApiOperation("‍根据id修改对应表的数据")
    public ResponseEntity<Document> update(@ApiParam(value = "‍表名", required = true) @PathVariable String collection,
                                           @ApiParam(value = "‍表对应的id", required = true) @PathVariable String id,
                                           @RequestBody String update) throws Exception {
        return responseOfPut(service.updateById(collection, id, update));
    }

}
