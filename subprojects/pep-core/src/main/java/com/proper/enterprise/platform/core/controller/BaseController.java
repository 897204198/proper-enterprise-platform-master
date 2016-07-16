package com.proper.enterprise.platform.core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

/**
 * REST Controller 的基类
 *
 * 用于响应各类 method 的请求
 */
public abstract class BaseController {

    /**
     * 返回 POST 请求的响应
     * 创建实体成功时返回 201 Created 状态及被创建的实体
     *
     * @param entity 被创建的实体
     * @param <T>    实体类型
     * @return POST 请求的响应
     */
    protected <T> ResponseEntity<T> responseOfPost(T entity) {
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    /**
     * 返回 GET 请求的响应
     * 查询到结果时返回 200 OK 状态及查询结果
     * 没有查到结果时返回 404 Not found 状态
     *
     * @param entity 查询结果
     * @param <T>    结果对象类型
     * @return GET 请求的响应
     */
    protected <T> ResponseEntity<T> responseOfGet(T entity) {
        return responseOKOrNotFound(entity);
    }

    /**
     * 返回 PUT 请求的响应
     * 查询到要更新的实体并更新成功时返回 200 OK 状态及更新后的实体
     * 没有查到结果时返回 404 Not found 状态
     *
     * @param entity 更新后的实体
     * @param <T>    更新的实体类型
     * @return PUT 请求的响应
     */
    protected <T> ResponseEntity<T> responseOfPut(T entity) {
        return responseOKOrNotFound(entity);
    }

    private <T> ResponseEntity<T> responseOKOrNotFound(T entity) {
        boolean notFound = true;
        if (entity != null) {
            notFound = entity instanceof Collection && ((Collection) entity).isEmpty();
        }
        return notFound ?
            new ResponseEntity<T>(HttpStatus.NOT_FOUND) :
            (new ResponseEntity<>(entity, HttpStatus.OK));
    }

    /**
     * 返回 DELETE 请求的响应
     * 查询到要删除的对象并删除成功时返回 200 OK 状态
     * 没有查到结果时返回 404 Not found 状态
     *
     * @param exist 是否成功删除对象
     * @return DELETE 请求的响应
     */
    protected ResponseEntity responseOfDelete(boolean exist) {
        return new ResponseEntity(exist ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

}
