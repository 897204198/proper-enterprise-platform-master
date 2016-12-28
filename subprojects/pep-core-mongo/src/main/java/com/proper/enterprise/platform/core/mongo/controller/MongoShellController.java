package com.proper.enterprise.platform.core.mongo.controller;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.mongo.service.MongoShellService;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/msc")
public class MongoShellController extends BaseController {

    @Autowired
    private MongoShellService service;

    @PostMapping("/{collection}")
    public ResponseEntity<String> create(@PathVariable String collection, @RequestBody String document) {
        return responseOfPost(service.insertOne(collection, document).toJson());
    }

    @GetMapping("/{collection}/{id}")
    public ResponseEntity<String> get(@PathVariable String collection, @PathVariable String id) throws Exception {
        Document doc = service.queryById(collection, id);
        return responseOfGet(doc != null ? doc.toJson() : null);
    }

    @GetMapping("/{collection}")
    public ResponseEntity<DataTrunk<String>> find(@PathVariable String collection,
                                             @RequestParam String query, String limit, String sort) throws Exception {
        query = decodeUrl(query);
        List<Document> docs = service.query(collection, query,
            StringUtil.isNumeric(limit) ? Integer.parseInt(limit) : -1,
            StringUtil.isNotNull(sort) ? decodeUrl(sort) : null);

        return responseOfGet(documentToJson(docs), service.count(collection, query));
    }

    private String decodeUrl(String str) throws UnsupportedEncodingException {
        return URLDecoder.decode(str, PEPConstants.DEFAULT_CHARSET.toString());
    }

    /**
     * 将 Document 转换成 json 需使用 Document 中提供的 toJson 方法
     * 通用的 json 转换类转出的 objectId 无法再转换成对象
     *
     * @param  docs Document 列表
     * @return Document json 字符串列表
     */
    private List<String> documentToJson(List<Document> docs) {
        Assert.notEmpty(docs, "Document list SHOULD NOT EMPTY!");
        List<String> result = new ArrayList<>();
        for (Document doc : docs) {
            result.add(doc.toJson());
        }
        return result;
    }

    @DeleteMapping("/{collection}/{ids}")
    public ResponseEntity deleteByIds(@PathVariable String collection, @PathVariable String ids) throws Exception {
        boolean exist;
        if (ids.contains(",")) {
            List<Document> docs = service.deleteByIds(collection, ids.split(","));
            exist = !docs.isEmpty();
        } else {
            Document doc = service.deleteById(collection, ids);
            exist = doc != null;
        }
        return responseOfDelete(exist);
    }

    @PutMapping("/{collection}/{id}")
    public ResponseEntity<String> update(@PathVariable String collection,
                                         @PathVariable String id, @RequestBody String update) throws Exception {
        return responseOfPut(service.updateById(collection, id, update).toJson());
    }

}
