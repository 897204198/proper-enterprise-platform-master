package com.proper.enterprise.platform.feedback.repository;

import com.proper.enterprise.platform.feedback.document.UserFeedBackDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserFeedBackRepository extends MongoRepository<UserFeedBackDocument, String> {

    /**
     * 根据userId获取UserFeedBackDocument
     * @param userId userId
     * @return UserFeedBackDocument
     */
    UserFeedBackDocument findByUserId(String userId);

    /**
     * 分页模糊查询
     * @param statusCode 反馈状态
     * @param allStatusExp 全部的状态
     * @param query 其他查询参数（手机号和用户名）
     * @param pageable 分页
     * @return 分页数据
     */
    @Query("{"
            + "    $and: ["
            + "        {$or: [{statusCode: ?0}, {$where: ?1} ]},"
            + "        {$or: [{userName: {$regex: ?2}}, {userTel: {$regex: ?2}}]}"
            + "    ]"
            + "}")
    Page<UserFeedBackDocument> findByUserAndStatus(String statusCode, String allStatusExp, String query, Pageable pageable);

}
