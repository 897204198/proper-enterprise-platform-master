package com.proper.enterprise.platform.core.controller;

import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.support.AbstractQuerySupport;
import com.proper.enterprise.platform.core.utils.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collection;
import java.util.List;

/**
 * REST Controller 的基类
 * <p>
 * 用于响应各类 method 的请求
 */
public abstract class BaseController extends AbstractQuerySupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

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

    protected <T, S> ResponseEntity<T> responseOfPost(S entity, Class<T> classType, Class... showType) {
        return new ResponseEntity<>(BeanUtil.convertToVO(entity, classType, showType), HttpStatus.CREATED);
    }

    /**
     * 返回 POST 请求的响应
     * 创建实体成功时返回 201 Created 状态及被创建的实体
     *
     * @param entity  被创建的实体
     * @param headers 响应头信息
     * @param <T>     实体类型
     * @return POST 请求的响应
     */
    protected <T> ResponseEntity<T> responseOfPost(T entity, MultiValueMap<String, String> headers) {
        return new ResponseEntity<>(entity, headers, HttpStatus.CREATED);
    }

    /**
     * 返回 GET 请求的响应
     * 查询到结果时返回 200 OK 状态及查询结果
     * 没有查到结果时返回 200 OK 状态
     *
     * @param entity 查询结果
     * @param <T>    结果对象类型
     * @return GET 请求的响应
     */
    protected <T> ResponseEntity<T> responseOfGet(T entity) {
        return responseOKWithOrWithoutContent(entity, null);
    }


    protected <T, S> ResponseEntity<T> responseOfGet(S entity, Class<T> classType, Class... showType) {
        return responseOKWithOrWithoutContent(BeanUtil.convertToVO(entity, classType, showType), null);
    }

    protected <T, S> ResponseEntity<DataTrunk<T>> responseOfGet(DataTrunk<S> entity, Class<T> classType, Class... showType) {
        return responseOKWithOrWithoutContent(BeanUtil.convertToVO(entity, classType, showType), null);
    }

    protected <T, S> ResponseEntity<Collection<T>> responseOfGet(Collection<S> entity, Class<T> classType, Class... showType) {
        return responseOKWithOrWithoutContent(BeanUtil.convertToVO(entity, classType, showType), null);
    }

    /**
     * 返回 GET 请求的响应
     * 查询到结果时返回 200 OK 状态及查询结果
     * 没有查到结果时返回 200 OK 状态
     *
     * @param entity  查询结果
     * @param headers 响应头信息
     * @param <T>     结果对象类型
     * @return GET 请求的响应
     */
    protected <T> ResponseEntity<T> responseOfGet(T entity, MultiValueMap<String, String> headers) {
        return responseOKWithOrWithoutContent(entity, headers);
    }

    /**
     * 返回 GET 请求的响应
     * 结果集封装到一个 DataTrunk 对象中
     * 查询到结果时返回 200 OK 状态及查询结果
     * 没有查到结果时返回 200 OK 状态
     *
     * @param list  结果集数据集合
     * @param count 数据总数
     * @param <T>   DataTrunk 保存的对象类型
     * @return GET 请求的响应
     */
    protected <T> ResponseEntity<DataTrunk<T>> responseOfGet(List<T> list, long count) {
        return responseOKWithOrWithoutContent(new DataTrunk<>(list, count), null);
    }

    /**
     * 返回 GET 请求的响应
     * 结果集封装到一个 DataTrunk 对象中
     * 查询到结果时返回 200 OK 状态及查询结果
     * 没有查到结果时返回 200 OK 状态
     *
     * @param list    结果集数据集合
     * @param count   数据总数
     * @param headers 响应头信息
     * @param <T>     DataTrunk 保存的对象类型
     * @return GET 请求的响应
     */
    protected <T> ResponseEntity<DataTrunk<T>> responseOfGet(List<T> list, long count, MultiValueMap<String, String> headers) {
        return responseOKWithOrWithoutContent(new DataTrunk<>(list, count), headers);
    }

    /**
     * 返回 PUT 请求的响应
     * 查询到要更新的实体并更新成功时返回 200 OK 状态及更新后的实体
     * 没有查到结果时返回 200 OK 状态
     *
     * @param entity 更新后的实体
     * @param <T>    更新的实体类型
     * @return PUT 请求的响应
     */
    protected <T> ResponseEntity<T> responseOfPut(T entity) {
        return responseOKWithOrWithoutContent(entity, null);
    }

    protected <T, S> ResponseEntity<T> responseOfPut(S entity, Class<T> classType, Class... showType) {
        return responseOKWithOrWithoutContent(BeanUtil.convertToVO(entity, classType, showType), null);
    }

    protected <T, S> ResponseEntity<Collection<T>> responseOfPut(Collection<S> collection, Class<T> classType, Class... showType) {
        return responseOKWithOrWithoutContent(BeanUtil.convertToVO(collection, classType, showType), null);
    }

    /**
     * 返回 PUT 请求的响应
     * 查询到要更新的实体并更新成功时返回 200 OK 状态及更新后的实体
     * 没有查到结果时返回 200 OK 状态
     *
     * @param entity  更新后的实体
     * @param headers 响应头信息
     * @param <T>     更新的实体类型
     * @return PUT 请求的响应
     */
    protected <T> ResponseEntity<T> responseOfPut(T entity, MultiValueMap<String, String> headers) {
        return responseOKWithOrWithoutContent(entity, headers);
    }

    private <T> ResponseEntity<T> responseOKWithOrWithoutContent(T entity, MultiValueMap<String, String> headers) {
        boolean noContent = entity == null;
        boolean isCollection = false;
        if (!noContent && entity instanceof Collection) {
            isCollection = true;
            noContent = ((Collection) entity).isEmpty();
        }
        return noContent && !isCollection
            ? new ResponseEntity<>(headers, HttpStatus.OK)
            : (new ResponseEntity<>(entity, headers, HttpStatus.OK));
    }

    /**
     * 返回 DELETE 请求的响应
     * 查询到要删除的对象并删除成功时返回 204 NO_CONTENT 状态
     * 没有查到结果时返回 404 Not found 状态
     *
     * @param exist 是否成功删除对象
     * @return DELETE 请求的响应
     */
    protected ResponseEntity responseOfDelete(boolean exist) {
        return new ResponseEntity(exist ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex, WebRequest request) {
        HttpStatus status = handleStatus(ex, request);
        HttpHeaders headers = handleHeaders(ex);
        String body = handleBody(ex);
        if (!(ex instanceof ErrMsgException)) {
            LOGGER.error("Controller throws exception", ex);
        }
        LOGGER.debug("Handle controller's exception to {}:{}:{}", status, headers, body);
        return new ResponseEntity<>(body, headers, status);
    }

    protected HttpHeaders handleHeaders(Exception ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"));
        headers.set(PEPConstants.RESPONSE_HEADER_ERROR_TYPE, PEPConstants.RESPONSE_SYSTEM_ERROR);
        if (ex instanceof ErrMsgException) {
            headers.set(PEPConstants.RESPONSE_HEADER_ERROR_TYPE, PEPConstants.RESPONSE_BUSINESS_ERROR);
        }
        return headers;
    }

    protected HttpStatus handleStatus(Exception ex, WebRequest request) {
        ResponseEntityExceptionHandler handler = new ResponseEntityExceptionHandler() {
        };
        ResponseEntity res = handler.handleException(ex, request);
        return res.getStatusCode();
    }

    protected String handleBody(Exception ex) {
        return null == ex.getMessage() ? PEPConstants.RESPONSE_SYSTEM_ERROR_MSG : ex.getMessage();
    }


}
