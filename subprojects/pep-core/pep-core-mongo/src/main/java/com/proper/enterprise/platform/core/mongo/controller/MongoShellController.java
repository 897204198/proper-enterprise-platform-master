package com.proper.enterprise.platform.core.mongo.controller;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.controller.BaseController;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.mongo.service.MongoShellService;
import com.proper.enterprise.platform.core.utils.StringUtil;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/msc")
public class MongoShellController extends BaseController {

    private static final String COMMA = ",";

    @Autowired
    private MongoShellService service;

    @PostMapping("/{collection}")
    public ResponseEntity<Document> create(@PathVariable String collection, @RequestBody String document) {
        return responseOfPost(service.insertOne(collection, document));
    }

    @GetMapping("/{collection}/{id}")
    public ResponseEntity<Document> get(@PathVariable String collection, @PathVariable String id) throws Exception {
        Document doc = service.queryById(collection, id);
        return responseOfGet(doc);
    }

    @GetMapping("/{collection}")
    public ResponseEntity<DataTrunk<Document>> find(@PathVariable String collection,
                                                    @RequestParam String query, String limit, String sort) throws Exception {
        query = decodeUrl(query);
        List<Document> docs = service.query(collection, query,
            StringUtil.isNumeric(limit) ? Integer.parseInt(limit) : -1,
            StringUtil.isNotNull(sort) ? decodeUrl(sort) : null);

        return docs.isEmpty()
            ? responseOfGet(new ArrayList<Document>(), 0)
            : responseOfGet(docs, service.count(collection, query));
    }

    private String decodeUrl(String str) throws UnsupportedEncodingException {
        return URLDecoder.decode(str, PEPConstants.DEFAULT_CHARSET.toString());
    }

    @DeleteMapping("/{collection}/{ids}")
    public ResponseEntity deleteByIds(@PathVariable String collection, @PathVariable String ids) throws Exception {
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
    public ResponseEntity<Document> update(@PathVariable String collection,
                                           @PathVariable String id, @RequestBody String update) throws Exception {
        return responseOfPut(service.updateById(collection, id, update));
    }

}
