package com.proper.enterprise.platform.notice.server.api.service;

import com.proper.enterprise.platform.core.entity.DataTrunk;
import com.proper.enterprise.platform.notice.server.api.model.App;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface AppDaoService {

    /**
     * 根据Id获取应用
     *
     * @param appKey 应用唯一标识
     * @return 应用
     */
    App get(String appKey);

    /**
     * 分页查询app
     *
     * @param appKey      应用唯一标识
     * @param appName     应用名称
     * @param describe    应用描述
     * @param enable      启用停用
     * @param pageRequest 分页参数
     * @return 分页对象
     */
    DataTrunk<App> findAll(String appKey, String appName, String describe, Boolean enable, PageRequest pageRequest);

    /**
     * 分页查询app, 并返回配置了哪些渠道
     *
     * @param appKey      应用唯一标识
     * @param appName     应用名称
     * @param describe    应用描述
     * @param enable      启用停用
     * @param pageRequest 分页参数
     * @return 分页对象
     */
    DataTrunk<App> findAllWithHaveConf(String appKey, String appName, String describe, Boolean enable, PageRequest pageRequest);

    /**
     * 保存应用
     *
     * @param app 应用
     * @return 应用token
     */
    App save(App app);

    /**
     * 更新app
     *
     * @param app 应用
     * @return 更新后的app
     */
    App updateApp(App app);

    /**
     * 删除App
     *
     * @param appIds 应用Id集合用，分隔
     * @return true删除 false不存在
     */
    boolean delete(String appIds);

    /**
     * 批量启用停用
     *
     * @param appIds appId集合 逗号分隔
     * @param enable true启用 false停用
     */
    void updateAppsEnable(String appIds, boolean enable);

    /**
     * app是否为启用状态 且app未被删除
     *
     * @param appKey 应用唯一标识
     * @return true false
     */
    boolean isEnable(String appKey);

    /**
     * 获取指定appKey的app配置
     * @param appKeys appKeys
     * @return Apps
     */
    List<App> findAppByAppKey(List<String> appKeys);

    /**
     * 获取所有配置的app
     * @return app
     */
    List<App> findByApp();
}
