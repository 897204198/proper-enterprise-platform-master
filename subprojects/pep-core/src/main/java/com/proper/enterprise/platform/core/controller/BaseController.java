package com.proper.enterprise.platform.core.controller;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collection;
import java.util.List;

/**
 * REST Controller 的基类
 *
 * 用于响应各类 method 的请求
 */
public abstract class BaseController {

    private final static Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    /**
     * 返回 POST 请求的响应
     * 创建实体成功时返回 201 Created 状态及被创建的实体
     *
     * @param  entity 被创建的实体
     * @param  <T>    实体类型
     * @return POST 请求的响应
     */
    protected <T> ResponseEntity<T> responseOfPost(T entity) {
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    /**
     * 返回 GET 请求的响应
     * 查询到结果时返回 200 OK 状态及查询结果
     * 没有查到结果时返回 200 OK 状态
     *
     * @param  entity 查询结果
     * @param  <T>    结果对象类型
     * @return GET 请求的响应
     */
    protected <T> ResponseEntity<T> responseOfGet(T entity) {
        return responseOKWithOrWithoutContent(entity);
    }

    /**
     * 返回 GET 请求的响应
     * 结果集封装到一个 DataTrunk 对象中
     * 查询到结果时返回 200 OK 状态及查询结果
     * 没有查到结果时返回 200 OK 状态
     *
     * @param  list  结果集数据集合
     * @param  count
     * @param  <T>
     * @return
     */
    protected <T> ResponseEntity<DataTrunk<T>> responseOfGet(List<T> list, long count) {
        return responseOKWithOrWithoutContent(new DataTrunk<>(list, count));
    }

    /**
     * 返回 PUT 请求的响应
     * 查询到要更新的实体并更新成功时返回 200 OK 状态及更新后的实体
     * 没有查到结果时返回 200 OK 状态
     *
     * @param  entity 更新后的实体
     * @param  <T>    更新的实体类型
     * @return PUT 请求的响应
     */
    protected <T> ResponseEntity<T> responseOfPut(T entity) {
        return responseOKWithOrWithoutContent(entity);
    }

    private <T> ResponseEntity<T> responseOKWithOrWithoutContent(T entity) {
        boolean noContent = true;
        if (entity != null) {
            noContent = entity instanceof Collection && ((Collection) entity).isEmpty();
        }
        return noContent
            ? new ResponseEntity<T>(HttpStatus.OK)
            : (new ResponseEntity<>(entity, HttpStatus.OK));
    }

    /**
     * 返回 DELETE 请求的响应
     * 查询到要删除的对象并删除成功时返回 200 OK 状态
     * 没有查到结果时返回 404 Not found 状态
     *
     * @param  exist 是否成功删除对象
     * @return DELETE 请求的响应
     */
    protected ResponseEntity responseOfDelete(boolean exist) {
        return new ResponseEntity(exist ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"));

        ResponseEntityExceptionHandler handler = new ResponseEntityExceptionHandler() { };
        ResponseEntity res = handler.handleException(ex, request);
        HttpStatus status = res.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR) ? HttpStatus.BAD_REQUEST : res.getStatusCode();

        LOGGER.debug("Handle controller's exception to {}:{}", status, ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), headers, status);
    }

}
