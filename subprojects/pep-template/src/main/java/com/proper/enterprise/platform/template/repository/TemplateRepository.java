package com.proper.enterprise.platform.template.repository;

import com.proper.enterprise.platform.template.document.TemplateDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TemplateRepository extends MongoRepository<TemplateDocument, String> {

    /**
     * 查询指定业务的模板
     *
     * @param code 关键字
     * @return 模板
     */
    TemplateDocument findByCode(String code);

    /**
     * 查询指定业务的启用模板
     *
     * @param code 关键字
     * @return 模板
     */
    TemplateDocument findByCodeAndEnableTrue(String code);

    /**
     * 查询启用状态的模板列表
     *
     * @return 启用状态的模板列表
     */
    List<TemplateDocument> findByEnableTrue();

    /**
     * 查询指定模板
     *
     * @param id  主鍵
     * @return 模板
     */
    TemplateDocument findByIdAndEnableTrue(String id);

    /**
     * 分页
     *
     * @param query       查询内容
     * @param pageable    分页信息
     * @return 分页
     */
    @Query("{"
        + "    enable : true, "
        + "    $or: ["
        + "       {code: {$regex: ?0}}, "
        + "       {name: {$regex: ?0}}, "
        + "       {description: {$regex: ?0}}"
        + "    ]"
        + " }")
    Page<TemplateDocument> findPagination(String query, Pageable pageable);

}
