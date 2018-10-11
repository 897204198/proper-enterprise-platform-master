package com.proper.enterprise.platform.notice.repository;

import com.proper.enterprise.platform.notice.document.NoticeSetDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface NoticeSetRepository extends MongoRepository<NoticeSetDocument, String> {

    /**
     * 查询当前人员的各通知类型设置情况
     *
     * @param userId 当前人员编号
     * @return 通知设置列表
     */
    List<NoticeSetDocument> findByUserId(String userId);

    /**
     * 查询指定通知类型下的人员设置
     *
     * @param catalog 通知类型
     * @param userId     当前人员编号
     * @return 通知设置列表
     */
    List<NoticeSetDocument> findByCatalogAndUserId(String catalog, String userId);

    /**
     * 查询指定通知类型下的人员设置
     *
     * @param catalog 通知类型
     * @param userId     当前人员列表
     * @return 通知设置列表
     */
    List<NoticeSetDocument> findByCatalogAndUserIdIn(String catalog, Set<String> userId);

}
