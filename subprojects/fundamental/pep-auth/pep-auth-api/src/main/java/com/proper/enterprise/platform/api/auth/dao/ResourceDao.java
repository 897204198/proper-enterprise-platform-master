package com.proper.enterprise.platform.api.auth.dao;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.core.service.BaseService;

import java.util.Collection;

@SuppressWarnings("unchecked")
public interface ResourceDao extends BaseService<Resource, String> {

    /**
     * 保存资源
     * @param resource 资源信息
     * @return 资源信息
     */
    Resource save(Resource resource);

    /**
     * 修改资源
     * @param resource 将修改的资源信息
     * @return 修改后的资源信息
     */
    Resource updateForSelective(Resource resource);

    /**
     * 查询
     * @param enableEnum 是否可用
     * @return 资源信息集合
     */
    Collection<? extends Resource> findAll(EnableEnum enableEnum);

    /**
     * 查询
     * @param name 姓名
     * @param enableEnum 权限
     * @return 资源信息集合
     */
    Collection<? extends Resource> findAll(String name, EnableEnum enableEnum);

    /**
     * 查询
     * @param ids 多个资源id
     * @return 资源信息集合
     */
    Collection<? extends Resource> findAll(Collection<String> ids);

    /**
     * 获取一个新的Resource实体
     * @return 新的Resource实体;
     */
    Resource getNewResourceEntity();

    /**
     * 通过id获取
     * @param id id;
     * @return 资源信息;
     */
    Resource get(String id);

    /**
     * 删除所有
     */
    void deleteAll();
}
