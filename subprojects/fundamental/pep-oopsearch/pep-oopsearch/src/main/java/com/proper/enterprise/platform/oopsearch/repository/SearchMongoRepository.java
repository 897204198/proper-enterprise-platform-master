package com.proper.enterprise.platform.oopsearch.repository;

import com.proper.enterprise.platform.oopsearch.document.SearchDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SearchMongoRepository extends MongoRepository<SearchDocument, String> {

    /**
     * 从mongodb中根据输入内容，模糊查询到匹配内容集合并返回
     *
     * @param table 根据模块名获取的表名集合
     * @param content 查询框输入的字符串内容
     * @param pageable 分页信息
     * @return 查询文档集合
     */
    List<SearchDocument> findByTabInAndConLike(List table, String content, Pageable pageable);
}
