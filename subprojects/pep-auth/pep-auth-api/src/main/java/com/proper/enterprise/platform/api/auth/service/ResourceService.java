package com.proper.enterprise.platform.api.auth.service;

import com.proper.enterprise.platform.api.auth.enums.EnableEnum;
import com.proper.enterprise.platform.api.auth.model.Menu;
import com.proper.enterprise.platform.api.auth.model.Resource;
import com.proper.enterprise.platform.api.auth.model.Role;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

public interface ResourceService {

    Resource save(Resource resource);

    /**
     * 保存资源信息
     *
     * @param resource 请求资源对象
     * @return 资源信息
     */
    Resource update(Resource resource);

    Resource get(String id);

    Resource get(String url, RequestMethod method);

    Collection<? extends Resource> findAll(Collection<String> ids, EnableEnum resourceEnable);

    /**
     * 查询所有资源
     *
     * @param enableEnum 启用停用All为全部 默认启用
     * @return 资源集合
     */
    Collection<? extends Resource> findAll(EnableEnum enableEnum);


    void delete(Resource resource);

    /**
     * 删除多条资源数据
     *
     * @param ids 以 , 分隔的待删除资源ID列表
     */
    boolean deleteByIds(String ids);

    /**
     * 更新资源状态
     *
     * @param idList 资源ID列表
     * @param enable 资源状态
     * @return 结果
     */
    Collection<? extends Resource> updateEnable(Collection<String> idList, boolean enable);

    /**
     * 获取指定资源菜单集合
     *
     * @param resourceId 资源ID
     * @param menuEnable 菜单状态
     * @return 菜单集合
     */
    Collection<? extends Menu> getResourceMenus(String resourceId, EnableEnum menuEnable);

    /**
     * 获取指定资源角色集合
     *
     * @param resourceId 资源ID
     * @param roleEnable 角色状态
     * @return 角色集合
     */
    Collection<? extends Role> getResourceRoles(String resourceId, EnableEnum roleEnable);

    /**
     * 根据传入的资源集合，过滤掉非法的(valid、enable是false)
     *
     * @param resources 待过滤的资源集合
     * @return 返回过滤后的资源集合
     */
    Collection<? extends Resource> getFilterResources(Collection<? extends Resource> resources, EnableEnum resourceEnable);
}
