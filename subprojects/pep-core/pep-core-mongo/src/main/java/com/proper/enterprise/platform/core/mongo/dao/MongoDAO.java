package com.proper.enterprise.platform.core.mongo.dao;

import org.bson.Document;

import java.util.List;

/**
 * 提供一个按照 mongo shell 语法操作 MongoDB 数据库的接口
 */
public interface MongoDAO {

    /**
     * 向 collection 中插入一条文档记录
     *
     * @param collection    集合名称
     * @param document      文档记录的 json 表示
     * @return 插入的文档记录
     */
    Document insertOne(String collection, String document);

    /**
     * 根据 id 删除一条文档记录
     *
     * @param collection    集合名称
     * @param id            object id
     * @return 删除的记录
     * @throws Exception 删除时发生异常
     */
    Document deleteById(String collection, String id) throws Exception;

    /**
     * 根据 id 数组删除所有文档记录
     *
     * @param collection    集合名称
     * @param ids           id 数组
     * @return 所有被删除的文档记录
     * @throws Exception 删除时发生异常
     */
    List<Document> deleteByIds(String collection, String[] ids) throws Exception;

    /**
     * 根据 id 更新集合中的文档
     *
     * @param collection    集合名称
     * @param id            object id
     * @param update        update 方法参数，语法可参考 mongo shell 语法
     * @return 被更新的文档
     * @throws Exception 更新时发生异常
     */
    Document updateById(String collection, String id, String update) throws Exception;

    /**
     * 根据 id 查询文档记录
     *
     * @param collection    集合名称
     * @param id            object id
     * @return 查询结果文档
     * @throws Exception 查询时发生异常
     */
    Document queryById(String collection, String id) throws Exception;

    /**
     * 根据条件查询文档集合
     *
     * @param collection    集合名称
     * @param query         查询条件，语法可参考 mongo shell 语法
     * @return 查询结果文档集合
     * @throws Exception 查询时发生异常
     */
    List<Document> query(String collection, String query) throws Exception;

    /**
     * 根据条件查询文档集合
     *
     * @param collection    集合名称
     * @param query         查询条件，语法可参考 mongo shell 语法
     * @param limit         最大记录数，0 表示无限制
     * @return 查询结果文档集合
     * @throws Exception 查询时发生异常
     */
    List<Document> query(String collection, String query, int limit) throws Exception;

    /**
     * 根据条件查询文档集合
     *
     * @param collection    集合名称
     * @param query         查询条件，语法可参考 mongo shell 语法
     * @param sort          排序条件，语法可参考 mongo shell 语法
     * @return 查询结果文档集合
     * @throws Exception 查询时发生异常
     */
    List<Document> query(String collection, String query, String sort) throws Exception;

    /**
     * 根据条件查询文档集合
     *
     * @param collection    集合名称
     * @param query         查询条件，语法可参考 mongo shell 语法
     * @param limit         最大记录数，0 表示无限制
     * @param sort          排序条件，语法可参考 mongo shell 语法
     * @return 查询结果文档集合
     * @throws Exception 查询时发生异常
     */
    List<Document> query(String collection, String query, int limit, String sort) throws Exception;

    /**
     * 根据条件查询文档集合总数
     *
     * @param  collection 集合名称
     * @param  query      查询条件，语法可参考 mongo shell 语法
     * @return 结果集总数
     * @throws Exception 计数时发生异常
     */
    long count(String collection, String query) throws Exception;

    /**
     * 删除集合
     *
     * @param collection    集合名称
     */
    void drop(String collection);

}
