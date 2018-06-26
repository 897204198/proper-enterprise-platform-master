package com.proper.enterprise.platform.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.List;

public interface MongoDataBaseService {

    /**
     * 增方法
     * @param root 路径
     * @param collection collection
     * @return document
     * @throws Exception 异常信息
     */
    Document insertOne(JsonNode root, String collection) throws Exception;

    /**
     * 删除方法
     * @param collection collection
     * @param objectIds ids
     * @return 数量
     * @throws Exception 异常信息
     */
    int delete(String collection, String objectIds) throws Exception;

    /**
     * 通过id删除
     * @param collection collection
     * @param objectIds ids
     * @return 返回对象
     * @throws Exception 异常信息
     */
    Document deleteById(String collection, String objectIds) throws Exception;

    /**
     * 改方法
     * @param root 路径
     * @param collection collection
     * @param objectId id
     * @return 返回结果
     * @throws Exception 异常信息
     */
    UpdateResult updateById(JsonNode root, String collection, String objectId) throws Exception;

    /**
     * 查询方法
     * @param root 路径
     * @param collection collection
     * @return 对象的集合
     * @throws Exception 异常信息
     */
    List<Document> query(JsonNode root, String collection) throws Exception;

    /**
     * 查方法
     * @param root 路径
     * @param collection collection
     * @return 对象总数
     * @throws Exception 异常信息
     */
    long count(JsonNode root, String collection) throws Exception;

    /**
     * 根据id查询
     * @param id id
     * @param collection collection
     * @return 对象
     * @throws Exception 异常信息
     */
    Document queryById(String id, String collection) throws Exception;

    /**
     * 删除表
     * @param collection collection
     * @throws Exception 异常信息
     */
    void droptable(String collection) throws Exception;

}
